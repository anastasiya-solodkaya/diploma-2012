package aiaudio.system.algorithms.realizations.postprocessing.analyse;

import org.apache.mahout.math.function.DoubleFunction;

/**
 *
 * @author Anastasiya
 */
public class RelevanceFunction implements DoubleFunction {
    private double relevanceBottomLimit;

    public RelevanceFunction(double relevanceBottomLimit) {
        this.relevanceBottomLimit = relevanceBottomLimit;
    }

    @Override
    public double apply(double arg1) {
        return arg1 >= relevanceBottomLimit ? 1 : 0;
    }
    
}
