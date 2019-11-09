package labs.com.usptodatabasegenerator.uspto.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class ZipUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ZipFile.class);


    public static boolean isValid(final File file) {
        try (ZipFile ignored = new ZipFile(file)) {
            return true;
        } catch (IOException e) {
            LOG.info(e.getMessage());
            return false;
        }
    }
}
