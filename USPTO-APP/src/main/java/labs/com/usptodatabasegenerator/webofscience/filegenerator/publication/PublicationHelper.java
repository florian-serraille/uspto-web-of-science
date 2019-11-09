package labs.com.usptodatabasegenerator.webofscience.filegenerator.publication;


import labs.com.usptodatabasegenerator.webofscience.filegenerator.exception.PublicationException;
import labs.com.usptodatabasegenerator.webofscience.filegenerator.tag.Tag;
import labs.com.usptodatabasegenerator.webofscience.filegenerator.tag.Tags;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class PublicationHelper {
    
    public static String PublicationToString(final Publication publication) {

        List<Field> fields = getFields(publication);
        List<String> registers = getRegisters(publication, fields);
        addTrailer(registers);
        
         return String.join("\n", registers);
    }

    private static void addTrailer(List<String> registers) {
        registers.add(Tags.ER.toString());
        registers.add("\r");
    }

    private static List<Field> getFields(final Publication publication) {
        return Arrays.
                stream(publication.getClass().getDeclaredFields())
                .collect(Collectors.toList());
    }

    private static List<String> getRegisters(final Publication publication, final List<Field> fields) {

        List<String> publicationRegisters = new ArrayList<>();

        try {
            for (Field field : fields){

                Tag tag = getTag(field);
                List<String> values = getValues(publication, field);
                publicationRegisters.addAll(formatRegisters(tag, values));
            }
        } catch (IllegalAccessException e){
            throw new PublicationException(e.getMessage());
        }

        return publicationRegisters;
    }

    private static List<String> formatRegisters(final Tag tag, final List<String> values) {

        List<String> registers = new ArrayList<>();
        
        registers.add(formatWithTag(tag,
                    values.stream()
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse("")));
            
        
        registers.addAll( values
                .stream()
                .filter(Objects::nonNull)
                .skip(1)
                .map(PublicationHelper::formatWithoutTag)
                .collect(Collectors.toList()));

        return registers;
    }

    private static String formatWithoutTag(final String value) {
        return StringUtils.rightPad(" ", 3) + value;
    }

    private static String formatWithTag(final Tag tag, final String value) {
        return StringUtils.rightPad(tag.value().toString(), 3) + value;
    }

    private static List<String> getValues(final Publication publication, final Field field) throws IllegalAccessException {

        List<String> values;

        field.setAccessible(Boolean.TRUE);
        Class<?> type = field.getType();
        Object obj = Optional.ofNullable(field.get(publication))
//                .orElseThrow(() -> new PublicationException("No value present for " + field.getName()));
        .orElse("");


        if (type.equals(String.class)){
            values = Collections.singletonList((String) obj);
        } else if (type.equals(Integer.class)) {
            values = Collections.singletonList(((Integer) obj).toString());
        } else if (type.equals(LocalDate.class)){
            values = Collections.singletonList(((LocalDate) obj).toString());
        } else if (type.equals(Year.class)){
            values = Collections.singletonList(((Year) obj).toString());
        } else if (type.equals(List.class)){
            values = (List<String>) obj;
        } else {
            throw new PublicationException("Type " + type + " not supported");
        }

        return values;
    }
    
    private static Tag getTag(final Field field) {
        return Arrays
                .stream(field.getAnnotationsByType(Tag.class))
                .findFirst()
                .orElseThrow(() -> new PublicationException("Field " + field.getName() + " not mapped properly. No @Tag present"));
    }
}
