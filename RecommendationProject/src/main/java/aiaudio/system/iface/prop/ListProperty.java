package aiaudio.system.iface.prop;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author Anastasiya
 */
public abstract class ListProperty<T> extends ObjectProperty<List<T>> {

    public static final String LIST_DELIMITER = ";";

    public ListProperty(String name, List<T> defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected List<T> parse(String propValue) {
        StringTokenizer tok = new StringTokenizer(propValue, LIST_DELIMITER);
        List<T> v = new Vector<T>();
        while (tok.hasMoreTokens()) {
            String t = tok.nextToken();
            try {
                addValue(t, v);
            } catch (NumberFormatException e) {
                // skip 
            }
        }
        return v;
    }

    protected abstract void addValue(String propValue, List<T> list);
}
