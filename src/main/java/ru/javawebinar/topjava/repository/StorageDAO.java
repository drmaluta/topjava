package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by Mike on 9/12/2016.
 */
public interface StorageDAO<T> {
    List<Meal> getAll();

    void save(Meal meal);

    void delete(int id);

    Meal get(int id);

    void update(Meal meal);
}