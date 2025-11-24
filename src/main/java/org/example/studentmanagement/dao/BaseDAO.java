package org.example.studentmanagement.dao;

import java.util.List;

public interface BaseDAO<T, ID> {

    void save(T entity);
    T findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    void delete(T entity);
}