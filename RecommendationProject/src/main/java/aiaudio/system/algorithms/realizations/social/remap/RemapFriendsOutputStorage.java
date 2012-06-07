package aiaudio.system.algorithms.realizations.social.remap;

import aiaudio.system.algorithms.base.AbstractOutputStorage;
import aiaudio.system.util.MatrixDataLoader;
import org.apache.mahout.math.SparseColumnMatrix;

/**
 *
 * @author Dmitry
 */
public class RemapFriendsOutputStorage extends AbstractOutputStorage{

    private SparseColumnMatrix friendMatrix;
    
    @Override
    protected void saveToDisk() {
        MatrixDataLoader.columnMatrixDataLoader.writeToFile(friendMatrix, directory + fileNameProperties.getFriendsFileName());
    }

    void initialize(int mappedUsersCount) {
        friendMatrix = new SparseColumnMatrix(mappedUsersCount, mappedUsersCount);
    }

    void setFriends(int id1, int id2) {
        friendMatrix.setQuick(id1, id2, 1);
    }
    
}
