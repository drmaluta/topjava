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
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/meals")
public class MealController {
    private static final Logger LOG = LoggerFactory.getLogger(MealController.class);

    @Autowired
    private MealRestController mealController;

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model) {
        LOG.info("getAll");
        model.addAttribute("meals", mealController.getAll());
        return "meals";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=delete")
    public String delete(@RequestParam("id") Integer id) {
        LOG.info("Delete {}", id);
        mealController.delete(id);
        return "redirect:meals";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=update")
    public String update(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("meal", mealController.get(id));
        return "meal";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "meal";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public ModelAndView getBetween(HttpServletRequest request) {
        LocalDate startDate = TimeUtil.parseLocalDate(resetParam("startDate", request));
        LocalDate endDate = TimeUtil.parseLocalDate(resetParam("endDate", request));
        LocalTime startTime = TimeUtil.parseLocalTime(resetParam("startTime", request));
        LocalTime endTime = TimeUtil.parseLocalTime(resetParam("endTime", request));

        ModelAndView mv = new ModelAndView("meals");
        mv.addObject("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST, params = {"id!="})
    public String saveUpdated(@RequestParam("id") Integer id, @ModelAttribute("meal") @Valid Meal meal) {
        LOG.info("Update {}", meal);
        mealController.update(meal, id);
        return "redirect:meals";
    }

    @RequestMapping(method = RequestMethod.POST, params = "id=")
    public String saveCreated(@ModelAttribute("meal") @Valid Meal meal) {
        LOG.info("Create {}", meal);
        mealController.create(meal);
        return "redirect:meals";
    }


    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }
}