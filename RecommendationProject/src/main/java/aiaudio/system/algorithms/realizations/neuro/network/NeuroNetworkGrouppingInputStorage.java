package aiaudio.system.algorithms.realizations.neuro.network;

import aiaudio.system.algorithms.base.AbstractInputStorage;
import aiaudio.system.util.MatrixDataLoader;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class NeuroNetworkGrouppingInputStorage extends AbstractInputStorage {

    private SparseColumnMatrix reduceResultMatrixColumn;

    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        reduceResultMatrixColumn = MatrixDataLoader.columnMatrixDataLoader.readFromFile(directory + fileNameProperties.getReducingCentroidsName());
    }

    int usersCount() {
        return reduceResultMatrixColumn.columnSize();
    }

    int artistsCount() {
        return reduceResultMatrixColumn.rowSize();
    }

    Vector getArtistsByUserRatings(int i) {
        return reduceResultMatrixColumn.viewColumn(i);
    }
}
