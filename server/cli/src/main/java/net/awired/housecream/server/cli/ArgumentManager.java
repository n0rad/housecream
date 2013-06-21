/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package net.awired.housecream.server.cli;

import java.io.File;
import net.awired.housecream.server.FileInfoEnum;
import net.awired.housecream.server.Housecream;
import net.awired.typed.command.line.parser.argument.CliArgumentManager;
import net.awired.typed.command.line.parser.argument.CliArgumentParseException;
import net.awired.typed.command.line.parser.argument.args.CliNoParamArgument;
import net.awired.typed.command.line.parser.argument.args.CliOneParamArgument;
import net.awired.typed.command.line.parser.param.CliParamEnum;
import net.awired.typed.command.line.parser.param.CliParamFile;
import net.awired.typed.command.line.parser.param.CliParamInt;
import net.awired.typed.command.line.parser.param.CliParamString;

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
        displayFile = new CliOneParamArgument<>('d', new CliParamEnum<>("file", FileInfoEnum.class));
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
        portArg = new CliOneParamArgument<>('p', portParam);
        portArg.setParamOneDefValue(4242);
        portArg.setName("port");
        portArg.setDescription("Port for servlet Contrainer");
        addArg(portArg);

        // -c
        contextPath = new CliOneParamArgument<>('c', new CliParamString("contextPath"));
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
        rootFolder = new CliOneParamArgument<>('r', new CliParamFile("home") {
            @Override
            public File parse(String param) throws CliArgumentParseException {
                File res = super.parse(param);
                System.setProperty(Housecream.HOUSECREAM_HOME_KEY, param);
                return res;
            }
        });
        rootFolder.setParamOneDefValue(Housecream.HOUSECREAM.findDefaultOsHomeDirectory());
        rootFolder.setName("home");
        rootFolder.setDescription("Housecream root folder (override system property : "
                + Housecream.HOUSECREAM_HOME_KEY + ")");
        addArg(rootFolder);
    }
}
