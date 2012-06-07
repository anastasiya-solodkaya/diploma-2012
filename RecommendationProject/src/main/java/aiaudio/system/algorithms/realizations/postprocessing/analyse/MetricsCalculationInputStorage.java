package aiaudio.system.algorithms.realizations.postprocessing.analyse;

import aiaudio.system.algorithms.base.AbstractInputStorage;
import aiaudio.system.algorithms.realizations.postprocessing.analyse.RelevanceFunction;
import aiaudio.system.util.MatrixDataLoader;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.math.SparseRowMatrix;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.Functions;

/**
 *
 * @author Anastasiya
 */
public class MetricsCalculationInputStorage extends AbstractInputStorage {
    private SparseRowMatrix recommendation;

    @Override
    protected void loadFromDisk() throws IOException, TasteException {
        recommendation = MatrixDataLoader.rowMatrixDataLoader.readFromFile(directory + fileNameProperties.getTopListRatingsName());
    }

    int placesCount() {
        return recommendation.rowSize();
    }

    int usersCount() {
        return recommendation.columnSize();
    }

    double aggregateSum(RelevanceFunction relevanceFunction) {
        return recommendation.aggregate(Functions.PLUS, relevanceFunction);

    }

    Vector getRatingsForUsersOnPlace(int place) {
        return recommendation.viewRow(place);
    }
}
