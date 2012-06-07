package aiaudio.system.iface;

import aiaudio.system.Phase;
import aiaudio.system.iface.arguments.BooleanArgument;
import aiaudio.system.iface.arguments.PhaseArgument;
import aiaudio.system.iface.arguments.StringArgument;

/**
 *
 * @author Anastasiya
 */
public class CommandLineArguments {

    private StringArgument inputDirectory = new StringArgument("-i", "input directory", "");
    private StringArgument outputDirectory = new StringArgument("-o", "output directory", "");
    private PhaseArgument phase = new PhaseArgument("-p", "algorithm phase");
    private BooleanArgument verbose = new BooleanArgument("-verbose", "verbose mode");

    public String getInputDirectory() {
        return inputDirectory.getValue();
    }

    public String getOutputDirectory() {
        return outputDirectory.getValue();
    }

    public Phase getPhase() {
        return phase.getValue();
    }

    public Boolean getVerbose() {
        return verbose.getValue();
    }

    private CommandLineArguments() {
    }

    public static CommandLineArguments parse(String... args) {
        CommandLineArguments commandLineArguments = new CommandLineArguments();
        commandLineArguments.inputDirectory.read(args);
        commandLineArguments.outputDirectory.read(args);
        commandLineArguments.phase.read(args);
        commandLineArguments.verbose.read(args);
        return commandLineArguments;
    }
}
