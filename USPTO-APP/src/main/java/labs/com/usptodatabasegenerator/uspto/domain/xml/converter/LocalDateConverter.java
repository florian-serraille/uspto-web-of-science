package labs.com.usptodatabasegenerator.uspto.domain.xml.converter;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") final Class type) {
        return LocalDate.class == type;
    }

    @Override
    public Object fromString(final String str) {
        try {
            return LocalDate.parse(str, DateTimeFormatter.BASIC_ISO_DATE);
        } catch (final DateTimeParseException e) {
            throw  new RuntimeException("Cannot parse value as local date", e);
        }
    }
}
