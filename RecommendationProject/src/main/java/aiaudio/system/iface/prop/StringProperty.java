package aiaudio.system.iface.prop;

import java.util.Properties;

/**
 *
 * @author Anastasiya
 */
public class StringProperty extends ObjectProperty<String> {

    public StringProperty(String name, String defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public void read(Properties prop) {
        value = prop.getProperty(name, defaultValue);
    }

    @Override
    protected String parse(String propValue) {
        return propValue;
    }
}
