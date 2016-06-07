package com.kpi.labs.timetable.dao;

import java.util.List;

public interface CRUD<T, K> {
    void create(T element);

    void update(T element);

    void delete(T element);

    T load(K key);

    List<T> loadAll();
}
