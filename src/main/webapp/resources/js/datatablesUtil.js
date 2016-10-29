function makeEditable() {

    $('.delete').click(function () {
        deleteRow($(this).parents('tr').attr("id"));
    });

    /*$('.deleteMeal').click(function () {
        deleteMealRow($(this).parents('tr').attr("id"));
    });*/

    $('#detailsForm').submit(function () {
        save();
        return false;
    });

    $('#filter').submit(function () {
        updateTable();
        return false;
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(event, jqXHR, options, jsExc);
    });
}

function add() {
    $('#id').val(null);
    $('#editRow').modal();
}

/*function deleteMeal(id) {
    deleteMealRow(id);
}*/

/*function deleteMealRow(id) {
    $.ajax({
        url: ajaxUrl + id,
        type: 'DELETE',
        success: function () {
            updateTableWithFilter();
            successNoty('Deleted');
        }
    });
}*/

function deleteRow(id) {
    $.ajax({
        url: ajaxUrl + id,
        type: 'DELETE',
        success: function () {
            updateTable();
            successNoty('Deleted');
        }
    });
}

/*function updateTableWithFilter() {
    /!*$.ajax({
        type: "POST",
        url: ajaxUrl + 'filter',
        data: $('#filter').serialize(),
        success: updateTableByData
    });
    return false;*!/
    var startDate = $('#startDate').val();
    var endDate = $('#endDate').val();
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();
    $.get(ajaxUrl + "filter?startDate="+startDate+"&startTime="+startTime+"&endDate="+endDate+"&endTime="+endTime, function (data) {
        datatableApi.fnClearTable();
        $.each(data, function (key, item) {
            datatableApi.fnAddData(item);
        });
        datatableApi.fnDraw();
        successNoty('Filtered');
    });
}*/

/*function updateTableByData(data) {
    datatableApi.clear().rows.add(data).draw();
}*/

function enable(chkbox, id) {
    var enabled = chkbox.is(":checked");
    chkbox.closest('tr').css("text-decoration", enabled ? "none" : "line-through");
    $.ajax({
        url: ajaxUrl + id,
        type: 'POST',
        data: 'enabled=' + enabled,
        success: function () {
            successNoty(enabled ? 'Enabled' : 'Disabled');
        }
    });
}

function updateTable() {
    var startDate = $('#startDate').val();
    var endDate = $('#endDate').val();
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();

    $.get(ajaxUrl, {startDate : startDate, startTime : startTime, endDate : endDate, endTime : endTime}, function (data) {
        datatableApi.fnClearTable();
        $.each(data, function (key, item) {
            datatableApi.fnAddData(item);
        });
        datatableApi.fnDraw();
    });
}

function save() {
    var form = $('#detailsForm');
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize(),
        success: function () {
            $('#editRow').modal('hide');
            updateTable();
            successNoty('Saved');
        }
    });
}

var failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    noty({
        text: text,
        type: 'success',
        layout: 'bottomRight',
        timeout: true
    });
}

function failNoty(event, jqXHR, options, jsExc) {
    closeNoty();
    failedNote = noty({
        text: 'Failed: ' + jqXHR.statusText + "<br>",
        type: 'error',
        layout: 'bottomRight'
    });
}
