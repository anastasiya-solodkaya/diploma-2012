package aiaudio.system.algorithms.realizations.neuro.reduce;

import aiaudio.system.algorithms.base.UsersArtistsBasedAlgorithm;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class DimensionReducingAlgorithm extends UsersArtistsBasedAlgorithm<DimensionReducingInputStorage, DimensionReducingOutputStorage> {

    private int testSetSize;
    private int dictionarySize;
    private int dictionaryCount;

    public void setDictionaryCount(int dictionaryCount) {
        this.dictionaryCount = dictionaryCount;
    }

    public void setDictionarySize(int dictionarySize) {
        this.dictionarySize = dictionarySize;
    }

    public void setTestSetSize(int testSetSize) {
        this.testSetSize = testSetSize;
    }

    @Override
    public void start() {
        super.start();

        outputStorage.initialize(dictionarySize, dictionaryCount, usersCount);
        Integer[] testSet = getRandomIndices(artistsCount, testSetSize);
        prepareDictionaries(testSet);
        reduceDimension();

    }

    @Override
    protected DimensionReducingInputStorage createInputStorage() {
        return new DimensionReducingInputStorage();
    }

    @Override
    protected DimensionReducingOutputStorage createOutputStorage() {
        return new DimensionReducingOutputStorage();
    }

    private void prepareDictionaries(Integer[] testSet) {
        log("start preparing dictionary");
        int[][] dictionaries = new int[dictionaryCount][dictionarySize];
        for (int i = 0; i < dictionaryCount; i++) {
            dictionaries[i] = makeRandomProjection(testSet);
        }
        fillReduceDimensionMatrix(dictionaries);
        log("end preparing dictionary");
    }

    private void reduceDimension() {
        log("start reducing");
        for (int i = 0; i < artistsCount; i++) {
            Vector row = inputStorage.getVectorByArtist(i);
            for (int j = 0; j < dictionaryCount; j++) {
                processDictionary(j, row);
            }
            if (i % 1000 == 0) {
                log("dimension reducing: processed " + i + " items");
            }
        }
    }

    private void processDictionary(int dictionaryNumber, Vector row) {
        int bestRow = foundBestRowInDictionary(dictionaryNumber, row);
        Vector centroid = outputStorage.getCentroid(dictionaryNumber, dictionarySize, bestRow);
        centroid.plus(row);
    }

    private int foundBestRowInDictionary(int dictionaryNumber, Vector row) {
        int bestRow = 0;
        double bestDistance = Double.MAX_VALUE;
        for (int k = 0; k < dictionarySize; k++) {
            Vector singleRow = outputStorage.getCentroid(dictionaryNumber, dictionarySize, k);

            double calculatedDistance = Math.sqrt(row.getDistanceSquared(singleRow));
            if (calculatedDistance < bestDistance) {
                bestDistance = calculatedDistance;
                bestRow = k;
            }
        }
        return bestRow;
    }

    private void fillReduceDimensionMatrix(int[][] dictionaries) {
        for (int dictionaryNumber = 0; dictionaryNumber < dictionaryCount; dictionaryNumber++) {
            for (int centroidNumber = 0; centroidNumber < dictionarySize; centroidNumber++) {
                int vectorIndex = dictionaries[dictionaryNumber][centroidNumber];
                RandomAccessSparseVector v = new RandomAccessSparseVector(usersCount);
                Vector viewRow = inputStorage.getVectorByArtist(vectorIndex);
                v.assign(viewRow);
                outputStorage.setCentroidRatings(dictionaryNumber, dictionarySize, centroidNumber, v);
            }

        }
    }

    private Integer[] getRandomIndices(int rowSize, int resultSize) {
        Set<Integer> testSet = new HashSet<Integer>();
        Random r = new Random();
        int nextIndex;
        while (testSet.size() < resultSize) {
            nextIndex = (int) (r.nextDouble() * rowSize);
            if (!testSet.contains(nextIndex)) {
                testSet.add(nextIndex);
            }
        }
        return testSet.toArray(new Integer[0]);
    }

    private int[] makeRandomProjection(Integer[] testSet) {
        Integer[] indicesInSet = getRandomIndices(testSet.length, dictionarySize);

        int[] result = new int[dictionarySize];
        for (int i = 0; i < indicesInSet.length; i++) {
            result[i] = testSet[indicesInSet[i]];
        }
        return result;
    }
}
