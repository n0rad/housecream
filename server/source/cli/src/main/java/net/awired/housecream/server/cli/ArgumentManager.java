/**
 *     Copyright (C) 2010 Julien SMADJA <julien dot smadja at gmail dot com> - Arnaud LEMAIRE <alemaire at norad dot fr>
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

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
import net.awired.housecream.server.core.application.common.ApplicationHelper;
import net.awired.housecream.server.core.application.enumeration.FileInfoEnum;
import net.awired.housecream.server.core.application.enumeration.LogLevelEnum;

class ArgumentManager extends CliArgumentManager {

    public final CliOneParamArgument<Integer> portArg;
    public final CliOneParamArgument<String> contextPath;
    public final CliNoParamArgument info;
    public final CliOneParamArgument<File> rootFolder;
    public final CliNoParamArgument clearDb;
    public final CliOneParamArgument<LogLevelEnum> logLevel;
    public final CliOneParamArgument<LogLevelEnum> logRootLevel;
    public final CliOneParamArgument<FileInfoEnum> displayFile;

    public ArgumentManager(Main main) {
        super("visuwall");

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
        portArg.setParamOneDefValue(8081);
        portArg.setName("port");
        portArg.setDescription("Port for servlet Contrainer");
        addArg(portArg);

        // -c
        contextPath = new CliOneParamArgument<String>('c', new CliParamString("contextPath"));
        contextPath.setParamOneDefValue("/");
        contextPath.setName("contextpath");
        contextPath.setDescription("Context path to access the application");
        addArg(contextPath);

        // -i
        info = new CliNoParamArgument('i');
        info.setName("info");
        info.setDescription("Print Visuwall information and exit");
        addArg(info);

        // -r
        rootFolder = new CliOneParamArgument<File>('r', new CliParamFile("home") {
            @Override
            public File parse(String param) throws CliArgumentParseException {
                File res = super.parse(param);
                System.setProperty(ApplicationHelper.HOME_KEY, param);
                return res;
            }
        });
        rootFolder.setParamOneDefValue(new File("~/.visuwall"));
        rootFolder.setName("home");
        rootFolder.setDescription("Visuwall root folder");
        addArg(rootFolder);

        // -l
        logLevel = new CliOneParamArgument<LogLevelEnum>('l', new CliParamEnum<LogLevelEnum>("level",
                LogLevelEnum.class));
        logLevel.setDescription("Change log level");
        logLevel.setParamOneDefValue(LogLevelEnum.info);
        logLevel.setName("level");
        addArg(logLevel);

        // -L
        logRootLevel = new CliOneParamArgument<LogLevelEnum>('L', new CliParamEnum<LogLevelEnum>("level",
                LogLevelEnum.class));
        logRootLevel.setDescription("Change log level for non-awired libs");
        logRootLevel.setParamOneDefValue(LogLevelEnum.info);
        logRootLevel.setName("root-level");
        addArg(logRootLevel);
    }
}
