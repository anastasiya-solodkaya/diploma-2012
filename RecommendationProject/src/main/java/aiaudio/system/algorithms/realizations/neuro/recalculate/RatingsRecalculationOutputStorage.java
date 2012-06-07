package aiaudio.system.algorithms.realizations.neuro.recalculate;

import aiaudio.system.util.MatrixDataLoader;
import aiaudio.system.algorithms.base.AbstractOutputStorage;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class RatingsRecalculationOutputStorage extends AbstractOutputStorage {

    private SparseColumnMatrix artistGroupRatings;
    private SparseColumnMatrix recalculatedUserRatings;

    @Override
    protected void saveToDisk() {
        MatrixDataLoader.columnMatrixDataLoader.writeToFile(artistGroupRatings, directory + fileNameProperties.getNetworkGroupsRatingsName());
        MatrixDataLoader.columnMatrixDataLoader.writeToFile(recalculatedUserRatings, directory + fileNameProperties.getRecalculatedRatingsName());
    }

    void initialize(int artistCount, int userCount, int groupCount) {
        artistGroupRatings = new SparseColumnMatrix(artistCount, groupCount);
        recalculatedUserRatings = new SparseColumnMatrix(artistCount, userCount);

    }

    Vector getGroupRatings(int groupByUser) {
        return artistGroupRatings.viewColumn(groupByUser);
    }

    void setRecalculatedRatings(int artistId, Vector groupRatings) {
        recalculatedUserRatings.assignColumn(artistId, groupRatings);
    }

    void setArtistGroupRating(int j, int i, double rating) {
        artistGroupRatings.setQuick(j, i, rating);

    }
}
