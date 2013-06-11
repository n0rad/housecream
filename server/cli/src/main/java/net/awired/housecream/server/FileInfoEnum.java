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
package net.awired.housecream.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public enum FileInfoEnum {
    readme("README.rst"), //
    changelog("CHANGELOG.txt"), //
    install("INSTALL.txt"), //
    licence("LICENCE.txt");

    private final String filename;

    private FileInfoEnum(String filename) {
        this.filename = filename;
    }

    public void display() {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
