package aiaudio.system.algorithms.realizations.neuro.network;

import aiaudio.system.algorithms.base.AbstractOutputStorage;
import aiaudio.system.algorithms.realizations.neuro.network.som.SelfOrganizedMap;
import aiaudio.system.util.MatrixDataLoader;
import aiaudio.system.util.VectorLoader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class NeuroNetworkGrouppingOutputStorage extends AbstractOutputStorage {

    private Vector userGroupsVector;
    private SparseColumnMatrix userGroupsMatrix;
    private SelfOrganizedMap network;

    public void loadFromFile(String path) throws IOException, TasteException {
        userGroupsVector = VectorLoader.denseVectorLoader.readFromFile(path);
        userGroupsMatrix = MatrixDataLoader.columnMatrixDataLoader.readFromFile(path);
    }

    public int groupCount() {
        return userGroupsMatrix.columnSize();
    }

    public int userCount() {
        return userGroupsMatrix.rowSize();
    }

    public Vector getUsersByGroup(int j) {
        return userGroupsMatrix.viewColumn(j).clone();
    }

    public int getGroupByUser(int j) {
        return (int) userGroupsVector.getQuick(j);
    }

    public void setGroupForUser(int i, int group) {
        userGroupsMatrix.setQuick(i, group, 1.0);
        userGroupsVector.setQuick(i, group);
    }

    @Override
    protected void saveToDisk() {
        try {
            MatrixDataLoader.columnMatrixDataLoader.writeToFile(userGroupsMatrix, directory + fileNameProperties.getNetworkGroupsName());
            network.save(directory + fileNameProperties.getTrainedNetworkName());
        } catch (IOException ex) {
            Logger.getLogger(NeuroNetworkGrouppingOutputStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setAndSaveTrainedSOM(SelfOrganizedMap network) throws IOException {
        this.network = network;
        network.save("network_snapshot.csv");
    }

    void initialize(int totalUsersCount, int groupCount) {
        userGroupsVector = new DenseVector(totalUsersCount);
        userGroupsMatrix = new SparseColumnMatrix(totalUsersCount, groupCount);
    }
}
