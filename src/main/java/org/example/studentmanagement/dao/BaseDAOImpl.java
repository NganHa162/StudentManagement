package org.example.studentmanagement.dao;

import java.util.List;


public class BaseDAOImpl<T, ID> implements BaseDAO<T, ID> {

    protected Class<T> getEntityClass() {
        return null;
    }

    @Override
    public void save(T entity) {
        // Default implementation - can be overridden by subclasses
    }

    @Override
    public T findById(ID id) {
        // Default implementation - can be overridden by subclasses
        return null;
    }

    @Override
    public List<T> findAll() {
        // Default implementation - can be overridden by subclasses
        return List.of();
    }

    @Override
    public void deleteById(ID id) {
        // Default implementation - can be overridden by subclasses
    }

    @Override
    public void delete(T entity) {
        // Default implementation - can be overridden by subclasses
    }
}
