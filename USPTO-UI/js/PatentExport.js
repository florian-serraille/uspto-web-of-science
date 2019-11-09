let PatentExport = (function () {

    let $classTag = "#modalExportFiles ";

    let getExportObject = function () {

        let $fileNameModal = $($classTag + "#fileNameModal").val();
        let $personTypeModal = $($classTag + "#personTypeModal").val();
        let $contentModal = $($classTag + "#contentModal").val();
        let $referenceModal = $($classTag + "#referenceModal").val();

        return JSON.stringify({
            "fileName": $fileNameModal,
            "personType": $personTypeModal,
            "content": $contentModal,
            "reference": $referenceModal
        });
    };

    let initExport = function () {
        $($classTag + "#exportFile").on("click", function () {
            let $exportObject = getExportObject();
            generateFile($exportObject);
        });
    };

    let generateFile = function ($exportObject) {

        let $span = $("<span>");
        $span.text(Messages.MG0010);

        let $i = $("<i>");
        $i.addClass("fa fa-spinner fa-spin");
        $i.attr("aria-hidden", true);

        $span.append($i);

        $($classTag + "#exportFile").html($span);
        $($classTag + "#exportFile").attr("disabled", true);

        setTimeout(function () {
            $.ajax({
                type: "POST",
                url: Script.getHostDefault() + '/patent/generate',
                data: $exportObject,
                contentType: "application/json",
                success: function ($data, textStatus, xhr) {
                    $($classTag + "#progress-export").removeClass("d-none");
                    monitoringGenerator();
                },
                error: function ($data) {
                    toastr.error(Messages.MG0011);
                    $($classTag + "#exportFile").html("Exportar");
                    $($classTag + "#exportFile").attr("disabled", false);
                }
            });
        }, 1000);
    };

    let monitoringGenerator = function () {
        let $intervalMonitoring = setInterval(function () {
            $.ajax({
                type: "GET",
                url: Script.getHostDefault() + '/patent/status',
                success: function ($data, $textStatus, $xhr) {
                    if ($xhr.status == "200") {

                        let $statusEtl = $data.status;
                        let $totalSteps = $data.stepNumbers;
                        let $stepStatus = $data.stepsStatus;

                        if ($stepStatus.length == $totalSteps && $statusEtl == 'COMPLETED') {
                            toastr.success(Messages.MG0012);
                            makeProgress(parseFloat("100.00"));
                            clearInterval($intervalMonitoring);
                            downloadFile();
                            return;
                        }

                        let $percent = Script.getCurrentPercent($stepStatus);
                        makeProgress($percent);
                    } else {
                        toastr.error(Messages.MG0011);
                        clearInterval($intervalMonitoring);
                        $($classTag + "#exportFile").html("Exportar");
                        $($classTag + "#exportFile").attr("disabled", false);
                        downloadFile();
                    }
                },
                error: function ($data) {
                    if ($data.statusText != "error") {
                        downloadFile();
                        clearInterval($intervalMonitoring);
                        toastr.error($data);
                    }
                }
            })
        }, 300);
    };

    let downloadFile = function () {
        $($classTag + "#downloadFile").removeClass("d-none");
        $($classTag + "#exportFile").addClass("d-none");

        $($classTag + "#downloadFile").attr("href", Script.getHostDefault() + "/patent/export");
        $($classTag + "#exportFile").html("Exportar");
        $($classTag + "#exportFile").attr("disabled", false);

        $($classTag + "#downloadFile").on("click", function () {
            $($classTag + "#progress-export").addClass("d-none");
            $($classTag + "#downloadFile").addClass("d-none");
            $($classTag + "#exportFile").removeClass("d-none");
            makeProgress(0);
        });
    };

    let makeProgress = function ($percent) {

        let $div = $("#progress-export .progress .progress-bar");

        $div.attr("style", "width:" + $percent + "%");
        $div.attr("aria-valuenow", $percent);

        $div.html($percent.toFixed(2) + "%");
    };

    /**
     * Revelating methods
     *
     * @return object
     */
    return {
        init: function () {
            initExport();
        }
    };
})();
