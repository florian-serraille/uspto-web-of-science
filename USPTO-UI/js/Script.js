let Script;

(function () {

    // Retorna o endponint base
    let getHostDefault = function () {
        return "http://localhost:8080/uspto-generator";
    };

    // Inicia o datePicker na tela que foi carregada
    let initDatePicker = function () {
        $(".datepicker").datepicker({
            language: 'pt-BR'
        })
    };

    // Recebe uma data no formato DD/MM/YYYY e retorna um objeto Date()
    let parseDate = function($dateString){
        if (typeof $dateString !== "undefined") {
            let $date = $dateString.split("/");
            return  new Date($date[2], $date[1] - 1, $date[0]);
        }
    };

    // Valida a data
    let validateDate = function ($selectorInitialDate, $selectorFinaleDate, $allowEmpty = true) {

        let $dataInicial = $($selectorInitialDate).val();
        let $dataFinal = $($selectorFinaleDate).val();

        if ($dataInicial == "" || $dataFinal == "") {
            if ($allowEmpty) {
                return ;
            } else {
                throw new Error(Messages.MG0009);
            }
        }

        $dataInicial = Script.parseDate($dataInicial);
        $dataFinal = Script.parseDate($dataFinal);

        if ($dataFinal.getTime() < $dataInicial.getTime()) {
            $("#dataInicial").addClass('is-invalid');
            $("#dataFinal").addClass('is-invalid');

            throw new Error(Messages.MG0001);
        }

        $($selectorInitialDate).removeClass('is-invalid');
        $($selectorFinaleDate).removeClass('is-invalid');

        return true;
    };

    let getValue = function ($fieldName) {
        let $value = $("#" + $fieldName).val();

        return $value ? $value : null;
    };

    // Return the current percentual of the process
    let getCurrentPercent = function($stepStatus){

        // Index of the current step
        let $index = Script.getIndexOfSteps($stepStatus);

        let $currentItem = $stepStatus[$index].currentItem;
        let $itemsTotalNumber = $stepStatus[$index].itemsTotalNumber;

        //Job percentual
        return ((100 * $currentItem)/$itemsTotalNumber);
    };

    //Return the index of the current step in steps array
    let getIndexOfSteps = function($stepStatus){

        let $index = 0;

        switch ($stepStatus.length) {
            case 1:
                $index = 0;
                break;
            case 2:
                $index = 1;
                break;
            case 3:
                $index = 2;
                break;
        }

        return $index;
    };

    /**
     * Revelating methods
     *
     * @return object
     */
    Script = {
        getHostDefault: getHostDefault,
        initDatePicker: initDatePicker,
        parseDate: parseDate,
        validateDate: validateDate,
        getValue: getValue,
        getCurrentPercent: getCurrentPercent,
        getIndexOfSteps: getIndexOfSteps,
    };
})();