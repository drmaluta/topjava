<%--
  Created by IntelliJ IDEA.
  User: Mike
  Date: 9/9/2016
  Time: 8:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Meal List</title>
    <style>
        table {
            width: 400px;
        }
        table, td, th, tr {
            background-color: #ffebcd;
            border: 1px solid black;
            border-collapse: collapse;
            text-align: center;
        }
        .red {
            color: red;
        }
        .green {
            color: green;
        }
        .header {
            background-color: #bce8f1;
        }
    </style>
</head>
<body bgcolor="#5f9ea0">
<div style="text-align: center;">
    <h2><a href="index.html">Home</a></h2>
    <h2>User list</h2>
    <table align="center">
        <tr class="header">
            <th>ID</th>
            <th>Description</th>
            <th>Calories</th>
            <th>Date</th>
            <th colspan=4>Action</th>
        </tr>
        <c:forEach var="meal" items="${meals}">
            <tr class="${(meal.isExceed() == true)? 'red' : 'green'}">
                <c:set var="cleanedDateTime" value="${fn:replace(meal.dateTime, 'T', ' ')}"/>
                <td>${meal.id}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>${cleanedDateTime}</td>

                <td><a  class="btn" href="meals?action=edit&id=<c:out value="${meal.id}"/>"><input type="button" value="Edit"/></a></td>
                <td><a class="btn" href="meals?action=delete&id=<c:out value="${meal.id}"/>"><input type="button" value="Delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <a class="btn" href="meals?action=add"><input type="button" value="Add meal"/></a>
</div>
</body>
</html>
