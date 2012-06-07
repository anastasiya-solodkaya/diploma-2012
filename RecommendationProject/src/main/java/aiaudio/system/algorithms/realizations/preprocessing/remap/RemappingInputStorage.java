package aiaudio.system.algorithms.realizations.preprocessing.remap;

import aiaudio.system.algorithms.base.AbstractInputStorage;
import aiaudio.system.util.MatrixDataLoader;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.SparseColumnMatrix;

/**
 *
 * @author Anastasiya
 */
public class RemappingInputStorage extends AbstractInputStorage{

    private ArtistUserDictionary dictionary;
    private SparseColumnMatrix matrix;

    public ArtistUserDictionary getDictionary() {
        return dictionary;
    }

    public SparseColumnMatrix getMatrix() {
        return matrix;
    }

    public RemappingInputStorage() {
        dictionary = new ArtistUserDictionary();
    }
    
    
    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        String inputFile = directory + fileNameProperties.getInputFileName();
        matrix = MatrixDataLoader.columnMatrixDataLoader.readFromFile(inputFile, dictionary);
    }
    
}
