package labs.com.usptodatabasegenerator.uspto.batch.download;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
@StepScope
public class DownloadReader implements ItemReader<LocalDate> {

    private LocalDate initialDate;
    private LocalDate finalDate;

    public static Long totalItemNumber;
    
    public DownloadReader(@NonNull @Value("${uspto.pattern1.initial-date}") final String initialDatePattern1Constant,
                          @Nullable @Value("#{jobParameters['initialDate']}") final String initialDateParam,
                          @Nullable @Value("#{jobParameters['finalDate']}") final String finalDateParam) {

        setUpInitialeDate(initialDatePattern1Constant, initialDateParam);
        setUpFinalDate(finalDateParam);
        calculateItemNumber(initialDate, finalDate);
    }

    private void calculateItemNumber(LocalDate initialDate, LocalDate finalDate) {

        totalItemNumber = 0L;
        
        for (LocalDate date = initialDate; date.isBefore(finalDate); date = date.plusDays(1)){
            if (date.getDayOfWeek().equals(DayOfWeek.TUESDAY)){
                totalItemNumber++;
            }
        }
    }

    private void setUpFinalDate(String finalDateParam) {
        finalDate = (finalDateParam != null) ?
                LocalDate.parse(finalDateParam) :
                LocalDate.now();
    }

    private void setUpInitialeDate(String initialDatePattern1Constant, String initialDateParam) {
        initialDate = (initialDateParam != null) ?
                LocalDate.parse(initialDateParam) :
                LocalDate.parse(initialDatePattern1Constant);

        while (isNotTuesday()) {
            initialDate = initialDate.plusDays(1);
        }
    }

    private boolean isNotTuesday() {
        return !initialDate.getDayOfWeek().equals(DayOfWeek.TUESDAY);
    }

    @Override
    public LocalDate read() {

        while (initialDate.isBefore(finalDate) || initialDate.isEqual(finalDate)) {
            LocalDate dateToProcess = initialDate;
            initialDate = initialDate.plusWeeks(1);
            return dateToProcess;
        }

        return null;
    }
}
