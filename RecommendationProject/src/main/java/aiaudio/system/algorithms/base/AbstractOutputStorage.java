package aiaudio.system.algorithms.base;

import aiaudio.system.util.FilesystemUtils;
import aiaudio.system.iface.FileNameProperties;

/**
 *
 * @author Anastasiya
 */
public abstract class AbstractOutputStorage extends AbstractStorage {

    public void save(String directory, FileNameProperties fileNameProperties) {
        this.directory = FilesystemUtils.addLastSlashIfNeeded(directory);
        this.fileNameProperties = fileNameProperties;
        saveToDisk();
    }

    protected abstract void saveToDisk();
}
