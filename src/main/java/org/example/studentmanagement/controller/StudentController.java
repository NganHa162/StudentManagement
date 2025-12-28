package org.example.studentmanagement.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.example.studentmanagement.entity.Assignment;
import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.GradeDetails;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.entity.AssignmentDetails;
import org.example.studentmanagement.entity.StudentCourseDetails;
import org.example.studentmanagement.service.CourseService;
import org.example.studentmanagement.service.AssignmentDetailsService;
import org.example.studentmanagement.service.StudentCourseDetailsService;
import org.example.studentmanagement.service.StudentService;
import org.example.studentmanagement.service.GradeDetailsService;
import org.example.studentmanagement.service.AssignmentService;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private StudentCourseDetailsService studentCourseDetailsService;

	@Autowired
	private AssignmentDetailsService assignmentDetailsService;

	@Autowired
	private GradeDetailsService gradeDetailsService;

	@Autowired
	private AssignmentService assignmentService;

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		String username = authentication.getName();
		model.addAttribute("username", username);

		// Add student object to model for navigation links
		java.util.Optional<Student> studentOpt = studentService.findByUserName(username);
		if (studentOpt.isPresent()) {
			model.addAttribute("student", studentOpt.get());
		}

		return "student/dashboard";
	}

	@GetMapping("/{studentId}/courses")
	public String showStudentPanel(@PathVariable("studentId") int studentId, Model theModel) {
		Student student = studentService.findByStudentId(studentId); // accessing student logged in
		List<Course> courses = student.getCourses();

		theModel.addAttribute("student", student);
		theModel.addAttribute("courses", courses);
		return "student/student-courses";
	}

	@GetMapping("/{studentId}/courses/{courseId}")
	public String showStudentCourse(@PathVariable("studentId") int studentId, @PathVariable("courseId") int courseId,
			Model theModel) {
		Student student = studentService.findByStudentId(studentId);
		List<Course> courses = student.getCourses();
		Course course = courseService.findCourseById(courseId);
		StudentCourseDetails studentCourseDetails = studentCourseDetailsService.findByStudentAndCourseId(studentId,
				courseId);

		// Use explicit service call to fetch assignments (Fixes Lazy Loading / NPE)
		List<Assignment> assignments = assignmentService.findByCourseId(courseId);
		if (assignments == null) {
			assignments = new java.util.ArrayList<>();
		} // Ensure assignments is never null

		if (studentCourseDetails != null) {
			studentCourseDetails.setAssignments(assignments);
		}

		for (Assignment assignment : assignments) {
			int daysRemaining = findDayDifference(assignment);
			assignment.setDaysRemaining(daysRemaining);
		}

		GradeDetails gradeDetails = null;
		if (studentCourseDetails != null && studentCourseDetails.getGradeDetails() != null) {
			gradeDetails = studentCourseDetails.getGradeDetails();
		} else {
			// Handle GradeDetails list safely as well from Service if needed, or just new
			// object
			// Original patch used gradeDetailsService.findByStudentIdAndCourseId
			List<GradeDetails> allGrades = gradeDetailsService.findByStudentIdAndCourseId(studentId, courseId);
			if (allGrades != null && !allGrades.isEmpty()) {
				gradeDetails = allGrades.get(0);
			} else {
				gradeDetails = new GradeDetails();
			}
			if (studentCourseDetails != null)
				studentCourseDetails.setGradeDetails(gradeDetails);
		}

		theModel.addAttribute("assignments", assignments);
		theModel.addAttribute("course", course);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("student", student);
		theModel.addAttribute("gradeDetails", gradeDetails);

		return "student/student-course-detail";
	}

	@GetMapping("/{studentId}/courses/{courseId}/assignment/{assignmentId}")
	public String showStudentAssignment(@PathVariable("studentId") int studentId,
			@PathVariable("courseId") int courseId,
			@PathVariable("assignmentId") int assignmentId, Model theModel) {
		Student student = studentService.findByStudentId(studentId);
		List<Course> courses = student.getCourses();
		Course course = courseService.findCourseById(courseId);
		StudentCourseDetails studentCourseDetails = studentCourseDetailsService.findByStudentAndCourseId(studentId,
				courseId);

		Assignment assignment = assignmentService.findById(assignmentId);

		if (assignment == null) {
			return "redirect:/student/" + studentId + "/courses/" + courseId;
		}

		// Calculate days remaining
		int daysRemaining = findDayDifference(assignment);
		assignment.setDaysRemaining(daysRemaining);

		AssignmentDetails assignmentDetails = null;
		if (studentCourseDetails != null) {
			assignmentDetails = assignmentDetailsService
					.findByAssignmentAndStudentCourseDetailsId(assignmentId, studentCourseDetails.getId());
		}

		if (assignmentDetails == null) {
			assignmentDetails = new AssignmentDetails();
			assignmentDetails.setAssignmentId(assignmentId);
			// Default to incomplete (0)
			assignmentDetails.setIsDone(0);
		}

		// Get grade for this assignment
		GradeDetails assignmentGrade = null;
		if (assignment != null) {
			List<GradeDetails> allGrades = gradeDetailsService.findByStudentIdAndCourseId(studentId, courseId);
			if (allGrades != null) {
				for (GradeDetails grade : allGrades) {
					if (grade.getAssignmentName() != null && grade.getAssignmentName().equals(assignment.getTitle())) {
						assignmentGrade = grade;
						break;
					}
				}
			}
		}

		theModel.addAttribute("assignment", assignment);
		theModel.addAttribute("assignmentDetails", assignmentDetails);
		theModel.addAttribute("assignmentGrade", assignmentGrade);
		theModel.addAttribute("course", course);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("student", student);

		return "student/student-assignment-detail";
	}

	@GetMapping("/{studentId}/courses/{courseId}/markAsCompleted/{assignmentId}")
	public String markAsCompleted(@PathVariable("studentId") int studentId, @PathVariable("courseId") int courseId,
			@PathVariable("assignmentId") int assignmentId, Model theModel) {
		// Student student = studentService.findByStudentId(studentId);
		// Course course = courseService.findCourseById(courseId);
		StudentCourseDetails studentCourseDetails = studentCourseDetailsService.findByStudentAndCourseId(studentId,
				courseId);

		if (studentCourseDetails != null) {
			AssignmentDetails assignmentDetails = assignmentDetailsService
					.findByAssignmentAndStudentCourseDetailsId(assignmentId, studentCourseDetails.getId());

			if (assignmentDetails == null) {
				assignmentDetails = new AssignmentDetails();
				assignmentDetails.setAssignmentId(assignmentId);
				assignmentDetails.setStudentCourseDetailsId(studentCourseDetails.getId());
			}

			assignmentDetails.setIsDone(1); // assignment is completed
			assignmentDetailsService.save(assignmentDetails);
		}

		return "redirect:/student/" + studentId + "/courses/" + courseId + "/assignment/" + assignmentId;
	}

	// helper method to find day difference between assignment due date and today
	private int findDayDifference(Assignment assignment) {
		String dateString = assignment.getDueDate();
		if (dateString == null) {
			return 0;
		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			LocalDate dueDate = LocalDate.parse(dateString, dtf);
			LocalDate today = LocalDate.now();
			int dayDiff = (int) Duration.between(today.atStartOfDay(), dueDate.atStartOfDay()).toDays();

			return dayDiff;

		} catch (Exception e) {
			// e.printStackTrace();
			return 0;
		}
	}
}
