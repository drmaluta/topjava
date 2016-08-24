package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> list = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
        list.forEach(meal->System.out.println(meal.toString()));


    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        List<UserMealWithExceed> list = new ArrayList<>();
        Map<LocalDate, Boolean> map = getDateExceed(mealList, caloriesPerDay);
        /*
        map.forEach((k,v)->{
            //System.out.println("Item : " + k + " Count : " + v);
            mealList.forEach(meal->{
                if (k.isEqual(meal.getDateTime().toLocalDate()) && TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)){
                    list.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), v));
                }
            });
        });*/

        for (UserMeal meal : mealList) {
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)){
                list.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), map.get(meal.getDateTime().toLocalDate())));
            }
        }

        return list;
    }

    private static Map<LocalDate, Boolean> getDateExceed(List<UserMeal> mealList, int caloriesPerDay){
        Map<LocalDate, Boolean> map = new HashMap<>();
        int sum = 0;
        boolean exceed;
        for (int i = 0; i < mealList.size() - 1; i++){
            sum += mealList.get(i).getCalories();
            if (!mealList.get(i).getDateTime().toLocalDate().isEqual(mealList.get(i + 1).getDateTime().toLocalDate())){
                exceed = sum > caloriesPerDay;
                map.put(mealList.get(i).getDateTime().toLocalDate(), exceed);
                sum = 0;
            }else if ((i + 1 == mealList.size() - 1) && mealList.get(i).getDateTime().toLocalDate().isEqual(mealList.get(i + 1).getDateTime().toLocalDate())){
                sum += mealList.get(i + 1).getCalories();
                exceed = sum > caloriesPerDay;
                map.put(mealList.get(i).getDateTime().toLocalDate(), exceed);
            }else if ((i + 1 == mealList.size() - 1) && !mealList.get(i - 1).getDateTime().toLocalDate().isEqual(mealList.get(i).getDateTime().toLocalDate())){
                sum += mealList.get(i + 1).getCalories();
                exceed = sum > caloriesPerDay;
                map.put(mealList.get(i + 1).getDateTime().toLocalDate(), exceed);
            }
        }
        return map;
    }
}
