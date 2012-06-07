package aiaudio.system.algorithms.realizations.neuro.network.som;

/**
 *
 * @author Anastasiya
 */
public class CardinalityNotMatchException extends RuntimeException{
    private int inputNetworkCardinality;
    private int inputVectorCardinality;

    public CardinalityNotMatchException(int inputNetworkCardinality, int inputVectorCardinality) {
        super("Cardinality no matches: expected " + inputNetworkCardinality + " but found " + inputVectorCardinality);
        this.inputNetworkCardinality = inputNetworkCardinality;
        this.inputVectorCardinality = inputVectorCardinality;
    }
}
