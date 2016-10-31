function makeEditable() {

    $('.delete').click(function () {
        deleteRow($(this).parents('tr').attr("id"));
    });


    $('#filter').submit(filter);

    $('#detailsForm').submit(function () {
        save();
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

function filter() {
    var form = $('#filter');
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: form.serialize(),
        success: function (data) {
            redraw(data);
            $('.delete').click(function () {
                deleteRow($(this).closest('tr').attr('id'));
            });
        }
    });
    return false;
}

function redraw(data) {
    datatableApi.clear().rows.add(data).draw();
    /*$.each(data, function (key, item) {
        datatableApi.row.add(item);
    });
    datatableApi.draw();*/
}

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
    var form = $('#filter').val();
    if (form != undefined) {
        filter();
    } else {
        $.get(ajaxUrl, function (data) {
            redraw(data);
        });
    }

    /*var startDate = $('#startDate').val();
    var endDate = $('#endDate').val();
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();

    $.get(ajaxUrl, {startDate : startDate, startTime : startTime, endDate : endDate, endTime : endTime}, function (data) {
        datatableApi.fnClearTable();
        $.each(data, function (key, item) {
            datatableApi.fnAddData(item);
        });
        datatableApi.fnDraw();
    });*/
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
