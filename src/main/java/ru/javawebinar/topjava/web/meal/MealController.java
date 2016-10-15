package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * Created by admin on 10/14/2016.
 */
@Controller
public class MealController {
    private static final Logger LOG = LoggerFactory.getLogger(MealController.class);
    @Autowired
    private MealService service;

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public String meals(Model model) {
        int userId = AuthorizedUser.id();
        LOG.info("getAll for User {}", userId);
        List<MealWithExceed> meals = MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay());
        model.addAttribute("meals", meals);
        return "meals";
    }

    @RequestMapping(value = "/meals/create", method = RequestMethod.GET)
    public String create(Model model){
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "", 1000);
        model.addAttribute("meal", meal);
        model.addAttribute("edit", false);
        return "meal";
    }

    @RequestMapping(value = "/meals/update", method = RequestMethod.GET)
    public String update(@RequestParam(value="id") int id, Model model){
        int userId = AuthorizedUser.id();
        Meal meal = service.get(id, userId);
        model.addAttribute("meal", meal);
        model.addAttribute("edit", true);
        return "meal";
    }

    @RequestMapping(value = "/meals/update", method = RequestMethod.POST)
    public String updateMeal(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int userId = AuthorizedUser.id();
        final Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        meal.setId(getId(request));
        service.update(meal,userId);
        return "redirect:/meals";

    }

    @RequestMapping(value = "/meals/create", method = RequestMethod.POST)
    public String save(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int userId = AuthorizedUser.id();
        final Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        meal.setId(null);
        service.save(meal,userId);
        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value="id") int id){
        int userId = AuthorizedUser.id();
        service.delete(id, userId);
        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.POST)
    public String getBetween (HttpServletRequest request, Model model) throws IOException{
        request.setCharacterEncoding("UTF-8");
        LocalDate startDate = TimeUtil.parseLocalDate(resetParam("startDate", request));
        LocalDate endDate = TimeUtil.parseLocalDate(resetParam("endDate", request));
        LocalTime startTime = TimeUtil.parseLocalTime(resetParam("startTime", request));
        LocalTime endTime = TimeUtil.parseLocalTime(resetParam("endTime", request));
        int userId = AuthorizedUser.id();
        List<MealWithExceed> meals = MealsUtil.getFilteredWithExceeded(
            service.getBetweenDates(startDate != null ? startDate : TimeUtil.MIN_DATE, endDate != null ? endDate : TimeUtil.MAX_DATE, userId),
            startTime != null ? startTime : LocalTime.MIN,
            endTime != null ? endTime : LocalTime.MAX,
            AuthorizedUser.getCaloriesPerDay()
        );
        model.addAttribute("meals", meals);
        return "meals";
    }

    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
