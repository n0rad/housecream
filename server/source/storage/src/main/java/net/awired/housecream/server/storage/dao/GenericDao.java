/**
 * Software Name : ON-webservice (REST API)
 *
 * Copyright 2009 - 2011 Orange Vallee.
 *
 * This software is the confidential and proprietary information of France Telecom.
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with France Telecom.
 */

package net.awired.housecream.server.storage.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import net.awired.ajsl.persistence.dao.AbstractDAO;
import net.awired.ajsl.persistence.entity.IdEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class GenericDao<T extends IdEntity<Long>> implements AbstractDAO<T, Long> {

    private Class<T> clazz;

    @PersistenceContext
    protected EntityManager em;

    protected GenericDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void persist(T entity) {
        em.persist(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(T entity) {
        em.remove(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T merge(T entity) {
        return em.merge(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public T findById(Long id) throws DoesNotExistsException {
        T result = em.find(clazz, id);
        if (result == null) {
            throw new DoesNotExistsException("Object '" + clazz.getName() + "' with id '" + id
                    + "' not found in database");
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected T findSingleResult(TypedQuery<T> query) throws DoesNotExistsException, NotUniqueResultException {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new DoesNotExistsException("Object '" + clazz.getName() + "' with parameters '"
                    + Arrays.toString(query.getParameters().toArray()) + "' not found in database", e);
        } catch (NonUniqueResultException e) {
            throw new NotUniqueResultException("Object '" + clazz.getName() + "' with parameters '"
                    + Arrays.toString(query.getParameters().toArray()) + "' expected only one in database", e);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected <E> E findGenericSingleResult(TypedQuery<E> query) throws DoesNotExistsException,
            NotUniqueResultException {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new DoesNotExistsException("Result of " + query + " with parameters '"
                    + Arrays.toString(query.getParameters().toArray()) + "' not found in database", e);
        } catch (NonUniqueResultException e) {
            throw new NotUniqueResultException("Result of " + query + "' with parameters '"
                    + Arrays.toString(query.getParameters().toArray()) + "' expected only one in database", e);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected Object findSingleResult(Query query) throws DoesNotExistsException, NotUniqueResultException {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new DoesNotExistsException("Object '" + clazz.getName() + "' with parameters '"
                    + Arrays.toString(query.getParameters().toArray()) + "' not found in database", e);
        } catch (NonUniqueResultException e) {
            throw new NotUniqueResultException("Object '" + clazz.getName() + "' with parameters '"
                    + Arrays.toString(query.getParameters().toArray()) + "' expected only one in database", e);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected List<Object[]> findMultiResultList(Query query) {
        List<Object[]> result = query.getResultList();
        if (result == null) {
            result = new ArrayList<Object[]>();
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected List<Object> findList(Query query) {
        List<Object> result = query.getResultList();
        if (result == null) {
            result = new ArrayList<Object>();
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected <U> List<U> findList(TypedQuery<U> query) {
        List<U> result = query.getResultList();
        if (result == null) {
            result = new ArrayList<U>();
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected long count(TypedQuery<Long> query) {
        return query.getSingleResult();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected void updateQuery(Query query) {
        query.executeUpdate();
    }

    @Override
    public T find(Long id) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<T> find(Long[] ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<T> find(Collection<Long> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<T> find(Collection<Long> ids, Class<?>... flags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<T> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T save(T object) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void save(T... objects) {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(Collection<T> objects) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh(T entity) {
        // TODO Auto-generated method stub

    }
}
