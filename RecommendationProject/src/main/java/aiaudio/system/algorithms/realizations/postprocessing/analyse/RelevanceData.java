package aiaudio.system.algorithms.realizations.postprocessing.analyse;

/**
 *
 * @author Anastasiya
 */
class RelevanceData {
    double threshold;

    public double getThreshold() {
        return threshold;
    }
    double relevantItemsCount;
    double totalRelevantSelectedInList;
    RelevanceFunction relevanceFunction;
    private double halfLifeUtilityBuffer;

    public RelevanceData(double threshold) {
        this.threshold = threshold;
        relevanceFunction = new RelevanceFunction(threshold);
    }
    
}
