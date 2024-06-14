package com.ra.project_module4.service;

import org.springframework.data.domain.Page;

public interface IGenericService<T, E> {
    T findById(E id);

    Page<T> findAll();

    T save(T entity);

    void deleteById(E id);
}
