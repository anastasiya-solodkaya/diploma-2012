package aiaudio.system.util;

import java.io.*;
import java.util.Properties;

/**
 *
 * @author Anastasiya
 */
public class ReadingUtils {

    public static Properties readPropertiesFromFile(String fileName) {

        Properties properties = new Properties();

        InputStream inStream;

        try {
            inStream = new FileInputStream(new File(fileName));
            properties.load(inStream);
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("FILE NOT FOUND EXCEPTION WHILE READING THE FILE: " + fileName + "\n" + fileNotFoundException);
        } catch (IOException ioException) {
            System.err.println("IOEXCETION WHILE READING THE FILE: " + fileName + "\n" + ioException);
        }

        return properties;

    }
}
