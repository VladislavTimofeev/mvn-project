package com.vlad.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<K extends Serializable, E> {

    E save(E entity);

    void delete(E entity);

    void update(E entity);

    Optional<E> findById(K id);

    List<E> findAll();
}
