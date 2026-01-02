package org.example.studentmanagement.controller;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.example.studentmanagement.entity.Assignment;
import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.GradeDetails;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.entity.StudentCourseDetails;
import org.example.studentmanagement.entity.Teacher;
import org.example.studentmanagement.service.AdminService;
import org.example.studentmanagement.service.CourseService;
import org.example.studentmanagement.service.GradeDetailsService;
import org.example.studentmanagement.service.StudentCourseDetailsService;
import org.example.studentmanagement.service.StudentService;
import org.example.studentmanagement.service.TeacherService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private StudentCourseDetailsService studentCourseDetailsService;
	
	@Autowired
	private GradeDetailsService gradeDetailsService;
	
	private int teacherDeleteErrorValue; //used for deleting teacher, 0 means the teacher has not any assigned courses, 1 means it has
	
	@GetMapping("/adminPanel")
	public String showAdminPanel() {
		
		return "admin/admin-panel";
	}
	
	@GetMapping("/adminInfo")
	public String showAdminInfo(Model theModel) {
		int courseSize = courseService.findAllCourses().size();
		theModel.addAttribute("courseSize", courseSize);
		int studentSize = studentService.findAllStudents().size();
		theModel.addAttribute("studentSize", studentSize);
		int teacherSize = teacherService.findAllTeachers().size();
		theModel.addAttribute("teacherSize", teacherSize);
		return "admin/admin-info";
	}
	
	@GetMapping("/students")
	public String showStudentList(Model theModel) {
		theModel.addAttribute("students", studentService.findAllStudents());
		
		return "admin/student-list"; 
	}
	
	@RequestMapping("/students/delete")
	public String deleteStudent(@RequestParam("studentId") int studentId) {
		adminService.deleteStudentWithRelatedData(studentId);
		return "redirect:/admin/students";
	}
	
	@GetMapping("/students/add")
	public String addStudent(Model theModel) {
		Student student = new Student();
		theModel.addAttribute("student", student);
		
		return "admin/student-form";
	}
	
	@PostMapping("/students/save")
	public String saveStudent(@Valid @ModelAttribute("student") Student theStudent, 
			BindingResult theBindingResult) {
		
		if (theBindingResult.hasErrors()) {
			return "admin/student-form";
		}
		
		// Use AdminService for admin operations
		if (theStudent.getId() == 0) {
			adminService.createStudent(theStudent);
		} else {
			adminService.updateStudent(theStudent);
		}
		
		return "redirect:/admin/students";
	}
	
	@GetMapping("/students/edit/{studentId}")
	public String editStudent(@PathVariable("studentId") int studentId, Model theModel) {
		Student student = studentService.findByStudentId(studentId);
		
		if (student == null) {
			return "redirect:/admin/students";
		}
		
		theModel.addAttribute("student", student);
		theModel.addAttribute("isEdit", true);
		
		return "admin/student-form";
	}
	
	@GetMapping("/students/{studentId}/courses")
	public String editCoursesForStudent(@PathVariable("studentId") int studentId, Model theModel) {
		Student student = studentService.findByStudentId(studentId);
		if (student == null) {
			return "redirect:/admin/students";
		}
		// ensure we don't pass a null list to the template
		List<Course> courses = student.getCourses();
		if (courses == null) {
			courses = new ArrayList<>();
		}
		theModel.addAttribute("student", student);
		theModel.addAttribute("courses", courses);
		
		return "admin/student-course-list";
	}
	
	@GetMapping("/students/{studentId}/addCourse")
	public String addCourseToStudent(@PathVariable("studentId") int studentId, Model theModel) {
		Student student = studentService.findByStudentId(studentId);
		if (student == null) {
			return "redirect:/admin/students";
		}
		// avoid NPE if student's courses is null
		if (student.getCourses() == null) {
			student.setCourses(new ArrayList<>());
		}
		List<Course> courses = courseService.findAllCourses();
		
		for (int i = 0; i < courses.size(); i++) { // finding the courses that the current student has not enrolled yet
			if (student.getCourses().contains(courses.get(i))) {
				courses.remove(i);
				i--;
			}
		}
		theModel.addAttribute("student", student);
		theModel.addAttribute("courses", courses); //unenrolled courses are displayed as drop-down list
		theModel.addAttribute("listSize", courses.size());
		return "admin/add-course";
	}
	
	@RequestMapping("/students/{studentId}/addCourse/save")
	public String saveCourseToStudent(@PathVariable("studentId") int studentId, @RequestParam("courseId") int courseId) {
		
		StudentCourseDetails sc = new StudentCourseDetails(0, studentId, courseId, new GradeDetails(), new ArrayList<Assignment>());
		studentCourseDetailsService.save(sc);
			
		return "redirect:/admin/students/" + studentId + "/courses";
	}
	
	
	@GetMapping("/students/{studentId}/courses/delete/{courseId}")
	public String deleteCourseFromStudent(@PathVariable("studentId") int studentId, @PathVariable("courseId") int courseId) {
		studentCourseDetailsService.deleteByStudentAndCourseId(studentId, courseId);
		
		List<GradeDetails> gradeDetailsList = gradeDetailsService.findByStudentIdAndCourseId(studentId, courseId);
		for (GradeDetails gd : gradeDetailsList) {
			gradeDetailsService.deleteById(gd.getId());
		}
		
		return "redirect:/admin/students/" + studentId + "/courses";
	}
	
	
	@GetMapping("/teachers")
	public String showTeacherList(Model theModel) {
		theModel.addAttribute("teachers", teacherService.findAllTeachers());
		theModel.addAttribute("error", teacherDeleteErrorValue); 
		teacherDeleteErrorValue = 0; //0 means the teacher has not any assigned courses, 1 means it has
		return "admin/teacher-list";
	}
	
	@GetMapping("/teachers/delete")
	public String deleteTeacher(@RequestParam("teacherId") int teacherId) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		if (teacher == null) {
			return "redirect:/admin/teachers";
		}
		// guard against null courses list
		List<Course> courses = teacher.getCourses();
		if (courses == null || courses.isEmpty()) { // if no assigned courses, safe to delete
			teacherService.deleteTeacherById(teacherId);
			teacherDeleteErrorValue = 0;
		} else {
			// Update courses to set teacher_id to null
			for (Course course : courses) {
				course.setTeacher(null);
				courseService.save(course);
			}
			teacherService.deleteTeacherById(teacherId);
			teacherDeleteErrorValue = 0;
		}
		
		return "redirect:/admin/teachers";
	}
	
	@GetMapping("/teachers/add")
	public String addTeacher(Model theModel) {
		Teacher teacher = new Teacher();
		theModel.addAttribute("teacher", teacher);
		
		return "admin/teacher-form";
	}
	
	@PostMapping("/teachers/save")
	public String saveTeacher(@Valid @ModelAttribute("teacher") Teacher theTeacher, 
			BindingResult theBindingResult) {
		
		if (theBindingResult.hasErrors()) {
			return "admin/teacher-form";
		}
		
		// Use AdminService for admin operations
		if (theTeacher.getId() == 0) {
			adminService.createTeacher(theTeacher);
		} else {
			adminService.updateTeacher(theTeacher);
		}
		
		return "redirect:/admin/teachers";
	}
	
	@GetMapping("/teachers/edit/{teacherId}")
	public String editTeacher(@PathVariable("teacherId") int teacherId, Model theModel) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		
		if (teacher == null) {
			return "redirect:/admin/teachers";
		}
		
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("isEdit", true);
		
		return "admin/teacher-form";
	}
	
	
	@GetMapping("/addCourse")
	public String addCourse(Model theModel) {
		//add course form has a select teacher field where all teachers registered are showed as drop-down list
		List<Teacher> teachers = teacherService.findAllTeachers(); 
		
		theModel.addAttribute("course", new Course());
		theModel.addAttribute("teachers", teachers);
		
		return "admin/course-form";
	}
	
	@PostMapping("/saveCourse")
	public String saveCourse(@Valid @ModelAttribute("course") Course theCourse, 
			BindingResult theBindingResult, @RequestParam("teacherId") int teacherId, Model theModel) {
		
		if (theBindingResult.hasErrors()) { //course form has data validation rules. If fields are not properly filled out, form is showed again
			List<Teacher> teachers = teacherService.findAllTeachers();
			theModel.addAttribute("teachers", teachers);
			return "admin/course-form";
		}
		
		theCourse.setTeacher(teacherService.findByTeacherId(teacherId)); //setTeacher method also sets the teacher's course as this	
		courseService.save(theCourse);
		
		return "redirect:/admin/adminPanel"; 
	}
	
	@GetMapping("/courses")
	public String showCourses(Model theModel) {
		theModel.addAttribute("courses", courseService.findAllCourses());	
		
		return "admin/course-list";
	}
	
	
	@GetMapping("/courses/delete")
	public String deleteCourse(@RequestParam("courseId") int courseId) {
		Course course = courseService.findCourseById(courseId);
		List<Student> students = course.getStudents();

		for(Student student : students) {
			// Delete student course details
			studentCourseDetailsService.deleteByStudentAndCourseId(student.getId(), courseId);

			// Delete all grade details for this student and course
			List<GradeDetails> gradeDetailsList = gradeDetailsService.findByStudentIdAndCourseId(student.getId(), courseId);
			for (GradeDetails gd : gradeDetailsList) {
				gradeDetailsService.deleteById(gd.getId());
			}
		}

		courseService.deleteCourseById(courseId);
		return "redirect:/admin/courses";
	}
	
	@GetMapping("/courses/{courseId}/students")
	public String showSudents(@PathVariable("courseId") int courseId, Model theModel) {		
		Course course = courseService.findCourseById(courseId);
		List<Student> students = course.getStudents();
		Teacher teacher = course.getTeacher();
		theModel.addAttribute("students", students);
		theModel.addAttribute("course", course);
		theModel.addAttribute("teacher", teacher);
		return "admin/course-student-list";
	}
	
	
	
	@GetMapping("/courses/{courseId}/students/delete")
	public String deleteStudentFromCourse(@PathVariable("courseId") int courseId, @RequestParam("studentId") int studentId) {
		studentCourseDetailsService.deleteByStudentAndCourseId(studentId, courseId);
		
		List<GradeDetails> gradeDetailsList = gradeDetailsService.findByStudentIdAndCourseId(studentId, courseId);
		for (GradeDetails gd : gradeDetailsList) {
			gradeDetailsService.deleteById(gd.getId());
		}
		
		return "redirect:/admin/courses/" + courseId + "/students";
	}
	
	@GetMapping("/courses/{courseId}/students/addStudent")
	public String addStudentToCourse(@PathVariable("courseId") int courseId, Model theModel) {
		Course course = courseService.findCourseById(courseId);
		List<Student> students = studentService.findAllStudents();
		
		for(int i = 0; i < students.size(); i++) { 
			if(course.getStudents().contains(students.get(i))) {
				students.remove(students.get(i));
				i--;
			}
		}
		theModel.addAttribute("students", students); //all students who are not enrolled to the current course yet
		theModel.addAttribute("course", course);
		theModel.addAttribute("listSize", students.size());
		return "admin/add-student";
		
	}
	
	@RequestMapping("/courses/{courseId}/students/addStudent/save")
	public String saveStudentToCourse(@RequestParam("studentId") int studentId, @PathVariable("courseId") int courseId) {
		
		StudentCourseDetails sc = new StudentCourseDetails(0, studentId, courseId, null, new ArrayList<Assignment>());
		studentCourseDetailsService.save(sc);
		
		return "redirect:/admin/courses/" + courseId + "/students";
	}
}
