package aiaudio.system.algorithms.realizations.preprocessing.split;

import aiaudio.system.algorithms.base.UserArtistsContainedInputStorage;
import aiaudio.system.util.MatrixDataLoader;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class DataSplittingInputStorage extends UserArtistsContainedInputStorage {

    private SparseColumnMatrix remapped;

    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        String remappedFile = directory + fileNameProperties.getRemappedFileName();
        remapped = MatrixDataLoader.columnMatrixDataLoader.readFromFile(remappedFile);
    }

    @Override
    public int getArtistsCount() {
        return remapped.rowSize();
    }

    @Override
    public int getUsersCount() {
        return remapped.columnSize();
    }

    @Override
    public Vector getVectorByUser(int userId) {
        return remapped.viewColumn(userId);
    }

    @Override
    public Vector getVectorByArtist(int artistId) {
        return remapped.viewRow(artistId);
    }

    @Override
    public double getValueByUserArtist(int userId, int artistId) {
        return remapped.getQuick(artistId, userId);
    }
}
