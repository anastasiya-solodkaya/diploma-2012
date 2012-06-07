package aiaudio.system.algorithms.realizations.neuro.reduce;

import aiaudio.system.algorithms.base.AbstractOutputStorage;
import aiaudio.system.util.MatrixDataLoader;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.SparseRowMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class DimensionReducingOutputStorage extends AbstractOutputStorage {

    private SparseRowMatrix reduceResultMatrix;

    @Override
    protected void saveToDisk() {
        MatrixDataLoader.rowMatrixDataLoader.writeToFile(reduceResultMatrix, directory + fileNameProperties.getReducingCentroidsName(), true);
    }

    Vector getCentroid(int dictionaryNumber, int dictionarySize, int centroidIndexInsideDictionary) {
        int index = dictionaryNumber * dictionarySize + centroidIndexInsideDictionary;
        return reduceResultMatrix.viewRow(index);

    }

    void setCentroidRatings(int dictionaryNumber, int dictionarySize, int centroidIndexInsideDictionary, RandomAccessSparseVector v) {
        reduceResultMatrix.assignRow(dictionaryNumber * dictionarySize + centroidIndexInsideDictionary, v);
    }

    void initialize(int dictionarySize, int dictionaryCount, int usersCount) {
        reduceResultMatrix = new SparseRowMatrix(dictionaryCount * dictionarySize, usersCount);
    }
}
