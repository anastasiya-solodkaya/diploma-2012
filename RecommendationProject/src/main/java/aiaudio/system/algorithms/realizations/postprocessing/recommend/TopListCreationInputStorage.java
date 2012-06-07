package aiaudio.system.algorithms.realizations.postprocessing.recommend;

import aiaudio.system.algorithms.base.AbstractInputStorage;
import aiaudio.system.util.MatrixDataLoader;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.DoubleFunction;

/**
 *
 * @author Anastasiya
 */
public class TopListCreationInputStorage extends AbstractInputStorage {

    private final static DoubleFunction learningToTestConversionFunction = new LearToTestConversionFunction();
    private SparseColumnMatrix learningSet;
    private SparseColumnMatrix recalculatedRatings;

    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        learningSet = MatrixDataLoader.columnMatrixDataLoader.readFromFile(directory + fileNameProperties.getLearningSetMaskName());
        recalculatedRatings = MatrixDataLoader.columnMatrixDataLoader.readFromFile(directory + fileNameProperties.getRecalculatedRatingsName(), artistsCount(), usersCount());
    }

    int usersCount() {
        return learningSet.columnSize();
    }

    int artistsCount() {
        return learningSet.rowSize();
    }

    Vector getTestUserRatings(int userId) {
        Vector userRatings = recalculatedRatings.viewColumn(userId).clone();
        Vector row = learningSet.viewColumn(userId).clone();
        row.assign(learningToTestConversionFunction);
        return userRatings.times(row);
    }

    private static class LearToTestConversionFunction implements DoubleFunction {

        @Override
        public double apply(double arg1) {
            return 1 - arg1;
        }
    }
}
