let PatentHome = (function () {

    let listenerPartials = function () {
        $("#search").on("click", function (e) {

            e.preventDefault();
            $.ajax({
                type: "GET",
                url: 'partials/partial_consulta.html',
                success: function ($data) {
                    $("#search-body").html($data);
                    $("#download-body").removeClass("show active");
                    $("#search-body").addClass("show active");
                },
                error: function ($data) {
                    toastr.error($data.responseText)
                }
            });
        });

        $("#download").on("click", function (e) {
            e.preventDefault();
            $.ajax({
                type: "GET",
                url: 'partials/partial_download.html',
                success: function ($data) {
                    $("#download-body").html($data);
                    $("#search-body").removeClass("show active");
                    $("#download-body").addClass("show active");
                },
                error: function ($data) {
                    toastr.error($data.responseText)
                }
            });
        });
    };

    let iniciarConsulta = function () {
        $("#search").trigger("click")
    };

    /**
     * Revelating methods
     *
     * @return object
     */
    return {
        listenerPartials : listenerPartials,
        iniciarConsulta: iniciarConsulta,
    };
})();