package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

/**
 * gkislin
 * 02.10.2016
 */
@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    Meal findOne(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m, u FROM Meal m JOIN FETCH User u ON u.id = m.user.id AND m.user.id = :userId WHERE m.id = :id")
    Meal getWithUser(
            @Param("id") int mealId,
            @Param("userId") int userId
    );

    @Override
    @Transactional
    Meal save(Meal meal);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC")
    List<Meal> findAll(@Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.dateTime BETWEEN :startDate AND :endDate ORDER BY m.dateTime DESC")
    List<Meal> findBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") int userId);
}
