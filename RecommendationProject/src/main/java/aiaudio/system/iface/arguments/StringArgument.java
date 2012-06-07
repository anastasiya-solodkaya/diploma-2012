package aiaudio.system.iface.arguments;

/**
 *
 * @author Anastasiya
 */
public class StringArgument extends Argument<String>{

    public StringArgument(String key, String name, String defaultValue) {
        super(key, name, defaultValue);
    }

    @Override
    protected String parseValue(String val) {
        return val.trim();
    }
    
}
