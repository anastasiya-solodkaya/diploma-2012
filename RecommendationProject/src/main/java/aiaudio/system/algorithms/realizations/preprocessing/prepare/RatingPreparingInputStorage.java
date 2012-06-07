package aiaudio.system.algorithms.realizations.preprocessing.prepare;

import aiaudio.system.algorithms.base.UserArtistsContainedInputStorage;
import aiaudio.system.util.MatrixDataLoader;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.SparseRowMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class RatingPreparingInputStorage extends UserArtistsContainedInputStorage{

    private SparseColumnMatrix initialDataColumn;
    private SparseRowMatrix initialDataRow;
    private RandomAccessSparseVector defaultValues;
    
    @Override
    public int getArtistsCount() {
        return initialDataColumn.rowSize();
    }

    @Override
    public int getUsersCount() {
        return initialDataColumn.columnSize();
    }

    @Override
    public Vector getVectorByUser(int userId) {
        return initialDataColumn.viewColumn(userId);
    }

    @Override
    public Vector getVectorByArtist(int artistId) {
        return initialDataRow.viewRow(artistId);
    }

    @Override
    public double getValueByUserArtist(int userId, int artistId) {
        return initialDataColumn.getQuick(artistId, userId);
    }

    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        initialDataColumn = MatrixDataLoader.columnMatrixDataLoader.readFromFile(fileNameProperties.getRemappedFileName());
        initialDataRow = MatrixDataLoader.rowMatrixDataLoader.readFromFile(fileNameProperties.getRemappedFileName());
        defaultValues = new RandomAccessSparseVector(getUsersCount());
    }

    void setArtistRow(int artistId, Vector result) {
    initialDataColumn.assignRow(artistId, result);
    }

    void setDefaultValue(int userId, double defaultValue) {
        defaultValues.setQuick(userId, defaultValue);
    }

    double getDefaultValue(int userId) {
        return defaultValues.getQuick(userId);
    }
    
}
