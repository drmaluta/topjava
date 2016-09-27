package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import static java.time.temporal.ChronoUnit.WEEKS;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {

    @Autowired
    protected MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testSave() {
        Meal newMeal = new Meal(LocalDateTime.now().withNano(0), "Ужин", 800);
        newMeal.setId(service.save(newMeal, ADMIN_ID).getId());
        MATCHER.assertCollectionEquals(Arrays.asList(newMeal, ADMIN_MEAL, ADMIN_MEAL_1), service.getAll(ADMIN_ID));
    }

    @Test
    public void testGetAll() {
        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN_MEAL, ADMIN_MEAL_1), service.getAll(ADMIN_ID));
    }

    @Test
    public void testDelete() {
        service.delete(ADMIN_MEAL_ID_1, ADMIN_ID);
        MATCHER.assertCollectionEquals(Collections.singletonList(ADMIN_MEAL), service.getAll(ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() {
        service.delete(10, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundWithUserIdDelete() {
        service.delete(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void testGet() {
        MATCHER.assertEquals(service.get(USER_MEAL_ID, USER_ID), USER_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundGet() {
        service.get(10, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundWithUserIdGet() {
        service.get(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void testGetFiltered() {
        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN_MEAL, ADMIN_MEAL_1),
                service.getBetweenDateTimes(LocalDateTime.of(LocalDate.of(2015, 6, 11), LocalTime.of(11, 0)), LocalDateTime.now(), ADMIN_ID));


    }

    @Test
    public void testUpdate() {
        Meal meal = new Meal(USER_MEAL);
        meal.setCalories(1000);
        service.update(meal, USER_ID);
        Meal mealUpdated = service.get(USER_MEAL_ID, USER_ID);
        MATCHER.assertEquals(meal, mealUpdated);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundUpdate() {
        Meal userMeal = new Meal(USER_MEAL);
        userMeal.setId(10);
        service.update(userMeal, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundWithUserIdUpdate() {
        Meal adminMeal = new Meal(ADMIN_MEAL);
        adminMeal.setCalories(1500);
        service.update(adminMeal, USER_ID);
    }
}