package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Mike on 9/12/2016.
 */
public class StorageDAOImpl  implements StorageDAO<Meal> {
    private static AtomicInteger count = new AtomicInteger(0);
    private static final ConcurrentHashMap<Integer, Meal> meals = new ConcurrentHashMap<>();


    private StorageDAOImpl() {
    }

    private static final StorageDAOImpl INSTANCE = new StorageDAOImpl();

    public static StorageDAOImpl getInstance() {
        if (meals.isEmpty()) {
            INSTANCE.save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
            INSTANCE.save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
            INSTANCE.save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
            INSTANCE.save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
            INSTANCE.save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
            INSTANCE.save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        }

        return INSTANCE;
    }

    @Override
    public List<Meal> getAll() {
        return meals.values().stream().collect(Collectors.toList());
    }

    @Override
    public void save(Meal meal) {
        if(meal.isNew()) meal.setId(count.incrementAndGet());
        meals.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public void update(Meal meal) {
        meals.replace(meal.getId(), meal);
    }
}