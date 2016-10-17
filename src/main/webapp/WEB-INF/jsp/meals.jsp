<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>

    <h2><a href="${pageContext.request.contextPath}/"><fmt:message key="app.home"/></a></h2>
    <h3><fmt:message key="meals.title"/></h3>
    <form method="post" action="${pageContext.request.contextPath}/meals/filter">
        <dl>
            <dt><fmt:message key="meals.date_from"/></dt>
            <dd><input type="date" name="startDate" value="${startDate}"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="meals.date_to"/></dt>
            <dd><input type="date" name="endDate" value="${endDate}"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="meals.time_from"/></dt>
            <dd><input type="time" name="startTime" value="${startTime}"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="meals.time_to"/></dt>
            <dd><input type="time" name="endTime" value="${endTime}"></dd>
        </dl>
        <button type="submit"><fmt:message key="meals.filter"/></button>
    </form>
    <hr>
    <a href="${pageContext.request.contextPath}/meals?action=create"><fmt:message key="meals.create"/></a>
    <hr>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><fmt:message key="meals.date"/></th>
            <th><fmt:message key="meals.description"/></th>
            <th><fmt:message key="meals.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="${pageContext.request.contextPath}/meals?action=update&id=${meal.id}"><fmt:message key="meals.update"/></a></td>
                <td><a href="${pageContext.request.contextPath}/meals?action=delete&id=${meal.id}"><fmt:message key="meals.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>