package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.StorageDAO;
import ru.javawebinar.topjava.repository.StorageDAOImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Mike on 9/9/2016.
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private static StorageDAO storage;
    private static final String INSERT_OR_EDIT = "/updateMealForm.jsp";
    private static final String MEAL_LIST = "/mealList.jsp";

    @Override
    public void init() throws ServletException {
        storage = StorageDAOImpl.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("Forward to mealList");
        String forward="";
        String action = request.getParameter("action");

        if (action != null && action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            storage.delete(id);
            forward = MEAL_LIST;
            @SuppressWarnings("unchecked")
            List<MealWithExceed> mealsWithExceeded = MealsUtil.getFilteredWithExceeded(storage.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealsWithExceeded);
        }else if (action != null && action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = storage.get(id);
            request.setAttribute("meal", meal);
        }else if(action != null && action.equalsIgnoreCase("add")){
            forward = INSERT_OR_EDIT;
            Meal meal = new Meal(null, null, 0);
            request.setAttribute("meal", meal);
        }else{
            forward = MEAL_LIST;
            @SuppressWarnings("unchecked")
            List<MealWithExceed> mealsWithExceeded = MealsUtil.getFilteredWithExceeded(storage.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealsWithExceeded);
        }

        //request.setAttribute("meals", mealsWithExceeded);

        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String date = request.getParameter("date");
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        String idString = request.getParameter("id");

        if (idString.equals("0")) {
            Meal meal = new Meal(localDateTime, description, calories);
            storage.save(meal);
        } else {
            Meal meal = new Meal(localDateTime, description, calories);
            meal.setId(Integer.parseInt(idString));
            storage.update(meal);
        }

        response.sendRedirect("meals");

    }
}
