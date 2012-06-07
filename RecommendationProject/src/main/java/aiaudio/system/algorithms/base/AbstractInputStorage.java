package aiaudio.system.algorithms.base;

import aiaudio.system.util.FilesystemUtils;
import aiaudio.system.iface.FileNameProperties;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;

/**
 *
 * @author Anastasiya
 */
public abstract class AbstractInputStorage extends AbstractStorage {

    public void load(String directory, FileNameProperties fileNameProperties) throws IOException, TasteException{
        this.directory = FilesystemUtils.addLastSlashIfNeeded(directory);
        this.fileNameProperties = fileNameProperties;
        loadFromDisk();
    }

    protected abstract void loadFromDisk()  throws IOException, TasteException;
}
