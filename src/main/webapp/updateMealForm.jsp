<%--
  Created by IntelliJ IDEA.
  User: Mike
  Date: 9/12/2016
  Time: 4:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update</title>
    <style>
        form {
            margin-top: 15px;
            width: auto;
        }
        label {
            margin-left: 7px;
        }
        input {
            margin-bottom: 5px;
        }
    </style>
</head>
<body bgcolor="#5f9ea0">
<div style="text-align: center;">
<label style="font-size: large; color: blue">Change meal properties</label>

<form action="meals" method="post" accept-charset="UTF-8">
    <table align="center" style="border: 2px solid; width: 650px; text-align:left" bgcolor="#f0fff0">
        <input type="hidden" name="id" value="${meal.id}">
        <tr>
            <td><label for="description">Description</label></td>
            <td><input id="description" type="text" name="description" value="${meal.description}"></td>
        </tr>
        <tr>
            <td><label for="calories">Calories</label></td>
            <td><input id="calories" type="text" name="calories" value="${(meal.calories==0)? '' : meal.calories}"></td>
        </tr>
        <tr>
            <td><label for="datetime">Datetime</label></td>
            <td><input id="datetime" type="datetime-local" name="date" value="${meal.dateTime}"></td>
        </tr>
    </table>
    <br>
    <input type="submit" value="Submit">
</form>
</div>
</body>
</html>
