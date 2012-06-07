package aiaudio.system.iface.prop;

/**
 *
 * @author Anastasiya
 */
public class DoubleProperty extends ObjectProperty<Double> {

    public DoubleProperty(String name, Double defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected Double parse(String propValue) {
        if (propValue != null) {
            try {
                return Double.valueOf(propValue);
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
