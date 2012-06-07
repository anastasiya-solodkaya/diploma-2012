package aiaudio.system.algorithms.realizations.postprocessing.recommend;

import aiaudio.system.algorithms.base.Algorithm;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.DoubleDoubleFunction;

/**
 *
 * @author Anastasiya
 */
public class TopListCreationAlgorithm extends Algorithm<TopListCreationInputStorage, TopListCreationOutputStorage> {

    private int userCount;
    private int artistCount;

    @Override
    public void start() {
        prepare();
        calculate();
    }

    private void calculate() {
        InverseNegativeness n = new InverseNegativeness();

        for (int userId = 0; userId < userCount; userId++) {
            log("processing " + userId + " users");
            processUser(userId, n);
        }
    }

    private void prepare() {
        userCount = inputStorage.usersCount();
        artistCount = inputStorage.artistsCount();

        outputStorage.initialize(artistCount, userCount);
    }

    private void processUser(int userId, InverseNegativeness n) {
        Vector usersRatings = inputStorage.getTestUserRatings(userId);
        int initialDefaultRatings = usersRatings.size() - usersRatings.getNumNondefaultElements();
        boolean findPositive = true;
        double minValue = usersRatings.minValue();
        for (int artistId = 0; artistId < artistCount; artistId++) {
            int maxValueIndex = usersRatings.maxValueIndex();
            double maxValue = usersRatings.maxValue();
            if (maxValue == 0) {
                artistId += initialDefaultRatings;
                usersRatings.assign(n, Math.abs(minValue));
                findPositive = false;
                continue;
            } else {
                if (!findPositive) {
                    maxValue = maxValue + minValue;
                }
                outputStorage.setRecommendations(userId, artistId, maxValueIndex, maxValue);
                usersRatings.setQuick(maxValueIndex, 0);
            }
        }
    }

    private class InverseNegativeness implements DoubleDoubleFunction {

        @Override
        public double apply(double arg1, double arg2) {
            if (arg1 == 0) {
                return 0;
            }

            return arg1 + arg2;
        }
    }

    @Override
    protected TopListCreationInputStorage createInputStorage() {
        return new TopListCreationInputStorage();
    }

    @Override
    protected TopListCreationOutputStorage createOutputStorage() {
        return new TopListCreationOutputStorage();
    }
}
