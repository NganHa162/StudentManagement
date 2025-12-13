package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.AssignmentDAO;
import org.example.studentmanagement.entity.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentDAO assignmentDAO;

    @Autowired
    public AssignmentServiceImpl(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

    @Override
    public Assignment findById(int id) {
        return assignmentDAO.findById(id);
    }

    @Override
    public List<Assignment> findAll() {
        return assignmentDAO.findAll();
    }

    @Override
    public void save(Assignment assignment) {
        assignmentDAO.save(assignment);
    }

    @Override
    public void deleteAssignmentById(int id) {
        assignmentDAO.deleteById(id);
    }
}

