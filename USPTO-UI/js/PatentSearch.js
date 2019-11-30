let PatentSearch = (function () {

    let $classTag = "#search-body ";

    // return the query object
    let getQueryObject = function () {

        // Persons
        let personsOperator = Script.getValue('personsOperator');
        let personsType = Script.getValue('personsType');

        //Classfications
        let classificationOperator = Script.getValue("classificationOperator");
        let classificationType = Script.getValue("classificationType");

        //Expression
        let expressionOperator = Script.getValue("expressionOperator");
        let expressionType = Script.getValue("expressionType");

        //Dates
        let initialDate = Script.getValue("initialDate");
        initialDate = (initialDate == null ? null : Script.parseDate(initialDate).toISOString());

        let finaleDate = Script.getValue("finaleDate");
        finaleDate = (finaleDate == null ? null : Script.parseDate(finaleDate).toISOString());

        Script.validateDate(
            $classTag + "#initialDate",
            $classTag + "#finaleDate"
        );

        return JSON.stringify({
            "personOperator": personsOperator,
            "personsType": personsType,
            "persons": persons.tagNames,

            "classificationOperator": classificationOperator,
            "classificationType": classificationType,
            "classifications": classifications.tagNames,

            "contentOperator": expressionOperator,
            "contentType": expressionType,
            "claims": expressions.tagNames,

            "initialPublicationDate": initialDate,
            "finalPublicationDate": finaleDate
        });
    };

    // Enable the blockUI
    let enableBlockUI = function ($blockUI) {

        if (!$blockUI) {
            $.unblockUI();
            return;
        }

        $.blockUI({
            message: getMessageBlockUI()
        });
    };

    // Generate the span that contains the message of blockUI
    let getMessageBlockUI = function () {
        let $span = $("<span>");
        $span.text(Messages.MG0007);

        let $i = $("<i>");
        $i.addClass("fa fa-spinner fa-spin");
        $i.attr("aria-hidden", true);

        $span.append($i);

        return $span;
    };

    // Set on html, the count of the search
    let setCountSearch = function ($countSearch) {
        $("#countSearch").val($countSearch);

        let $isCountZero = ($countSearch == 0);

        $($classTag + "#exportPatents").attr("disabled", $isCountZero);
    };

    // Start the query listener
    let initQuery = function () {
        $($classTag).on("click", "#searchPatents", function () {

            try {
                let $queryObject = getQueryObject();

                enableBlockUI(true);
                $.ajax({
                    type: "POST",
                    url: Script.getHostDefault() + '/patent/search',
                    data: $queryObject,
                    contentType: "application/json",
                    success: function ($data) {
                        setCountSearch($data);
                        enableBlockUI(false);
                    },
                    error: function ($data) {
                        if (typeof $data.responseJSON !== 'undefined') {
                            toastr.error($data.responseJSON.message);
                        } else {
                            toastr.error(Messages.MG0008);
                        }
                        enableBlockUI(false);
                    }
                });
            } catch (error) {
                enableBlockUI(false);
                toastr.error(error.message);
            }
        });
    };

    // Clean the form
    let clearForm = function () {
        $($classTag).on("click", "#cleanForm", function () {
            PatentHome.iniciarConsulta();
        });
    };

    /**
     * Revelating methods
     *
     * @return object
     */
    return {
        init: function () {
            initQuery();
            clearForm();
        }
    };
})();
