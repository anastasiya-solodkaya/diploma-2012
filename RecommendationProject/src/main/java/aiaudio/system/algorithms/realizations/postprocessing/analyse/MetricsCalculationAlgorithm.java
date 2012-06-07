package aiaudio.system.algorithms.realizations.postprocessing.analyse;

import aiaudio.system.algorithms.base.Algorithm;
import java.util.ArrayList;
import java.util.List;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.DoubleFunction;
import org.apache.mahout.math.function.Functions;

/**
 *
 * @author Anastasiya
 */
public class MetricsCalculationAlgorithm extends Algorithm<MetricsCalculationInputStorage, MetricsCalculationOutputStorage> {
    public static final double maxHalfLifeUtilityThreshold = 2.0;
    private double halfLifeUtilityStep = 0.05;
    private double minHalfLifeUtilityThreshold = 0.0;

    private ArrayList<RelevanceData> relevanceDatas = new ArrayList<RelevanceData>();

    public void setThresholds(List<Double> list) {
        for (Double threshold : list) {
            relevanceDatas.add(new RelevanceData(threshold));
        }
    }
    private int placesCount;
    private int usersCount;
    private double totalSelected;
    private double totalSelectedWithZeros;
    private double halfLifeUtilityBuffer;
    private int alpha = 10;

    @Override
    public void start() {
        prepare();
        for (int placeNum = 0; placeNum < placesCount; placeNum++) {
            processPlace(placeNum);


        }
    }

    private void processPlace(int placeNum) {
        log("process " + placeNum + " place");
        Vector places = inputStorage.getRatingsForUsersOnPlace(placeNum);
        totalSelected += places.getNumNondefaultElements();
        totalSelectedWithZeros += totalSelected + (places.size() - places.getNumNondefaultElements());
        calculatePrecisionRecall(places, placeNum);
        halfLifeUtilityBuffer += calculateHalfLifeUtility(places, placeNum + 1, alpha);
        calculateHalfLifeUtility(placeNum);
    }

    private void calculateHalfLifeUtility(int placeNum) {
        for (double idealRating = maxHalfLifeUtilityThreshold; idealRating >= minHalfLifeUtilityThreshold; idealRating -= halfLifeUtilityStep) {
            double halfLifeUtilityConstat = calculateHalfLifeUtilityConstant(idealRating, alpha, placeNum + 1);
            double result = 100 * halfLifeUtilityBuffer / (usersCount * halfLifeUtilityConstat);
            outputStorage.setHalfLifeUtility(result, placeNum, idealRating);
        }
    }

    private void calculatePrecisionRecall(Vector places, int placeNum) {
        for (RelevanceData relevanceData : relevanceDatas) {
            double relevanceInPlace = places.aggregate(Functions.PLUS, relevanceData.relevanceFunction);
            relevanceData.totalRelevantSelectedInList += relevanceInPlace;
            double ts = totalSelectedWithZeros;
            double precision = relevanceData.totalRelevantSelectedInList / ts;
            double recall = relevanceData.totalRelevantSelectedInList / relevanceData.relevantItemsCount;

            outputStorage.setPrecisionRecall(precision, recall, placeNum, relevanceData.threshold);
        }
    }

    private void prepare() {
        placesCount = inputStorage.placesCount();
        usersCount = inputStorage.usersCount();

        if (placesCount > 100) {
            placesCount = 100;
        }
        outputStorage.initialize(placesCount, relevanceDatas);
        outputStorage.initialize(placesCount, maxHalfLifeUtilityThreshold, minHalfLifeUtilityThreshold, halfLifeUtilityStep);

        for (RelevanceData relevanceData : relevanceDatas) {
            double val = inputStorage.aggregateSum(relevanceData.relevanceFunction);
            relevanceData.relevantItemsCount = val;
        }

        totalSelected = minHalfLifeUtilityThreshold;
        totalSelectedWithZeros = minHalfLifeUtilityThreshold;
        halfLifeUtilityBuffer = 0;
    }

    private double calculateHalfLifeUtilityConstant(double threshold, double totalCount, double viewed) {
        double d1 = 1 - Math.pow(2, viewed / (totalCount - 1));
        double d2 = 1 - Math.pow(2, (1 / (totalCount - 1)));
        return (0.5 + 0.5 * (d1 / d2)) * threshold;
    }

    private double calculateHalfLifeUtility(Vector places, int j, int alpha) {
        HalfLifeUtilityFunction f = new HalfLifeUtilityFunction(j, alpha);
        return places.aggregate(Functions.PLUS, f);
    }

    @Override
    protected MetricsCalculationInputStorage createInputStorage() {
        return new MetricsCalculationInputStorage();
    }

    @Override
    protected MetricsCalculationOutputStorage createOutputStorage() {
        return new MetricsCalculationOutputStorage();
    }
}
