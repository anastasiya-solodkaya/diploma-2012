package aiaudio.system.algorithms.realizations.preprocessing.remap;

import aiaudio.system.algorithms.base.AbstractOutputStorage;
import aiaudio.system.util.MatrixDataLoader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.math.SparseColumnMatrix;

/**
 *
 * @author Anastasiya
 */
public class RemappingOutputStorage extends AbstractOutputStorage {

    private ArtistUserDictionary dictionary;
    private SparseColumnMatrix matrix;

    public void setDictionary(ArtistUserDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void setMatrix(SparseColumnMatrix matrix) {
        this.matrix = matrix;
    }

    
    @Override
    protected void saveToDisk() {
        try {
            String remapped = directory + fileNameProperties.getRemappedFileName();
            MatrixDataLoader.columnMatrixDataLoader.writeToFile(matrix, remapped);
            dictionary.write(directory + fileNameProperties.getUserMapName(), directory + fileNameProperties.getArtistMapName());
        } catch (IOException ex) {
            Logger.getLogger(RemappingOutputStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
