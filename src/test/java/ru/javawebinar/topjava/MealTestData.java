package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

/**
 * GKislin
 * 13.03.2015.
 */
public class MealTestData {

    public static final Integer ADMIN_MEAL_ID = 100009;
    public static final Integer ADMIN_MEAL_ID_1 = 100008;
    public static final Integer USER_MEAL_ID = 100002;
    public static final Meal ADMIN_MEAL = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2015, Month.JUNE, 11, 21, 0), "Админ ужин", 1500);
    public static final Meal ADMIN_MEAL_1 = new Meal(ADMIN_MEAL_ID_1, LocalDateTime.of(2015, Month.JUNE, 11, 14, 0), "Админ ланч", 510);
    public static final Meal USER_MEAL = new Meal(USER_MEAL_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher<>(
            (expected, actual) -> expected == actual || (Objects.equals(expected.toString(), actual.toString())));
}