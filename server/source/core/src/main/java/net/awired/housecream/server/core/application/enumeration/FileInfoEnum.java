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

package net.awired.housecream.server.core.application.enumeration;

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
