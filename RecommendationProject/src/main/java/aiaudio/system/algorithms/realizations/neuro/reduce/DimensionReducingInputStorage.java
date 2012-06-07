package aiaudio.system.algorithms.realizations.neuro.reduce;

import aiaudio.system.util.MatrixDataLoader;
import aiaudio.system.algorithms.base.UserArtistsContainedInputStorage;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.SparseRowMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class DimensionReducingInputStorage extends UserArtistsContainedInputStorage {

    private SparseRowMatrix normilizedRatings;
    private SparseRowMatrix learningSetMask;

    @Override
    public int getArtistsCount() {
        return learningSetMask.rowSize();
    }

    @Override
    public int getUsersCount() {
        return learningSetMask.columnSize();
    }

    @Override
    public Vector getVectorByUser(int userId) {
        throw new RuntimeException("Mathod not implemented");
    }

    @Override
    public Vector getVectorByArtist(int artistId) {
        Vector artistRatings = normilizedRatings.viewRow(artistId);
        return artistRatings.times(learningSetMask.viewRow(artistId));

    }

    @Override
    public double getValueByUserArtist(int userId, int artistId) {
        throw new RuntimeException("Mathod not implemented");
    }

    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        learningSetMask = MatrixDataLoader.rowMatrixDataLoader.readFromFile(directory + fileNameProperties.getLearningSetMaskName());
        normilizedRatings = MatrixDataLoader.rowMatrixDataLoader.readFromFile(directory + fileNameProperties.getNormilizedRatingsName(), getArtistsCount(), getUsersCount());
    }
}
