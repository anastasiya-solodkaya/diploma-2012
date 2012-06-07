package aiaudio.system.algorithms.realizations.postprocessing.analyse;

import org.apache.mahout.math.function.DoubleFunction;

/**
 *
 * @author Anastasiya
 */
class HalfLifeUtilityFunction implements DoubleFunction {
    private int j;
    private double alpha;

    public HalfLifeUtilityFunction(int j, double alpha) {
        this.j = j;
        this.alpha = alpha;
    }

    @Override
    public double apply(double arg1) {
        double v = ((double) j - 1) / (alpha - 1);
        return Math.max(arg1, 0) / Math.pow(2, v);
    }
    
}
