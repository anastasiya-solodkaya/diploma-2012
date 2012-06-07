package aiaudio.system.iface.arguments;

/**
 *
 * @author Anastasiya
 */
public class BooleanArgument extends Argument<Boolean> {

    public BooleanArgument(String key, String name) {
        super(key, name, false);
    }

    @Override
    protected Boolean parseValue(String val) {
        return true;
    }   
    
}
