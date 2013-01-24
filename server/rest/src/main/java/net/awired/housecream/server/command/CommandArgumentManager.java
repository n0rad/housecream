package net.awired.housecream.server.command;

import net.awired.aclm.argument.CliArgumentManager;
import net.awired.aclm.argument.args.CliNoParamArgument;

public class CommandArgumentManager extends CliArgumentManager {

    private final CliNoParamArgument status;

    protected CommandArgumentManager() {
        super("");

        status = new CliNoParamArgument('s');
        addArg(status);
    }
}
