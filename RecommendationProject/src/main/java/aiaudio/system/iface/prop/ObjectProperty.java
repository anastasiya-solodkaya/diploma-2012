package aiaudio.system.iface.prop;

import java.util.Properties;

/**
 *
 * @author Anastasiya
 */
public abstract class ObjectProperty<T> {

    protected String name;
    protected T defaultValue;
    protected T value;

    public ObjectProperty(String name, T defaultValue) {
        this.defaultValue = defaultValue;
        this.name = name;
        this.value = defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void read(Properties prop) {
        String propValue = prop.getProperty(name);
        value = parse(propValue);
    }

    @Override
    public String toString() {
        return "ObjectProperty{" + "value=" + value + '}';
    }
    
    
    protected abstract T parse(String propValue);
}
