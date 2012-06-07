package aiaudio.system.iface.prop;

/**
 *
 * @author Anastasiya
 */
public class IntegerProperty extends ObjectProperty<Integer> {

    public IntegerProperty(String name, Integer defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Integer parse(String propValue) {
        if (propValue != null) {
            try {
                return Integer.valueOf(propValue);
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
