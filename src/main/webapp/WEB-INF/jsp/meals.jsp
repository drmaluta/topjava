<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<link rel="stylesheet" href="webjars/datatables/1.10.12/css/jquery.dataTables.min.css">

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron">
    <div class="container">
        <div class="shadow">
            <section>
                <h3><fmt:message key="meals.title"/></h3>
                <form method="get" class="form-horizontal" id="filter">
                    <div class="form-group col-xs-12">
                        <label for="startDate" class="control-label col-xs-2"><fmt:message key="meals.startDate"/>:</label>
                        <div class="col-xs-2">
                            <input type="date" name="startDate" class="form-control" id="startDate" value="${startDate}">
                        </div>
                        <label for="endDate" class="control-label col-xs-2"><fmt:message key="meals.endDate"/>:</label>

                        <div class="col-xs-2">
                            <input type="date" name="endDate" class="form-control" id="endDate" value="${endDate}">
                        </div>
                    </div>
                    <div class="form-group col-xs-12">
                        <label for="endDate" class="control-label col-xs-2"><fmt:message key="meals.startTime"/>:</label>
                        <div class="col-xs-2">
                            <input type="time" name="startTime" class="form-control" id="startTime" value="${startTime}">
                        </div>
                        <label for="endDate" class="control-label col-xs-2"><fmt:message key="meals.endTime"/>:</label>
                        <div class="col-xs-2">
                            <input type="time" name="endTime" class="form-control" id="endTime" value="${endTime}">
                        </div>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-primary"><fmt:message key="meals.filter"/></button>
                    </div>
                </form>
                <hr>
                <div class="view-box">
                    <a onclick="add()" class="btn btn-info"><fmt:message key="meals.add"/></a>
                    <table class="table table-striped table-bordered" id="datatable">
                        <thead>
                        <tr>
                            <th><fmt:message key="meals.dateTime"/></th>
                            <th><fmt:message key="meals.description"/></th>
                            <th><fmt:message key="meals.calories"/></th>
                            <th></th>
                            <th></th>
                        </tr>
                        </thead>
                        <c:forEach items="${meals}" var="meal">
                            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
                            <tr  id="${meal.id}" class="${meal.exceed ? 'exceeded' : 'normal'}">
                                <td>
                                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                                        ${fn:formatDateTime(meal.dateTime)}
                                </td>
                                <td>${meal.description}</td>
                                <td>${meal.calories}</td>
                                <td><a class="btn btn-primary btn-xs edit"><fmt:message key="common.update"/></a></td>
                                <td><a class="btn btn-danger btn-xs delete"><fmt:message key="common.delete"/></a></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </section>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
<div class="modal fade" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"><fmt:message key="meals.edit"/></h2>
            </div>
            <div class="modal-body">
                <form method="post" class="form-horizontal" id="detailsForm">
                    <input type="text" hidden="hidden" id="id" name="id" value="${meal.id}">
                    <div class="form-group">
                        <label for="dateTime" class="control-label col-xs-4"><fmt:message key="meals.dateTime"/></label>
                        <div class="col-xs-6">
                            <input type="datetime-local" class="form-control" name="dateTime" placeholder="DateTime" id="dateTime">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description" class="control-label col-xs-4"><fmt:message key="meals.description"/></label>
                        <div class="col-xs-6">
                            <input type="text" name="description" class="form-control" placeholder="Description" id="description">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="calories" class="control-label col-xs-4"><fmt:message key="meals.calories"/></label>
                        <div class="col-xs-6">
                            <input type="number" name="calories" class="form-control" placeholder="Calories" id="calories">
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-offset-3 col-xs-9">
                            <button type="submit" class="btn btn-primary"><fmt:message key="common.save"/></button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="webjars/jquery/2.2.4/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="webjars/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="webjars/noty/2.3.8/js/noty/packaged/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="resources/js/datatablesUtil.js"></script>
<script type="text/javascript">
    var ajaxUrl = 'ajax/profile/meals/';
    var datatableApi;
    $(function () {
        datatableApi = $('#datatable').DataTable({
            "paging": false,
            "info": false,
            "createdRow": function ( row, data, dataIndex ) {
                $(row).addClass(data.exceed? "exceeded" : "normal");
                $(row).attr('id', data.id);
            },
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type) {
                        if (type == 'display') {
                            return date.replace('T', ' ').substr(0, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": '<td><a class="btn btn-primary btn-xs edit"><fmt:message
                                        key="common.update"/></a></td>',
                    "orderable": false
                },
                {
                    "orderable": false,
                    "render":function () {
                        return '<td><button class="btn btn-danger btn-xs delete"><fmt:message
                                        key="common.delete"/></button></td>'
                    }
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        });
        makeEditable();
    });
</script>
</html>