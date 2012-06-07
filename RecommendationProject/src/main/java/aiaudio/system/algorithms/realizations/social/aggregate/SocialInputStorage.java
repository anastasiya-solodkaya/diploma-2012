package aiaudio.system.algorithms.realizations.social.aggregate;

import aiaudio.system.algorithms.base.UserArtistsContainedInputStorage;
import aiaudio.system.util.MatrixDataLoader;
import aiaudio.system.util.VectorLoader;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Dmitry
 */
public class SocialInputStorage extends UserArtistsContainedInputStorage{

    private SparseColumnMatrix normilizedRatings;
    private SparseColumnMatrix learningSetMask;
    private SparseColumnMatrix friendsMatrix;
    private DenseVector neuroGroups;
    
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
        Vector userRatings = normilizedRatings.viewColumn(userId).clone();
        Vector userMask = learningSetMask.viewColumn(userId);
        userRatings.times(userMask);
        return userRatings;
    }

    @Override
    public Vector getVectorByArtist(int artistId) {
        // Да, так и должно быть
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getValueByUserArtist(int userId, int artistId) {
        return normilizedRatings.getQuick(artistId, userId) * learningSetMask.getQuick(artistId, userId);
    }

    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        learningSetMask = MatrixDataLoader.columnMatrixDataLoader.readFromFile(directory + fileNameProperties.getLearningSetMaskName());
        normilizedRatings = MatrixDataLoader.columnMatrixDataLoader.readFromFile(directory + fileNameProperties.getNormilizedRatingsName(), getArtistsCount(), getUsersCount());
        friendsMatrix = MatrixDataLoader.columnMatrixDataLoader.readFromFile(directory + fileNameProperties.getFriendsFileName(), getUsersCount(), getUsersCount());
        neuroGroups = VectorLoader.denseVectorLoader.readFromFile(directory + fileNameProperties.getNetworkGroupsName());
    }

    int getUserGroup(int userId) {
        return (int) neuroGroups.getQuick(userId);
    }

    boolean isFriends(int user1, int user2) {
        return friendsMatrix.getQuick(user1, user2) != 0 || friendsMatrix.getQuick(user2, user1) != 0;
    }

}
