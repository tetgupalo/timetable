package com.kpi.labs.timetable.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CRUD<T, K> {
    K create(T element);

    void update(T element);

    void delete(T element);

    T load(K key);

    List<T> loadAll();
}
