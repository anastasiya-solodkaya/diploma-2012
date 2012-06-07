package aiaudio.system.algorithms.realizations.neuro.recalculate;

import aiaudio.system.util.MatrixDataLoader;
import aiaudio.system.util.VectorLoader;
import aiaudio.system.algorithms.base.AbstractInputStorage;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.SparseRowMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class RatingsRecalculationInputStorage extends AbstractInputStorage {

    private SparseRowMatrix normilizedRatings;
    private SparseRowMatrix learningSetMask;
    private Vector userGroupsVector;
    private SparseColumnMatrix userGroupsMatrix;

    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        userGroupsVector = VectorLoader.denseVectorLoader.readFromFile(directory + fileNameProperties.getNetworkGroupsName());
        userGroupsMatrix = MatrixDataLoader.columnMatrixDataLoader.readFromFile(directory + fileNameProperties.getNetworkGroupsName());
        learningSetMask = MatrixDataLoader.rowMatrixDataLoader.readFromFile(directory + fileNameProperties.getLearningSetMaskName());
        normilizedRatings = MatrixDataLoader.rowMatrixDataLoader.readFromFile(directory + fileNameProperties.getNormilizedRatingsName(), learningSetMask.rowSize(), learningSetMask.columnSize());
    }

    int groupCount() {
        return userGroupsMatrix.columnSize();
    }

    int learningArtistsCount() {
        return learningSetMask.rowSize();
    }

    int userCount() {
        return learningSetMask.columnSize();
    }

    int getGroupByUser(int j) {
        return (int) userGroupsVector.get(j);
    }

    Vector getUsersByGroup(int i) {
        return userGroupsMatrix.viewColumn(i).clone();
    }

    Vector getArtistsLearningRatings(int j) {
        Vector artistRatings = normilizedRatings.viewRow(j);
        return artistRatings.times(learningSetMask.viewRow(j));
    }
}
