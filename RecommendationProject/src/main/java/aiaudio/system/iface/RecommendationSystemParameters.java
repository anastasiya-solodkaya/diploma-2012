package aiaudio.system.iface;

import aiaudio.system.algorithms.realizations.preprocessing.prepare.InitialData;
import aiaudio.system.algorithms.realizations.preprocessing.prepare.Normalizaton;
import aiaudio.system.iface.prop.*;
import aiaudio.system.util.ReadingUtils;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author Anastasiya
 */
public class RecommendationSystemParameters {

    // rating preparing
    private InitialDataProperty initialData = new InitialDataProperty("initial_data", InitialData.LISTEN_COUNT);
    private NormalizationProperty normalizaton = new NormalizationProperty("normalizaton", Normalizaton.AVERAGE);
    // split data options (learning set percentage
    private DoubleProperty learningSetPercent = new DoubleProperty("learning_set_percent", 0.7);
    // network options
    private IntegerProperty maxLearingCicles = new IntegerProperty("max_learning_cicles", 1000);
    private IntegerProperty userGroupCount = new IntegerProperty("user_groups_count", 500);
    private IntegerProperty trainingSetSize = new IntegerProperty("training_set_size", 10000);
    //recommendation
    private IntegerProperty topListLength = new IntegerProperty("top_list_length", 1000);
    //reducing
    private IntegerProperty testSetSize = new IntegerProperty("test_set_size", 5000);
    private IntegerProperty dictionarySize = new IntegerProperty("dictionary_count", 5);
    private IntegerProperty dictionaryCount = new IntegerProperty("dictionary_size", 1000);
    //analyzing
    private DoubleListProperty prThreaholds = new DoubleListProperty("recall_and_precision_threshold", new Vector<Double>());
    //social
    private DoubleProperty friendsWeight = new DoubleProperty("friends_weight", 0.3);
    private DoubleProperty neuroWeight = new DoubleProperty("neuro_weight", 0.5);
    private DoubleProperty similarityWeight = new DoubleProperty("similarity_weight", 0.5);
    private DoubleProperty similarityThreshold = new DoubleProperty("similarity_distance_threshold", 0.5);

    public int getDictionaryCount() {
        return dictionaryCount.getValue();
    }

    public int getDictionarySize() {
        return dictionarySize.getValue();
    }

    public InitialData getInitialData() {
        return initialData.getValue();
    }

    public double getLearningSetPercent() {
        return learningSetPercent.getValue();
    }

    public int getMaxLearingCicles() {
        return maxLearingCicles.getValue();
    }

    public Normalizaton getNormalizaton() {
        return normalizaton.getValue();
    }

    public List<Double> getPrThreaholds() {
        return prThreaholds.getValue();
    }

    public int getTestSetSize() {
        return testSetSize.getValue();
    }

    public int getTopListLength() {
        return topListLength.getValue();
    }

    public int getTrainingSetSize() {
        return trainingSetSize.getValue();
    }

    public int getUserGroupCount() {
        return userGroupCount.getValue();
    }

    public double getFriendsWeight() {
        return friendsWeight.getValue();
    }

    public double getNeuroWeight() {
        return neuroWeight.getValue();
    }

    public double getSimilarityWeight() {
        return similarityWeight.getValue();
    }

    public double getSimilarityThreshold() {
        return similarityThreshold.getValue();
    }

    public static RecommendationSystemParameters parse(String settingsPath) {
        Properties properties = ReadingUtils.readPropertiesFromFile(settingsPath);
        RecommendationSystemParameters parameters = new RecommendationSystemParameters();

        parameters.fill(properties);

        return parameters;
    }

    private void fill(Properties properties) {
        learningSetPercent.read(properties);
        testSetSize.read(properties);
        dictionarySize.read(properties);
        dictionaryCount.read(properties);
        maxLearingCicles.read(properties);
        userGroupCount.read(properties);
        trainingSetSize.read(properties);
        topListLength.read(properties);
        prThreaholds.read(properties);
        initialData.read(properties);
        normalizaton.read(properties);
        friendsWeight.read(properties);
        neuroWeight.read(properties);
        similarityWeight.read(properties);
        similarityThreshold.read(properties);
    }

    @Override
    public String toString() {
        return "RecommendationSystemParameters{"
                + "initialData=" + initialData
                + ", normalizaton=" + normalizaton
                + ", learningSetPercent=" + learningSetPercent
                + ", maxLearingCicles=" + maxLearingCicles
                + ", userGroupCount=" + userGroupCount
                + ", trainingSetSize=" + trainingSetSize
                + ", topListLength=" + topListLength
                + ", testSetSize=" + testSetSize
                + ", dictionarySize=" + dictionarySize
                + ", dictionaryCount=" + dictionaryCount
                + ", prThreaholds=" + prThreaholds
                + ", friendsWeight=" + friendsWeight
                + ", neuroWeight=" + neuroWeight
                + ", similarityWeight=" + similarityWeight
                + '}';
    }
}
