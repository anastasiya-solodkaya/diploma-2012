package aiaudio.system.algorithms.realizations.preprocessing.prepare;

import aiaudio.system.algorithms.base.AbstractOutputStorage;
import aiaudio.system.util.MatrixDataLoader;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class RatingPreparingOutputStorage extends AbstractOutputStorage {

    private SparseColumnMatrix matrix;

    void initialize(int artistsCount, int usersCount) {
        matrix = new SparseColumnMatrix(artistsCount, usersCount);
    }

    @Override
    protected void saveToDisk() {
        MatrixDataLoader.columnMatrixDataLoader.writeToFile(matrix, directory + fileNameProperties.getNormilizedRatingsName());
    }

    void setUserColumn(int userId, Vector newUserValue) {
        matrix.assignColumn(userId, newUserValue);
    }
}
