let PatentProcessor = (function () {

    let $classTag = "#download-body ";

    // Process the files
    let processFiles = function () {
        $($classTag + "#processFiles").on("click", function () {
            $.get(Script.getHostDefault() + "/etl");
            monitorEtl();
        });
    };

    // Monitor the ETL
    let monitorEtl = function () {

        let $intervalEtl = setInterval(function () {

            try {
                initProcess();

                $.ajax({
                    type: "GET",
                    url: Script.getHostDefault() + '/etl/status',
                    success: function ($data, textStatus, xhr) {
                        if (xhr.status == '200') {

                            let $statusEtl = $data.status;
                            let $totalSteps = $data.stepNumbers;
                            let $stepStatus = $data.stepsStatus;

                            if ($stepStatus.length == $totalSteps && $statusEtl == 'COMPLETED') {
                                finishProcess($intervalEtl);
                                toastr.success(Messages.MG0006);
                                return;
                            }

                            makeProgress(
                                Script.getCurrentPercent($stepStatus),
                                getTextCurrentProcess($stepStatus, $totalSteps)
                            );
                        } else {
                            console.log($data)
                        }
                    },
                    error: function ($data) {
                        if ($data.statusText == "error"){
                            setTimeout(function () {
                                finishProcess($intervalEtl);
                                toastr.error(Messages.MG0008);
                            }, 900);
                        }
                    }
                });
            } catch (error) {
                console.log(error);
                clearInterval($intervalEtl);
                toastr.error(error.message);
            }

        }, 1000);
    };

    // Init the process
    let initProcess = function(){

        $($classTag + "#processFiles").html('');

        let $icone = $("<i>");
        $icone.addClass("fa fa-refresh fa-spin ml-2");

        $icone.attr("aria-hidden", "true");

        let $span = $("<span>");
        $span.addClass('pull-center');
        $span.append(Messages.MG0005);

        $($classTag + "#processFiles").append($span);
        $($classTag + "#processFiles").append($icone);

        $($classTag + "#downloadFiles").attr('disabled', true);
        $($classTag + "#processFiles").attr('disabled', true);
    };

    let finishProcess = function($intervalEtl){
        clearInterval($intervalEtl);
        $($classTag + "#current-process-info").html('');
        $($classTag + "#current-process").html('');
        $($classTag + "#downloadFiles").attr("disabled", false);
        $($classTag + "#processFiles").attr("disabled", false);
        $($classTag + "#processFiles").html("Processar");
        PatentDownload.listFiles();
    };

    // Return the text of current step
    let getTextCurrentProcess = function($stepStatus, $totalSteps){

        let $stepName = getStepName($stepStatus);
        let $textStep = getTextStep($stepName);
        let $indexCurrentStep = (Script.getIndexOfSteps($stepStatus) + 1);
        let $currentStepNumber = $indexCurrentStep + "/" + $totalSteps;

        return ($currentStepNumber + " " + $textStep);
    };

    // Return the current step name
    let getStepName = function($stepStatus){

        let $index = Script.getIndexOfSteps($stepStatus);

        return $stepStatus[$index].stepName;
    };

    // Return the text of current step
    let getTextStep = function($stepName){

        let $textStep = "";

        switch ($stepName) {
            case "unzipStep":
                $textStep = Messages.MG0002;
                break;
            case "cleanStep":
                $textStep = Messages.MG0003;
                break;
            case "loadStep":
                $textStep = Messages.MG0004;
                break;
        }

        return $textStep;
    };

    // Mount the progress
    let makeProgress = function ($percent, $text) {

        let $div = $("<div>");
        $div.addClass("progress");

        let $divBar = $("<div>");
        $divBar.addClass("progress-bar");
        $divBar.addClass("progress-bar-animated");
        $divBar.addClass("progress-bar-striped");

        $divBar.attr("style","width:" + $percent + "%");
        $divBar.attr("aria-valuenow", $percent);
        $divBar.attr("aria-valuemin", 0);
        $divBar.attr("aria-valuemax", 100);
        $divBar.append($percent.toFixed(2) + "%");

        $div.append($divBar);

        $($classTag + "#current-process").html($div);

        let $divInformacoes = $("<div>");
        $divInformacoes.addClass("alert alert-info text-center");

        let $span = $("<span>");
        $span.append($text);

        $divInformacoes.append($span);
        $($classTag + "#current-process-info").html($divInformacoes);
    };

    /**
     * Revelating methods
     *
     * @return object
     */
    return {
        init: function () {
            processFiles();
        }
    };
})();