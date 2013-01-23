package net.awired.housecream.server.cli;

import java.io.File;
import net.awired.aclm.argument.CliArgumentManager;
import net.awired.aclm.argument.CliArgumentParseException;
import net.awired.aclm.argument.args.CliNoParamArgument;
import net.awired.aclm.argument.args.CliOneParamArgument;
import net.awired.aclm.param.CliParamEnum;
import net.awired.aclm.param.CliParamFile;
import net.awired.aclm.param.CliParamInt;
import net.awired.aclm.param.CliParamString;
import net.awired.housecream.server.core.application.Housecream;
import net.awired.housecream.server.core.application.enumeration.FileInfoEnum;

class ArgumentManager extends CliArgumentManager {

    public final CliOneParamArgument<Integer> portArg;
    public final CliOneParamArgument<String> contextPath;
    public final CliNoParamArgument info;
    public final CliOneParamArgument<File> rootFolder;
    public final CliNoParamArgument clearDb;
    public final CliOneParamArgument<FileInfoEnum> displayFile;

    public ArgumentManager() {
        super("housecream");
        getUsageDisplayer().setUsageShort(true);

        // -d
        displayFile = new CliOneParamArgument<FileInfoEnum>('d', new CliParamEnum<FileInfoEnum>("file",
                FileInfoEnum.class));
        displayFile.setDescription("Display an information file and exit");
        displayFile.setName("display");
        addArg(displayFile);

        // -C
        clearDb = new CliNoParamArgument('C');
        clearDb.setName("clear");
        clearDb.setDescription("Clear database and exit");
        addArg(clearDb);

        // -p
        CliParamInt portParam = new CliParamInt("port");
        portParam.setNegativable(false);
        portParam.setZeroable(false);
        portArg = new CliOneParamArgument<Integer>('p', portParam);
        portArg.setParamOneDefValue(4242);
        portArg.setName("port");
        portArg.setDescription("Port for servlet Contrainer");
        addArg(portArg);

        // -c
        contextPath = new CliOneParamArgument<String>('c', new CliParamString("contextPath"));
        contextPath.setParamOneDefValue("/");
        contextPath.setName("contextpath");
        contextPath.setDescription("Context path to access the application");
        addArg(contextPath);

        // -v
        info = new CliNoParamArgument('v');
        info.setName("version");
        info.setDescription("Print Housecream information and exit");
        addArg(info);

        // -r
        rootFolder = new CliOneParamArgument<File>('r', new CliParamFile("home") {
            @Override
            public File parse(String param) throws CliArgumentParseException {
                File res = super.parse(param);
                System.setProperty(Housecream.HOME_KEY, param);
                return res;
            }
        });
        rootFolder.setParamOneDefValue(new File("~/.housecream"));
        rootFolder.setName("home");
        rootFolder.setDescription("Housecream root folder");
        addArg(rootFolder);
    }
}
