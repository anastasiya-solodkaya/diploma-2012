package aiaudio.system.algorithms.realizations.social.remap;

import aiaudio.system.algorithms.base.AbstractInputStorage;
import aiaudio.system.util.MatrixDataLoader;
import aiaudio.system.util.VectorLoader;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.SparseColumnMatrix;

/**
 *
 * @author Dmitry
 */
public class RemapFriendsInputStorage extends AbstractInputStorage {

    private SparseColumnMatrix friendsInitialMatrix;
    private DenseVector mappedUsers;

    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        friendsInitialMatrix = MatrixDataLoader.columnMatrixDataLoader.readFromFile(directory + fileNameProperties.getFriendsFileName());
        mappedUsers = VectorLoader.denseVectorLoader.readFromFile(directory + fileNameProperties.getUserMapName());
    }

    int getFriendsMatrixUsersCount() {
        return friendsInitialMatrix.columnSize();
    }

    int getMappedUsersCount() {
        return mappedUsers.size();
    }

    boolean isFriends(int i, int j) {
        return friendsInitialMatrix.getQuick(j, i) != 0 || friendsInitialMatrix.getQuick(i, j) != 0;
    }

    int getUserId(int i) {
        return (int) mappedUsers.getQuick(i);
    }
}
