/**
 *
 *     Copyright (C) Housecream.org
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
package org.housecream.server.storage.updater;

import static info.archinnov.achilles.counter.AchillesCounter.CQL_COUNTER_TABLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.datastax.driver.core.Session;
import fr.norad.core.updater.Updater;

@Component
public class UpdaterV0_0 implements Updater {

    @Autowired
    private Session session;

    @Override
    public void update() {

        session.execute("CREATE TABLE version (version text PRIMARY KEY);");

        StringBuilder tableAchillesCounter = new StringBuilder();
        tableAchillesCounter.append("CREATE TABLE ").append(CQL_COUNTER_TABLE).append("(");
        tableAchillesCounter.append("fqcn text,");
        tableAchillesCounter.append("primary_key text,");
        tableAchillesCounter.append("property_name text,");
        tableAchillesCounter.append("counter_value counter,");
        tableAchillesCounter.append("primary key((fqcn,primary_key),property_name))");

        session.execute(tableAchillesCounter.toString());
    }

}
