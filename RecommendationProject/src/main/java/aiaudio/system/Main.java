package aiaudio.system;

import aiaudio.system.algorithms.base.IAlgorithm;
import aiaudio.system.iface.CommandLineArguments;
import aiaudio.system.iface.FileNameProperties;
import aiaudio.system.iface.RecommendationSystemParameters;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.common.TasteException;

/**
 *
 * @author Anastasiya
 */
public class Main {

    public static final String ALGORITHM_PROPERTIES_FILE_NAME = "algorithm.properties";
    public static final String FILENAMES_FILE_NAME = "filenames.properties";

    public static void main(String[] args) {
        try {
            CommandLineArguments commandLineArguments = CommandLineArguments.parse(args);
            FileNameProperties fileNameProperties = FileNameProperties.parse(FILENAMES_FILE_NAME);
            RecommendationSystemParameters algorithmParameters = RecommendationSystemParameters.parse(ALGORITHM_PROPERTIES_FILE_NAME);

            IAlgorithm algorithm = AlgorithmFactory.create(commandLineArguments, algorithmParameters);
            algorithm.setOutputDirectiry(commandLineArguments.getOutputDirectory());
            algorithm.setVerbose(commandLineArguments.getVerbose());
            algorithm.prepare(commandLineArguments.getInputDirectory(), fileNameProperties);
            algorithm.start();
            algorithm.save(commandLineArguments.getOutputDirectory(), fileNameProperties);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TasteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
