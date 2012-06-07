package aiaudio.system.algorithms.base;

import aiaudio.system.iface.FileNameProperties;
import java.io.IOException;
import java.util.Calendar;
import org.apache.mahout.cf.taste.common.TasteException;

/**
 *
 * @author Anastasiya
 */
public abstract class Algorithm<I extends AbstractInputStorage, O extends AbstractOutputStorage> implements IAlgorithm {

    protected I inputStorage;
    protected O outputStorage;
    
    protected boolean verbose;

    @Override
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public Algorithm() {
    }
    
    protected void log(String message){
        if(verbose){
            String dt = Calendar.getInstance().getTime().toLocaleString();
            System.out.println(dt + " " + message);
        }
    }

    @Override
    public void prepare(String inputDirectory, FileNameProperties fileNameProperties) throws IOException, TasteException {
        inputStorage = createInputStorage();
        outputStorage = createOutputStorage();

        inputStorage.load(inputDirectory, fileNameProperties);
    }

    @Override
    public void save(String outputDirectory, FileNameProperties fileNameProperties) {
        outputStorage.save(outputDirectory, fileNameProperties);
    }

    @Override
    public void setOutputDirectiry(String outputDirectiry) {
        
    }

    protected abstract I createInputStorage();

    protected abstract O createOutputStorage();

}
