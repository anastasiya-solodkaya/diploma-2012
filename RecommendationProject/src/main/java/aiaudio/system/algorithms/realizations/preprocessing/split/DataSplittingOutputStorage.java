package aiaudio.system.algorithms.realizations.preprocessing.split;

import aiaudio.system.algorithms.base.AbstractOutputStorage;
import aiaudio.system.util.MatrixDataLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class DataSplittingOutputStorage extends AbstractOutputStorage {

    public static final double IS_LEARNING = 1.0;
    private SparseColumnMatrix mask;

    @Override
    protected void saveToDisk() {
        String path = directory + fileNameProperties.getLearningSetMaskName();
        MatrixDataLoader.columnMatrixDataLoader.writeToFile(mask, path);
    }

    void setLearningRating(int artistId, int userId) {
        mask.setQuick(artistId, userId, IS_LEARNING);
    }

    List<Integer> getArtistsWithoutUsers() {
        List v = new ArrayList<Integer>();
        for (int row = 0; row < mask.rowSize(); row++) {
            Vector artistRow = mask.viewRow(row);
            if (artistRow.getLengthSquared() == 0) {
                v.add(row);
            }
        }
        return v;
    }

    void initialize(int artistsCount, int usersCount) {
        mask = new SparseColumnMatrix(artistsCount, usersCount);
    }
}
