package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.GradeDetailsDAO;
import org.example.studentmanagement.entity.GradeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeDetailsServiceImpl implements GradeDetailsService {

    private final GradeDetailsDAO gradeDetailsDAO;

    @Autowired
    public GradeDetailsServiceImpl(GradeDetailsDAO gradeDetailsDAO) {
        this.gradeDetailsDAO = gradeDetailsDAO;
    }

    @Override
    public GradeDetails findById(int id) {
        return gradeDetailsDAO.findById(id);
    }

    @Override
    public List<GradeDetails> findAll() {
        return gradeDetailsDAO.findAll();
    }

    @Override
    public void save(GradeDetails gradeDetails) {
        gradeDetailsDAO.save(gradeDetails);
    }

    @Override
    public void deleteById(int id) {
        gradeDetailsDAO.deleteById(id);
    }
}

