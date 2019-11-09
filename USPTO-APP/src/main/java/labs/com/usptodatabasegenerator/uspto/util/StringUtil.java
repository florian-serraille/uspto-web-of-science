package labs.com.usptodatabasegenerator.uspto.util;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    public static String removeWhiteSpaceAndReturnCarriage(String line){
        line = StringUtils.replaceChars(line, '\n', ' ');
        return RegExUtils.replaceAll(line, "[ ]{2,}", " ");
    }
}
