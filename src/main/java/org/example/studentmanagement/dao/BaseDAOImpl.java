package org.example.studentmanagement.dao;

import java.util.List;


public abstract class BaseDAOImpl<T, ID> implements BaseDAO<T, ID> {

    protected Class<T> getEntityClass() {

        return null;
    }


    @Override
    public void save(T entity) {
        throw new UnsupportedOperationException("save not implemented");
    }

    @Override
    public T findById(ID id) {
        throw new UnsupportedOperationException("findById not implemented");
    }

    @Override
    public List<T> findAll() {
        throw new UnsupportedOperationException("findAll not implemented");
    }

    @Override
    public void deleteById(ID id) {
        throw new UnsupportedOperationException("deleteById not implemented");
    }

    @Override
    public void delete(T entity) {
        throw new UnsupportedOperationException("delete not implemented");
    }
}
