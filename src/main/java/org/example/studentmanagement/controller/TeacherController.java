package org.example.studentmanagement.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.example.studentmanagement.entity.Assignment;
import org.example.studentmanagement.entity.AssignmentDetails;
import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.GradeDetails;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.entity.StudentCourseDetails;
import org.example.studentmanagement.entity.Teacher;
import org.example.studentmanagement.service.AssignmentDetailsService;
import org.example.studentmanagement.service.AssignmentService;
import org.example.studentmanagement.service.CourseService;
import org.example.studentmanagement.service.GradeDetailsService;
import org.example.studentmanagement.service.StudentCourseDetailsService;
import org.example.studentmanagement.service.TeacherService;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private StudentCourseDetailsService studentCourseDetailsService;
	
	@Autowired
	private AssignmentDetailsService assignmentDetailsService;
	
	@Autowired
	private AssignmentService assignmentService;
	
	@Autowired
	private GradeDetailsService gradeDetailsService;
	
	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		String username = authentication.getName();
		Teacher teacher = teacherService.findByUserName(username)
				.orElseThrow(() -> new RuntimeException("Teacher not found"));
		List<Course> courses = teacher.getCourses();

		model.addAttribute("username", username);
		model.addAttribute("teacher", teacher);
		model.addAttribute("courses", courses);
		return "teacher/dashboard";
	}
	
	@GetMapping("/{teacherId}/courses")
	public String showTeacherCourses(@PathVariable("teacherId") int teacherId, Model theModel) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		List<Course> courses = teacher.getCourses();
		
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("courses", courses);
		return "teacher/teacher-courses";
	}
	
	@GetMapping("/{teacherId}/courses/{courseId}")
	public String showTeacherCourseDetails(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId, Model theModel) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		Course course = courseService.findCourseById(courseId);
		List<Course> courses = teacher.getCourses();
		List<Student> students = course.getStudents();

		// Load assignments directly from the course, not from StudentCourseDetails
		List<Assignment> assignments = assignmentService.findByCourseId(courseId);
		if(assignments != null && assignments.size() > 0) {
			for(Assignment assignment : assignments) {
				int daysRemaining = findDayDifference(assignment);
				assignment.setDaysRemaining(daysRemaining);
			}
		} else {
			assignments = null;
		}

		if(students != null && students.size() != 0) {
			List<GradeDetails> gradeDetailsList = new ArrayList<>();
			for(Student student : students) {
				// Load grade details directly from grade_details table
				List<GradeDetails> studentGrades = gradeDetailsService.findByStudentIdAndCourseId(student.getId(), courseId);
				if(studentGrades != null && studentGrades.size() > 0) {
					// Add the first grade record (assuming one grade per student per course)
					gradeDetailsList.add(studentGrades.get(0));
				} else {
					// Add empty grade details for students without grades
					GradeDetails emptyGrade = new GradeDetails();
					emptyGrade.setStudentId(student.getId());
					emptyGrade.setCourseId(courseId);
					emptyGrade.setScore(0.0);
					emptyGrade.setMaxScore(100.0);
					gradeDetailsList.add(emptyGrade);
				}
			}
			HashMap<List<Student>, List<GradeDetails>> studentGradeList = new HashMap<>();
			studentGradeList.put(students, gradeDetailsList);
			theModel.addAttribute("studentGradeList", studentGradeList);
		}

		theModel.addAttribute("assignments", assignments);
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("course", course);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("students", students);

		return "teacher/teacher-course-details";
	}
	
	
	@GetMapping("/{teacherId}/courses/{courseId}/editGrades")
	public String editGradesForm(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId, Model theModel) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		Course course = courseService.findCourseById(courseId);
		List<Course> courses = teacher.getCourses();
		List<Student> students = course.getStudents();

		List<GradeDetails> gradeDetailsList = new ArrayList<>();
		for(Student student : students) {
			// Load grade details directly from grade_details table
			List<GradeDetails> studentGrades = gradeDetailsService.findByStudentIdAndCourseId(student.getId(), courseId);
			if(studentGrades != null && studentGrades.size() > 0) {
				// Add the first grade record (assuming one grade per student per course)
				gradeDetailsList.add(studentGrades.get(0));
			} else {
				// Create a default empty GradeDetails for students without grades
				GradeDetails emptyGrade = new GradeDetails();
				emptyGrade.setStudentId(student.getId());
				emptyGrade.setCourseId(courseId);
				emptyGrade.setScore(0.0);
				emptyGrade.setMaxScore(100.0);
				gradeDetailsList.add(emptyGrade);
			}
		}

		HashMap<List<Student>, List<GradeDetails>> studentGradeList = new HashMap<>();
		studentGradeList.put(students, gradeDetailsList);

		theModel.addAttribute("studentGradeList", studentGradeList);
		theModel.addAttribute("course", course);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("students", students);
		theModel.addAttribute("gradeDetailsList", gradeDetailsList);

		return "teacher/edit-grades-form";
	}
	
	
	@PostMapping("/{teacherId}/courses/{courseId}/editGrades/save/{gradeDetailsId}")
	public String modifyGrades(@ModelAttribute GradeDetails gradeDetails,
			@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId,
			@PathVariable("gradeDetailsId") int gradeDetailsId) throws Exception {

		// Ensure courseId is set from path variable (in case form binding fails)
		gradeDetails.setCourseId(courseId);

		// Set common fields
		gradeDetails.setGradedByTeacherId(teacherId);
		gradeDetails.setGradedDate(java.time.LocalDate.now().toString());

		if (gradeDetailsId != 0) {
			// Update existing grade - set the ID and save
			gradeDetails.setId(gradeDetailsId);
		}

		// Save the grade (will insert if ID is 0, update if ID exists)
		gradeDetailsService.save(gradeDetails);

	    return "redirect:/teacher/" + teacherId + "/courses/" + courseId;
	}
	
	
	@GetMapping("/{teacherId}/courses/{courseId}/assignments/{assignmentId}")
	public String showAssignmentDetails(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId,
			@PathVariable("assignmentId") int assignmentId, Model theModel) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		Course course = courseService.findCourseById(courseId);
		List<Student> students = course.getStudents();
		List<Course> courses = teacher.getCourses();
		
		Assignment assignment = assignmentService.findById(assignmentId);
		List<Assignment> assignments = new ArrayList<>();
		List<StudentCourseDetails> studentCourseDetails = new ArrayList<>();
		List<AssignmentDetails> studentCourseAssignmentDetails = new ArrayList<>();
		List<String> assignmentStatuses = new ArrayList<>();
		List<GradeDetails> assignmentGrades = new ArrayList<>();
		
		for(Student student : students) {
			StudentCourseDetails scd = studentCourseDetailsService.findByStudentAndCourseId(student.getId(), courseId);
			if (scd != null) {
				AssignmentDetails studentCourseAssignmentDetail = assignmentDetailsService.
						findByAssignmentAndStudentCourseDetailsId(assignmentId, scd.getId());
				studentCourseAssignmentDetails.add(studentCourseAssignmentDetail);
				if(studentCourseAssignmentDetail != null && studentCourseAssignmentDetail.getIsDone() == 0) {
					assignmentStatuses.add("incomplete");
				} else if(studentCourseAssignmentDetail != null) {
					assignmentStatuses.add("completed");
				} else {
					assignmentStatuses.add("not assigned");
				}

				// Load assignment grade for this student
				GradeDetails gradeForAssignment = null;
				List<GradeDetails> studentGrades = gradeDetailsService.findByStudentIdAndCourseId(student.getId(), courseId);
				if (studentGrades != null && assignment != null) {
					for (GradeDetails grade : studentGrades) {
						if (grade.getAssignmentName() != null && grade.getAssignmentName().equals(assignment.getTitle())) {
							gradeForAssignment = grade;
							break;
						}
					}
				}
				assignmentGrades.add(gradeForAssignment);
			} else {
				assignmentStatuses.add("not enrolled");
				assignmentGrades.add(null);
			}
		}
				
		HashMap<List<Student>, List<String>> list = new HashMap<>();
		list.put(students, assignmentStatuses);
		
		theModel.addAttribute("list", list);
		theModel.addAttribute("assignmentDetails", studentCourseAssignmentDetails);
		theModel.addAttribute("students", students);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("course", course);
		theModel.addAttribute("assignment", assignment);
		theModel.addAttribute("assignmentGrades", assignmentGrades);

		return "teacher/teacher-assignment-status";
	}
	
	
	
	@GetMapping("/{teacherId}/courses/{courseId}/assignments/{assignmentId}/delete")
	public String deleteAssignment(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId,
			@PathVariable("assignmentId") int assignmentId) {
		assignmentService.deleteAssignmentById(assignmentId);

		return "redirect:/teacher/" + teacherId + "/courses/" + courseId;
	}

	@PostMapping("/{teacherId}/courses/{courseId}/assignments/{assignmentId}/gradeStudent/{studentId}")
	public String gradeAssignmentForStudent(
			@PathVariable("teacherId") int teacherId,
			@PathVariable("courseId") int courseId,
			@PathVariable("assignmentId") int assignmentId,
			@PathVariable("studentId") int studentId,
			@ModelAttribute GradeDetails gradeDetails) {

		Assignment assignment = assignmentService.findById(assignmentId);

		// Check if grade already exists for this student/assignment combination
		List<GradeDetails> existingGrades = gradeDetailsService.findByStudentIdAndCourseId(studentId, courseId);
		GradeDetails existingGrade = null;
		if (existingGrades != null && assignment != null) {
			for (GradeDetails grade : existingGrades) {
				if (grade.getAssignmentName() != null && grade.getAssignmentName().equals(assignment.getTitle())) {
					existingGrade = grade;
					break;
				}
			}
		}

		// Set all required fields
		if (existingGrade != null) {
			gradeDetails.setId(existingGrade.getId());
		}
		gradeDetails.setStudentId(studentId);
		gradeDetails.setCourseId(courseId);
		if (assignment != null) {
			gradeDetails.setAssignmentName(assignment.getTitle());
		}
		gradeDetails.setGradedByTeacherId(teacherId);
		gradeDetails.setGradedDate(java.time.LocalDate.now().toString());

		// Save the grade
		gradeDetailsService.save(gradeDetails);

		return "redirect:/teacher/" + teacherId + "/courses/" + courseId + "/assignments/" + assignmentId;
	}

	@GetMapping("/{teacherId}/courses/{courseId}/addNewAssignment")
	public String addNewAssignment(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId, Model theModel) {
		Assignment assignment = new Assignment();
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		List<Course> courses = teacher.getCourses();
		
		theModel.addAttribute("assignment", assignment);
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("course", courseService.findCourseById(courseId));
		theModel.addAttribute("courses", courses);
		
		return "teacher/assignment-form";
	}
	
	@PostMapping("/{teacherId}/courses/{courseId}/addNewAssignment/save")
	public String saveAssignment(@Valid @ModelAttribute("assignment") Assignment assignment, BindingResult theBindingResult,
			@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId, Model theModel) {

		Teacher teacher = teacherService.findByTeacherId(teacherId);
		List<Course> courses = teacher.getCourses();

		if (theBindingResult.hasErrors()) {
			theModel.addAttribute("teacher", teacher);
			theModel.addAttribute("courses", courses);
			theModel.addAttribute("course", courseService.findCourseById(courseId));
			return "teacher/assignment-form";
		}

		// Set required fields before saving
		assignment.setCourseId(courseId);
		assignment.setCreatedByTeacherId(teacherId);
		assignment.setCreatedDate(java.time.LocalDate.now().toString());
		if (assignment.getStatus() == null || assignment.getStatus().isEmpty()) {
			assignment.setStatus("active");
		}

		// assignment.setDaysRemaining(findDayDifference(assignment));
		assignmentService.save(assignment);
		
		Course course = courseService.findCourseById(courseId);
		List<Student> students = course.getStudents();
		
		for(Student student : students) {
			StudentCourseDetails studentCourseDetails = studentCourseDetailsService.findByStudentAndCourseId(student.getId(), courseId);
			AssignmentDetails assignmentDetail = new AssignmentDetails();
			assignmentDetail.setAssignmentId(assignment.getId());
			assignmentDetail.setStudentCourseDetailsId(studentCourseDetails.getId());
			assignmentDetail.setIsDone(0);
			assignmentDetailsService.save(assignmentDetail);
		}
		
		
		theModel.addAttribute("teacher", teacher);
		
		return "redirect:/teacher/" + teacherId + "/courses/" + courseId;
	}
	
	
	
	
	private int findDayDifference(Assignment assignment) {
		String dateString = assignment.getDueDate();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			LocalDate dueDate = LocalDate.parse(dateString, dtf);
			LocalDate today = LocalDate.now();
			long dayDiff = java.time.temporal.ChronoUnit.DAYS.between(today, dueDate);

			return (int) dayDiff;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}
	
}

