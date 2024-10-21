package com.vlad.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractRepository<K extends Serializable, E> implements BaseRepository<K, E> {

    private final Class<E> entityClass;
    protected final EntityManager entityManager;

    @Override
    public E save(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(E entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            E managedEntity = entityManager.merge(entity);
            entityManager.remove(managedEntity);
        }
        entityManager.flush();
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder().createQuery(entityClass);
        criteria.from(entityClass);
        return entityManager.createQuery(criteria).getResultList();
    }
}
