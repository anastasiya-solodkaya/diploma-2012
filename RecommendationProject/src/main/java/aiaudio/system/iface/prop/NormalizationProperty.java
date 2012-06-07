package aiaudio.system.iface.prop;

import aiaudio.system.algorithms.realizations.preprocessing.prepare.Normalizaton;

/**
 *
 * @author Anastasiya
 */
public class NormalizationProperty extends ObjectProperty<Normalizaton> {

    private static final String AVERAGE = "average";
    private static final String ZSCORE = "zscore";

    public NormalizationProperty(String name, Normalizaton defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Normalizaton parse(String propValue) {
        if (propValue != null && propValue.trim().equalsIgnoreCase(AVERAGE)) {
            return Normalizaton.AVERAGE;
        } else if (propValue != null && propValue.trim().equalsIgnoreCase(ZSCORE)) {
            return Normalizaton.ZSCORE;
        }
        return defaultValue;
    }
}
