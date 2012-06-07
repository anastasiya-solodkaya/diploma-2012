package aiaudio.system.algorithms.realizations.neuro.network.som;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.DoubleDoubleFunction;

/**
 *
 * @author Anastasiya
 */
public class SelfOrganizedMap {

    private int inputNeuronCount;
    private int outputNeuronCount;
    private double initialLearningRate;
    private double learningRateConst;
    private double initialEffectiveWidth;
    private double effectiveWidthConst;
    private DenseMatrix neuronMatrix;
    private long learningStep;

    public SelfOrganizedMap(int inputNeuronCount, int outputNeuronCount) {
        this.inputNeuronCount = inputNeuronCount;
        this.outputNeuronCount = outputNeuronCount;
        neuronMatrix = new DenseMatrix(outputNeuronCount, inputNeuronCount);
    }

    public void setEffectiveWidthConst(double effectiveWidthConst) {
        this.effectiveWidthConst = effectiveWidthConst;
    }

    public void setInitialEffectiveWidth(double initialEffectiveWidth) {
        this.initialEffectiveWidth = initialEffectiveWidth;
    }

    public void setInitialLearningRate(double initialLearningRate) {
        this.initialLearningRate = initialLearningRate;
    }

    public void setLearningRateConst(double learningRateConst) {
        this.learningRateConst = learningRateConst;
    }

    protected double calculateLearningRate(long time) {
        return initialLearningRate * Math.exp(-time / learningRateConst);
    }

    protected double calculateEffectiveWidth(long time) {
        return initialEffectiveWidth * Math.exp(-time / effectiveWidthConst);
    }

    protected double calculateNeighbourHoodFunction(long time, Vector winner, Vector neighbour) {
        double squareLateralDistance = calculateSquareLateralDistance(winner, neighbour);
        return Math.exp(-squareLateralDistance / (2 * calculateEffectiveWidth(time)));
    }

    private double calculateSquareLateralDistance(Vector winner, Vector neighbour) {
        return calculateVectorDistance(winner, neighbour);
    }

    private double calculateVectorDistance(Vector v1, Vector v2) {
        return v1.getDistanceSquared(v2);
    }

    private boolean correctWeights(int winner, Vector vector) {
        Vector winnerNeuronWeightes = neuronMatrix.viewRow(winner);
        double learningRate = calculateLearningRate(learningStep);
        boolean needToStop = false;
        double sumDistance = 0;
        int dC = 0;
        for (int i = 0; i < outputNeuronCount; i++) {
            Vector neuronWeightes = neuronMatrix.viewRow(i);
            double neighbourFunction = calculateNeighbourHoodFunction(learningStep, winnerNeuronWeightes, neuronWeightes);

            CorrectionFunction cf = new CorrectionFunction(learningRate, neighbourFunction);
            Vector oldVector = neuronWeightes.clone();
            neuronWeightes.assign(vector, cf);
            double distance = Math.sqrt(oldVector.getDistanceSquared(neuronWeightes));
            if (distance < 0.0001) {
                needToStop = true;
            } else {
                dC++;
                sumDistance += distance;
            }
        }
        System.out.println("average distance = " + (dC == 0 ? 0 : sumDistance / dC));
        return needToStop;
    }

    private int findWinner(Vector vector) {
        int winner = -1;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < outputNeuronCount; i++) {
            Vector neuronWeightes = neuronMatrix.viewRow(i);
            double distance = Math.sqrt(neuronWeightes.getDistanceSquared(vector));
            if (minDistance > distance) {
                minDistance = distance;
                winner = i;
            }
        }
        return winner;
    }

    public void startLearning(Vector[] trainingSet, int learningCount) throws CardinalityNotMatchException {
        initialize(trainingSet);
        learningStep = 0;
        for (int i = 0; i < learningCount; i++) {
            boolean stop = learn(trainingSet);
            if (stop) {
                break;
            }
        }
    }

    private boolean learn(Vector[] trainingSet) throws CardinalityNotMatchException {
        boolean stopLearning = true;
        for (int i = 0; i < trainingSet.length; i++) {
            Vector vector = trainingSet[i];
            boolean canStopLearning = recalculateNeurons(vector);
            if (!canStopLearning) {
                stopLearning = false;
            }
        }
        return stopLearning;
    }

    private void initialize(Vector[] trainingSet) {
        setInitialLearningRate(0.1);
        setLearningRateConst(1000);
        double maxValue = findMaxCoordinateIntegralValue(trainingSet) * 2;
        setInitialEffectiveWidth(maxValue);
        setEffectiveWidthConst(1000 / Math.log(initialEffectiveWidth));
        randomizeWeights(maxValue);
    }

    private boolean recalculateNeurons(Vector vector) throws CardinalityNotMatchException {
        learningStep++;
        if (vector.size() != inputNeuronCount) {
            throw new CardinalityNotMatchException(inputNeuronCount, vector.size());
        }
        int winner = findWinner(vector);
        return correctWeights(winner, vector);
    }

    private void randomizeWeights(double valueListWidth) {
        int i = 0;
        Random random = new Random();
        int intWidth = (int) Math.round(valueListWidth);
        while (i < outputNeuronCount) {
            Vector vector = createRandomVector(inputNeuronCount, random, intWidth);
            if (!matrixContainsSameVector(vector)) {
                neuronMatrix.assignRow(i, vector);
                i++;
            }
        }
    }

    private boolean matrixContainsSameVector(Vector vector) {
        for (int i = 0; i < outputNeuronCount; i++) {
            Vector neuronWeightes = neuronMatrix.viewRow(i);
            double distance = Math.sqrt(neuronWeightes.getDistanceSquared(vector));
            if (distance == 0) {
                return true;
            }
        }
        return false;
    }

    private Vector createRandomVector(int inputNeuronCount, Random random, int maxValue) {
        Vector v = new DenseVector(inputNeuronCount);
        for (int i = 0; i < inputNeuronCount; i++) {
            double nextDouble = random.nextGaussian() * maxValue;
            v.setQuick(i, nextDouble);
        }
        return v;
    }

    public void print() {
        for (int i = 0; i < outputNeuronCount; i++) {
            String asFormatString = neuronMatrix.viewRow(i).asFormatString();
            System.out.println(i + ": " + asFormatString);
        }
    }

    public void save(String fileName) throws IOException {
        BufferedWriter write = new BufferedWriter(new FileWriter(fileName));
        for (int i = 0; i < outputNeuronCount; i++) {
            Vector row = neuronMatrix.viewRow(i);
            int size = row.size();
            for (int j = 0; j < size; j++) {
                write.write(row.getQuick(j) + " ");
            }
            write.newLine();
        }
        write.flush();
    }

    public int calculateGroup(Vector vector) {
        return findWinner(vector);
    }

    private double findMaxCoordinateIntegralValue(Vector[] trainingSet) {
        double max = 0;
        boolean found = false;
        for (int i = 0; i < trainingSet.length; i++) {
            Vector vector = trainingSet[i];
            double quick = Math.abs(vector.getQuick(i));
            if (max < quick) {
                max = quick;
                found = true;
            }
        }
        max = Math.round(max);
        return found && max > 0 ? max : 1;
    }

    private static class CorrectionFunction implements DoubleDoubleFunction {

        private double learningRate;
        private double neighbourFunction;

        public CorrectionFunction(double learningRate, double neighbourFunction) {
            this.learningRate = learningRate;
            this.neighbourFunction = neighbourFunction;
        }

        @Override
        public double apply(double arg1, double arg2) {
            return arg1 - learningRate * neighbourFunction * (arg2 - arg1);
        }
    }
}
