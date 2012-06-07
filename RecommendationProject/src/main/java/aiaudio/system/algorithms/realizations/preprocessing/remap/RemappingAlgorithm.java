package aiaudio.system.algorithms.realizations.preprocessing.remap;

import aiaudio.system.algorithms.base.Algorithm;

/**
 *
 * @author Anastasiya
 */
public class RemappingAlgorithm extends Algorithm<RemappingInputStorage, RemappingOutputStorage>{

    @Override
    public void start() {
        outputStorage.setMatrix(inputStorage.getMatrix());
        outputStorage.setDictionary(inputStorage.getDictionary());
    }

    @Override
    protected RemappingInputStorage createInputStorage() {
        return new RemappingInputStorage();
    }

    @Override
    protected RemappingOutputStorage createOutputStorage() {
        return new RemappingOutputStorage();
    }
    
}
