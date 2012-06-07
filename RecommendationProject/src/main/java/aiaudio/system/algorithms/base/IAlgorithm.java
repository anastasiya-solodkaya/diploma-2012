/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aiaudio.system.algorithms.base;

import aiaudio.system.iface.FileNameProperties;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;

/**
 *
 * @author nastya
 */
public interface IAlgorithm {

    void prepare(String inputDirectory, FileNameProperties fileNameProperties) throws IOException, TasteException;

    void save(String outputDirectory, FileNameProperties fileNameProperties);

    void setVerbose(boolean verbose);

    void start();

    void setOutputDirectiry(String outputDirectiry);
    
}
