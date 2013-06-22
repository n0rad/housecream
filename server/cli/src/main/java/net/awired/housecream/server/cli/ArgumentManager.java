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
import java.net.InetAddress;
import java.net.UnknownHostException;
import net.awired.housecream.server.FileInfoEnum;
import net.awired.housecream.server.Housecream;
import net.awired.typed.command.line.parser.argument.CliArgumentManager;
import net.awired.typed.command.line.parser.argument.CliArgumentParseException;
import net.awired.typed.command.line.parser.argument.args.CliNoParamArgument;
import net.awired.typed.command.line.parser.argument.args.CliOneParamArgument;
import net.awired.typed.command.line.parser.param.CliParamEnum;
import net.awired.typed.command.line.parser.param.CliParamFile;
import net.awired.typed.command.line.parser.param.CliParamInetAddress;
import net.awired.typed.command.line.parser.param.CliParamInt;
import net.awired.typed.command.line.parser.param.CliParamString;

public class ArgumentManager extends CliArgumentManager {

    private final CliOneParamArgument<InetAddress> housecreamHost;
    private final CliOneParamArgument<Integer> housecreamPort;
    private final CliOneParamArgument<String> housecreamContextPath;

    private final CliOneParamArgument<InetAddress> cassandraHost;
    private final CliOneParamArgument<Integer> cassandraPort;
    private final CliOneParamArgument<String> cassandraLogin;
    private final CliOneParamArgument<String> cassandraPassword;

    private final CliNoParamArgument info;
    private final CliOneParamArgument<File> rootFolder;
    //    private final CliNoParamArgument clearDb;
    private final CliOneParamArgument<FileInfoEnum> displayFile;

    public ArgumentManager() {
        super("housecream");
        getUsageDisplayer().setUsageShort(true);

        // -d
        displayFile = new CliOneParamArgument<>('d', new CliParamEnum<>("file", FileInfoEnum.class));
        displayFile.setDescription("Display an information file and exit");
        displayFile.setName("display");
        addArg(displayFile);

        //        // -C
        //        clearDb = new CliNoParamArgument('C');
        //        clearDb.setName("clear");
        //        clearDb.setDescription("Clear database and exit");
        //        addArg(clearDb);

        // -v
        info = new CliNoParamArgument('v');
        info.setName("version");
        info.setDescription("Print Housecream information and exit");
        addArg(info);

        // -o
        housecreamHost = new CliOneParamArgument<>('o', new CliParamInetAddress("host"));
        housecreamHost.setName("host");
        try {
            housecreamHost.setParamOneDefValue(InetAddress.getByName("0.0.0.0"));
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Cannot build default listen host 0.0.0.0");
        }
        housecreamHost.setDescription("Host to start Housecream");
        addArg(housecreamHost);

        // -p
        CliParamInt portParam = new CliParamInt("port");
        portParam.setNegativable(false);
        portParam.setZeroable(false);
        housecreamPort = new CliOneParamArgument<>('p', portParam);
        housecreamPort.setParamOneDefValue(4242);
        housecreamPort.setName("port");
        housecreamPort.setDescription("Port to start Housecream");
        addArg(housecreamPort);

        // -c
        housecreamContextPath = new CliOneParamArgument<>('c', new CliParamString("contextPath"));
        housecreamContextPath.setParamOneDefValue("/");
        housecreamContextPath.setName("contextpath");
        housecreamContextPath.setDescription("Context path to access the application");
        addArg(housecreamContextPath);

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

        // -H
        cassandraHost = new CliOneParamArgument<>('H', new CliParamInetAddress("host"));
        cassandraHost.setName("db-host");
        cassandraHost.setDescription("Host to connect to Cassandra. Start embedded if not provided");
        addArg(cassandraHost);

        // -P
        CliParamInt paramOneArgument = new CliParamInt("port");
        paramOneArgument.setNegativable(false);
        paramOneArgument.setZeroable(false);

        cassandraPort = new CliOneParamArgument<>('P', paramOneArgument);
        cassandraPort.setName("db-port");
        cassandraPort.setDescription("Port to connect to Cassandra");
        addArg(cassandraPort);

        // -L
        cassandraLogin = new CliOneParamArgument<>('L', new CliParamString("username"));
        cassandraLogin.setName("db-login");
        cassandraLogin.setDescription("Login to connect to Cassandra");
        addArg(cassandraLogin);

        // -S
        cassandraPassword = new CliOneParamArgument<>('S', new CliParamString("password"));
        cassandraPassword.setName("db-password");
        cassandraPassword.setDescription("Password to connect to Cassandra");
        addArg(cassandraPassword);

    }

    public CliOneParamArgument<InetAddress> getHousecreamHost() {
        return housecreamHost;
    }

    public CliOneParamArgument<Integer> getHousecreamPort() {
        return housecreamPort;
    }

    public CliOneParamArgument<String> getHousecreamContextPath() {
        return housecreamContextPath;
    }

    public CliOneParamArgument<InetAddress> getCassandraHost() {
        return cassandraHost;
    }

    public CliOneParamArgument<Integer> getCassandraPort() {
        return cassandraPort;
    }

    public CliOneParamArgument<String> getCassandraLogin() {
        return cassandraLogin;
    }

    public CliOneParamArgument<String> getCassandraPassword() {
        return cassandraPassword;
    }

    public CliNoParamArgument getInfo() {
        return info;
    }

    public CliOneParamArgument<File> getRootFolder() {
        return rootFolder;
    }

    public CliOneParamArgument<FileInfoEnum> getDisplayFile() {
        return displayFile;
    }
}
