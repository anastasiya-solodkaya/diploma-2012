package aiaudio.system.iface.arguments;

/**
 *
 * @author Anastasiya
 */
public abstract class Argument<T> {

    private String key;
    private String name;
    private T defaultValue;
    private T value;

    public Argument(String key, String name, T defaultValue) {
        this.key = key;
        this.name = name;
        this.defaultValue = defaultValue;
        value = defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void read(String... args) {
        value = parse(args);
    }

    private T parse(String... args) {
        int index = findKeyIndex(args);
        if (index == -1) {
            String msg = getArgumentNotSetMessage();
            System.out.println(msg);
            return defaultValue;
        }
        if (args.length <= index + 1) {
            String msg = getArgumentErrorMessage();
            System.out.println(msg);
            return defaultValue;
        }
        String val = args[index + 1];
        return parseValue(val);
    }

    protected int findKeyIndex(String[] args) {
        int keyIndex = -1;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals(key)) {
                keyIndex = i;
                break;
            }
        }
        return keyIndex;

    }

    private String getArgumentNotSetMessage() {
        return "Argument " + key + "(" + name + ") not set. Use default value " + defaultValue;
    }

    private String getArgumentErrorMessage() {
        return "Error parsing argument " + key + "(" + name + ") not set. Use default value " + defaultValue;
    }

    protected abstract T parseValue(String val);
}
