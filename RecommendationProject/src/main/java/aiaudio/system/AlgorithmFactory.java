package aiaudio.system;

import aiaudio.system.algorithms.base.ChainAlgorithm;
import aiaudio.system.algorithms.base.IAlgorithm;
import aiaudio.system.iface.RecommendationSystemParameters;
import aiaudio.system.iface.CommandLineArguments;
import aiaudio.system.algorithms.realizations.postprocessing.analyse.MetricsCalculationAlgorithm;
import aiaudio.system.algorithms.realizations.neuro.network.NeuroNetworkGrouppingAlgorithm;
import aiaudio.system.algorithms.realizations.preprocessing.prepare.RatingPreparingAlgorithm;
import aiaudio.system.algorithms.realizations.neuro.recalculate.RatingsRecalculationAlgorithm;
import aiaudio.system.algorithms.realizations.postprocessing.recommend.TopListCreationAlgorithm;
import aiaudio.system.algorithms.realizations.neuro.reduce.DimensionReducingAlgorithm;
import aiaudio.system.algorithms.realizations.preprocessing.remap.RemappingAlgorithm;
import aiaudio.system.algorithms.realizations.preprocessing.split.DataSplittingAlgorithm;
import aiaudio.system.algorithms.realizations.social.aggregate.SocialAlgorithm;
import aiaudio.system.algorithms.realizations.social.remap.RemapFriendsAlgorithm;

/**
 *
 * @author Anastasiya
 */
public class AlgorithmFactory {

    private static IAlgorithm createSplitAlgorithm(RecommendationSystemParameters algorithmParameters) {
        DataSplittingAlgorithm da = new DataSplittingAlgorithm();
        da.setLearningSetPercent(algorithmParameters.getLearningSetPercent());
        return da;
    }

    private static IAlgorithm createPrepareRatingsAlgorithm(RecommendationSystemParameters algorithmParameters) {
        RatingPreparingAlgorithm alg = new RatingPreparingAlgorithm();
        alg.setInitialData(algorithmParameters.getInitialData());
        alg.setNormalizaton(algorithmParameters.getNormalizaton());
        return alg;
    }

    private static IAlgorithm createReduceData(RecommendationSystemParameters algorithmParameters) {
        DimensionReducingAlgorithm alg = new DimensionReducingAlgorithm();
        alg.setDictionaryCount(algorithmParameters.getDictionaryCount());
        alg.setDictionarySize(algorithmParameters.getDictionarySize());
        alg.setTestSetSize(algorithmParameters.getTestSetSize());
        return alg;
    }

    private static IAlgorithm createSOMAlgorithm(RecommendationSystemParameters algorithmParameters) {
        NeuroNetworkGrouppingAlgorithm alg = new NeuroNetworkGrouppingAlgorithm();
        alg.setGroupCount(algorithmParameters.getUserGroupCount());
        alg.setMaxLearningCicles(algorithmParameters.getMaxLearingCicles());
        alg.setTrainingSetSize(algorithmParameters.getTrainingSetSize());
        return alg;
    }

    private static IAlgorithm createRecalculateRatingsAlgorithm(RecommendationSystemParameters algorithmParameters) {
        RatingsRecalculationAlgorithm alg = new RatingsRecalculationAlgorithm();
        return alg;
    }

    private static IAlgorithm createTopListCreationAlgorithm(RecommendationSystemParameters algorithmParameters) {
        TopListCreationAlgorithm alg = new TopListCreationAlgorithm();
        return alg;
    }

    private static IAlgorithm createAnalyzingAlgorithm(RecommendationSystemParameters algorithmParameters) {
        MetricsCalculationAlgorithm alg = new MetricsCalculationAlgorithm();
        alg.setThresholds(algorithmParameters.getPrThreaholds());
        return alg;
    }

    private static IAlgorithm createSocialAlgorithm(RecommendationSystemParameters algorithmParameters) {
        SocialAlgorithm alg = new SocialAlgorithm();
        
        alg.setFriendsWeight(algorithmParameters.getFriendsWeight());
        alg.setNeuroWeight(algorithmParameters.getNeuroWeight());
        alg.setSimilarityWeight(algorithmParameters.getSimilarityWeight());
        alg.setSimilarityThreshold(algorithmParameters.getSimilarityThreshold());
        
        return alg;
    }

    private static IAlgorithm createFriendsMapAlgorithm(RecommendationSystemParameters algorithmParameters) {
        return new RemapFriendsAlgorithm();
    }

    private static IAlgorithm getAlgorithmByPhase(Phase phase, RecommendationSystemParameters algorithmParameters) {
        switch (phase) {
           case Map:
               return new RemappingAlgorithm();
           case SplitToSets:
               return createSplitAlgorithm(algorithmParameters);
           case ConvertToRatings:
               return createPrepareRatingsAlgorithm(algorithmParameters);
           case ReduceDimension:
               return createReduceData(algorithmParameters);
           case ProcessNetwork:
               return createSOMAlgorithm(algorithmParameters);
           case RecalculateRatings:
               return createRecalculateRatingsAlgorithm(algorithmParameters);
           case FillTopList:
               return createTopListCreationAlgorithm(algorithmParameters);
           case Analyze:
               return createAnalyzingAlgorithm(algorithmParameters);
           case Social:
               return createSocialAlgorithm(algorithmParameters);
           case MapFriends:
               return createFriendsMapAlgorithm(algorithmParameters);
           default:
               return createFullAlgorithm(algorithmParameters);
       }
    }

    private static IAlgorithm createFullAlgorithm(RecommendationSystemParameters algorithmParameters) {
        ChainAlgorithm alg = new ChainAlgorithm();
        Phase[] all = Phase.getOrderedPhases();
        for (int i = 0; i < all.length; i++) {
            Phase phase = all[i];
            IAlgorithm a = getAlgorithmByPhase(phase, algorithmParameters);
            alg.add(a);
        }
        return alg;
    }
    
    private AlgorithmFactory instance;

    public AlgorithmFactory getInstance() {
        if (instance == null) {
            instance = new AlgorithmFactory();
        }
        return instance;
    }

    private AlgorithmFactory() {
    }

    public static IAlgorithm create(CommandLineArguments arguments, RecommendationSystemParameters algorithmParameters) {
        Phase phase = arguments.getPhase();
        return getAlgorithmByPhase(phase, algorithmParameters);
    }
}
