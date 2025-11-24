package org.example.studentmanagement.dao;

import java.util.List;


public class BaseDAOImpl<T, ID> implements BaseDAO<T, ID> {

    protected Class<T> getEntityClass() {

        return null;
    }


    @Override
    public void save(Object entity) {

    }

    @Override
    public Object findById(Object o) {
        return null;
    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Object o) {

    }

    @Override
    public void delete(Object entity) {

    }
}
