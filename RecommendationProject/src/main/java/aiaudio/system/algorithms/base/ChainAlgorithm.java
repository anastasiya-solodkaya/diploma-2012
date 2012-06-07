package aiaudio.system.algorithms.base;

import aiaudio.system.iface.FileNameProperties;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.common.TasteException;

/**
 *
 * @author Anastasiya
 */
public class ChainAlgorithm implements IAlgorithm {

    private String inputDirectiry;
    private String outputDirectiry;
    private FileNameProperties fileNameProperties;
    private boolean verbose;
    private List<IAlgorithm> algorithms = new Vector<IAlgorithm>();

    public boolean add(IAlgorithm e) {
        return algorithms.add(e);
    }

    @Override
    public void setOutputDirectiry(String outputDirectiry) {
        this.outputDirectiry = outputDirectiry;
    }

    @Override
    public void prepare(String inputDirectory, FileNameProperties fileNameProperties) throws IOException, TasteException {
        this.inputDirectiry = inputDirectory;
        this.fileNameProperties = fileNameProperties;
    }

    @Override
    public void save(String outputDirectory, FileNameProperties fileNameProperties) {
    }

    @Override
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public void start() {
        for (int i = 0; i < algorithms.size(); i++) {
            startAlgorithm(algorithms.get(i), i);
        }
    }

    private void startAlgorithm(IAlgorithm iAlgorithm, int i) {
        try {
            String dir = i == 0 ? inputDirectiry : outputDirectiry;
            iAlgorithm.prepare(dir, fileNameProperties);
            iAlgorithm.setVerbose(verbose);
            iAlgorithm.start();
            iAlgorithm.save(outputDirectiry, fileNameProperties);
        } catch (IOException ex) {
            Logger.getLogger(ChainAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TasteException ex) {
            Logger.getLogger(ChainAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
