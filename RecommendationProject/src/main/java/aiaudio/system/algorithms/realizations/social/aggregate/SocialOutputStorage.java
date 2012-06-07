package aiaudio.system.algorithms.realizations.social.aggregate;

import aiaudio.system.algorithms.base.AbstractOutputStorage;
import aiaudio.system.util.MatrixDataLoader;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.SparseRowMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Dmitry
 */
public class SocialOutputStorage extends AbstractOutputStorage {

    private SparseRowMatrix weights;
    private SparseColumnMatrix ratings;

    public void initialize(int usersCount, int artistsCount) {
        weights = new SparseRowMatrix(usersCount, usersCount);
        ratings = new SparseColumnMatrix(artistsCount, usersCount);
    }

    @Override
    protected void saveToDisk() {
        MatrixDataLoader.columnMatrixDataLoader.writeToFile(ratings, directory + fileNameProperties.getSocialRatingsFileName());
    }

    void setWeight(int user1, int user2, double weight) {
        weights.setQuick(user1, user2, weight);
    }

    double getWeight(int user1, int user2) {
        double w1 = weights.getQuick(user1, user2);
        double w2 = weights.getQuick(user2, user1);
        return w1 == 0 ? w2 : w1;
    }

    public void saveTemporary(String fileName) {
        MatrixDataLoader.rowMatrixDataLoader.writeToFile(weights, fileName);
    }

    void setSocialCalculatedRatings(int userId, Vector summaryRatings) {
        ratings.assignColumn(userId, summaryRatings);
    }
}
