package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.AssignmentDetailsDAO;
import org.example.studentmanagement.entity.AssignmentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentDetailsServiceImpl implements AssignmentDetailsService {

    private final AssignmentDetailsDAO assignmentDetailsDAO;

    @Autowired
    public AssignmentDetailsServiceImpl(AssignmentDetailsDAO assignmentDetailsDAO) {
        this.assignmentDetailsDAO = assignmentDetailsDAO;
    }

    @Override
    public AssignmentDetails findByAssignmentAndStudentCourseDetailsId(int assignmentId, int studentCourseDetailsId) {
        return assignmentDetailsDAO.findByAssignmentIdAndStudentCourseDetailsId(assignmentId, studentCourseDetailsId);
    }

    @Override
    public List<AssignmentDetails> findAll() {
        return assignmentDetailsDAO.findAll();
    }

    @Override
    public void save(AssignmentDetails assignmentDetails) {
        assignmentDetailsDAO.save(assignmentDetails);
    }

    @Override
    public void deleteById(int id) {
        assignmentDetailsDAO.deleteById(id);
    }
}

