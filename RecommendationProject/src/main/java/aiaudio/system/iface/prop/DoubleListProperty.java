package aiaudio.system.iface.prop;

import java.util.List;

/**
 *
 * @author Anastasiya
 */
public class DoubleListProperty extends ListProperty<Double> {

    public DoubleListProperty(String name, List<Double> defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected void addValue(String propValue, List<Double> list) {
        if (propValue != null) {
            try {
                Double d = Double.valueOf(propValue);
                list.add(d);
            } catch (NumberFormatException ex) {
            }
        }
    }
}
