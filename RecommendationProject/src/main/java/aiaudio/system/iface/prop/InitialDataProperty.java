package aiaudio.system.iface.prop;

import aiaudio.system.algorithms.realizations.preprocessing.prepare.InitialData;

/**
 *
 * @author Anastasiya
 */
public class InitialDataProperty extends ObjectProperty<InitialData> {

    private static final String INVERSED = "inversed";
    private static final String LISTEN_COUNT = "listen_count";

    public InitialDataProperty(String name, InitialData defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected InitialData parse(String propValue) {
        if (propValue != null && propValue.trim().equalsIgnoreCase(LISTEN_COUNT)) {
            return InitialData.LISTEN_COUNT;
        } else if (propValue != null && propValue.trim().equalsIgnoreCase(INVERSED)) {
            return InitialData.INVERSED_LISTEN_COUNT;
        }
        return defaultValue;
    }
}
