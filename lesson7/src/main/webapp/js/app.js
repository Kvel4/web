window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
};

ajax = function (data, success, type="POST", url="", dataType="json") {
    $.ajax({
        type: type,
        url: url,
        dataType: dataType,
        data: data,
        success: function (response) {
            if (response["redirect"]) {
                location.href = response["redirect"]
            }
            success(response);
        }
    });
};

