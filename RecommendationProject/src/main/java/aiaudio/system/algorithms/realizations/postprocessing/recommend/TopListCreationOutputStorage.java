package aiaudio.system.algorithms.realizations.postprocessing.recommend;

import aiaudio.system.algorithms.base.AbstractOutputStorage;
import aiaudio.system.util.MatrixDataLoader;
import org.apache.mahout.math.SparseColumnMatrix;

/**
 *
 * @author Anastasiya
 */
public class TopListCreationOutputStorage extends AbstractOutputStorage {

    private SparseColumnMatrix recommendedArtists;
    private SparseColumnMatrix recommendedRatings;

    @Override
    protected void saveToDisk() {
        MatrixDataLoader.columnMatrixDataLoader.writeToFile(recommendedArtists, directory + fileNameProperties.getTopListArtistsName());
        MatrixDataLoader.columnMatrixDataLoader.writeToFile(recommendedRatings, directory + fileNameProperties.getTopListRatingsName());
    }

    void initialize(int artistCount, int userCount) {
        recommendedArtists = new SparseColumnMatrix(artistCount, userCount);
        recommendedRatings = new SparseColumnMatrix(artistCount, userCount);
    }

    void setRecommendations(int userId, int place, int artistId, double artistRating) {
        recommendedArtists.setQuick(place, userId, artistId);
        recommendedRatings.setQuick(place, userId, artistRating);
    }
}
