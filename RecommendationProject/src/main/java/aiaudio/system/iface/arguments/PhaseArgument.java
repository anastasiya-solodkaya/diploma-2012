package aiaudio.system.iface.arguments;

import aiaudio.system.Phase;

/**
 *
 * @author Anastasiya
 */
public class PhaseArgument extends Argument<Phase> {

    public PhaseArgument(String key, String name) {
        super(key, name, null);
    }

    @Override
    protected Phase parseValue(String input) {
        return Phase.fromString(input);
    }
}