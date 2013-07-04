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
package net.awired.housecream.server.storage.updater;

import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import net.awired.core.updater.Update;
import net.awired.core.updater.UpdateRunner;
import net.awired.core.updater.Updater;
import net.awired.core.updater.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;

@Component
public class DbUpdater {

    @Autowired
    private Session session;

    @Autowired
    private ApplicationContext context;

    private UpdateRunner updateRunner;

    @PostConstruct
    public void update() {
        updateRunner = new UpdateRunner("Housecream DB", springBeansToUpdate(context.getBeansOfType(Updater.class))) {

            @Override
            protected Version getCurrentVersion() {
                try {
                    Row version = session.execute(select("version").from("version")).one();
                    if (version == null) {
                        return null;
                    }
                    return Version.parse(version.getString(0));
                } catch (InvalidQueryException e) {
                    return null;
                }
            }

            @Override
            protected void setNewVersion(Version version) {
                session.execute(insertInto("version").value("version", version));
            }

        };
        updateRunner.updateToLatest();
    }

    private Set<Update> springBeansToUpdate(Map<String, Updater> beans) {
        Set<Update> updates = new HashSet<>();
        for (String beanName : beans.keySet()) {
            Updater bean = beans.get(beanName);
            String name = bean.getClass().getName();
            String version = name.substring(name.indexOf("V") + 1).replaceAll("_", ".");
            updates.add(new Update(Version.parse(version), bean));
        }
        return updates;
    }
}
