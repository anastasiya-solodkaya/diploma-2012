package aiaudio.system.util;

import java.io.File;

/**
 *
 * @author Anastasiya
 */
public class FilesystemUtils {

    public static String addLastSlashIfNeeded(String path) {
        if(path == null || path.isEmpty()){
            return "";
        }
        if (!path.endsWith(File.separator)) {
            return path + File.separator;
        }
        return path;
    }
}
