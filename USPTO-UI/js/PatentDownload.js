let PatentDownload = (function () {

    let $classTag = "#download-body ";

    let downloadFiles = function () {
        $($classTag + "#downloadFiles").on("click", function (e) {

            try {
                e.preventDefault();

                let $initialDate = Script.getValue("dataInicial");
                let $finalDate = Script.getValue("dataFinal");

                disableButoes(true);
                Script.validateDate(
                    $classTag + "#dataInicial",
                    $classTag + "#dataFinal",
                    true
                );


                //Dates
                if ($initialDate) {
                    $initialDate = (initialDate == null ? null : moment(
                            Script.parseDate($initialDate)).format("YYYY-MM-DD")
                    );
                }

                if ($finalDate) {
                    $finalDate = ($finalDate == null ? null : moment(
                            Script.parseDate($finalDate)).format("YYYY-MM-DD")
                    );
                }

                if (!$initialDate) {
                    $initialDate = "";
                }

                if (!$finalDate) {
                    $finalDate = "";
                }

                let $stringParameter = "/download?initialDate=" +
                    $initialDate +
                    "&finalDate=" +
                    $finalDate
                ;

                $.get(Script.getHostDefault() + $stringParameter);
                monitorDownload();
            } catch (error) {
                disableButoes(false);
                toastr.error(error.message)
            }
        });
    };

    let monitorDownload = function () {
        monitorCurrentDownload();
    };

    let prepareHtmlCurrentDownload = function ($nomeArquivo) {

        let $div = $("<div>");
        $div.addClass(" alert alert-info text-center");

        let $span = $("<span>");
        $span.addClass('pull-center');
        $span.attr('id', 'nomeArquivo');

        let $strong = $("<strong>");
        $strong.attr('id', $nomeArquivo);
        $strong.append($nomeArquivo);

        $span.append($strong);

        let $icone = $("<i>");
        $icone.addClass("fa fa-refresh fa-spin pull-right");
        $icone.attr("aria-hidden", "true");

        $div.append($span);
        $div.append($icone);

        $($classTag + "#current-download").html($div);
    };

    let monitorCurrentDownload = function () {
        let $intervalDownload = setInterval(function () {
            ajaxDownloadFiles($intervalDownload);
            listFiles($intervalDownload);
        }, 1000)

    };

    let disableButoes = function($enable){
        $($classTag + "#downloadFiles").attr("disabled", $enable);
        $($classTag + "#processFiles").attr("disabled", $enable);
    };

    let ajaxDownloadFiles = function($intervalDownload){

        $.ajax({
            type: "GET",
            url: Script.getHostDefault() + '/download/current',
            contentType: "application/json",
            success: function ($data, textStatus, xhr) {
                if ($data && $data.name && xhr.status == '200') {

                    $($classTag + "#processFiles").attr("disabled", true);
                    $($classTag + "#downloadFiles").attr("disabled", true);


                    let fileName = $data.name;

                    localStorage.setItem('nomeArquivo', fileName);
                    prepareHtmlCurrentDownload(fileName);

                    let nomeArquivo = localStorage.getItem('nomeArquivo');

                    if (nomeArquivo !== $data.name) {
                        toastr.success(nomeArquivo + " baixado com sucesso!");
                    }

                    return true;
                } else {
                    $($classTag + "#current-download").html('');

                    clearInterval($intervalDownload);

                    if ($($classTag + "#current-download").children().length == 0 &&
                        $($classTag + "#current-process").children().length == 0
                    ) {
                        $($classTag + "#downloadFiles").attr("disabled", false);
                        $($classTag + "#processFiles").attr("disabled", false);
                    }
                }
            }, error : function ($data) {
                if ($data.statusText == "error") {
                    clearInterval($intervalDownload);
                    disableButoes(false);
                    toastr.error(Messages.MG0008);
                }
            }
        });
    };

    let listFiles = function ($intervalDownload = null) {
        $.ajax({
            type: "GET",
            url: Script.getHostDefault() + '/download/files',
            success: function ($data) {
                $($classTag + "#section-files").html('');

                $($data).each(function () {
                    addFile($(this)[0])
                })
            },error : function ($data) {
                if ($data.statusText == "error") {
                    clearInterval($intervalDownload);
                    disableButoes(false);
                    toastr.error(Messages.MG0013);
                }
            }
        });
    };

    let addFile = function ($arquivo) {

        let $div = $("<div>");
        let $btn = $("<button>");
        let $i = $("<i>");
        let $small = $("<small>");

        let $strong = $("<strong>");

        if ($arquivo.processDate == null) {
            $div.addClass("alert");
            $div.addClass("alert-warning");
            $i.addClass("fa fa-square-o");
        } else {
            $div.addClass("alert");
            $div.addClass("alert-success");
            $i.addClass("fa fa-check-square");
        }

        $div.addClass("file-downloaded");
        $btn.addClass("btn btn-success btn-sm");

        $strong.append(" " + $arquivo.name);

        $small.append($strong);

        $btn.append($i);


        $div.append($btn);
        $div.append($small);

        $($classTag + "#section-files").append($div)
    };

    /**
     * Revelating methods
     *
     * @return object
     */
    return {
        init: function () {
            downloadFiles();
            listFiles();
            monitorDownload();
        },
        listFiles: function () {
            listFiles();
        }
    };
})();
