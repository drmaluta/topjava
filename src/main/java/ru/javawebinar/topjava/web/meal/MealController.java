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
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class MealController {
    private static final Logger LOG = LoggerFactory.getLogger(MealController.class);
    @Autowired
    MealService service;

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model) {
        int userId = AuthorizedUser.id();
        LOG.info("getAll for User {}", userId);
        List<MealWithExceed> meals = MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay());
        model.addAttribute("meals", meals);
        return "meals";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=delete")
    public String delete(@RequestParam("id") Integer id) {
        int userId = AuthorizedUser.id();
        LOG.info("delete meal {} for User {}", id, userId);
        service.delete(id, userId);
        return "redirect:meals";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=update")
    public String update(@RequestParam("id") Integer id, Model model) {
        int userId = AuthorizedUser.id();
        model.addAttribute("meal", service.get(id, userId));
        return "meal";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "meal";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public ModelAndView getBetween(HttpServletRequest request) {
        int userId = AuthorizedUser.id();

        LocalDate startDate = TimeUtil.parseLocalDate(resetParam("startDate", request));
        LocalDate endDate = TimeUtil.parseLocalDate(resetParam("endDate", request));
        LocalTime startTime = TimeUtil.parseLocalTime(resetParam("startTime", request));
        LocalTime endTime = TimeUtil.parseLocalTime(resetParam("endTime", request));
        LOG.info("getBetween dates {} - {} for time {} - {} for User {}", startDate, endDate, startTime, endTime, userId);

        List<MealWithExceed> meals = MealsUtil.getFilteredWithExceeded(
                service.getBetweenDates(startDate != null ? startDate : TimeUtil.MIN_DATE, endDate != null ? endDate : TimeUtil.MAX_DATE, userId),
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        );
        ModelAndView mv = new ModelAndView("meals");
        mv.addObject("meals", meals);
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST, params = {"id!="})
    public String saveUpdated(@ModelAttribute("meal") @Valid Meal meal) {
        int userId = AuthorizedUser.id();
        LOG.info("update {} for User {}", meal, userId);
        service.update(meal, userId);
        return "redirect:meals";
    }

    @RequestMapping(method = RequestMethod.POST, params = "id=")
    public String saveCreated(@ModelAttribute("meal") @Valid Meal meal) {
        meal.setId(null);
        int userId = AuthorizedUser.id();
        LOG.info("create {} for User {}", meal, userId);
        service.save(meal, userId);
        return "redirect:meals";
    }


    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }
}