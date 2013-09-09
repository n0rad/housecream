package org.housecream.server.storage.updater;

import static fr.norad.core.updater.Version.V;
import com.datastax.driver.core.Session;
import fr.norad.core.updater.ApplicationVersion;
import fr.norad.core.updater.Update;

public class V0 extends ApplicationVersion {

    public V0(final Session session) {
        super(V(0, 1), new Update("create outpoints CF") {
            @Override
            public void runUpdate() {
                session.execute("CREATE TABLE outpoints(id UUID," //
                        + "name text," //
                        + "uri text," //
                        + " PRIMARY KEY(id));");
            }
        }, new Update("create inpoints cf") {
            @Override
            public void runUpdate() {
                session.execute("CREATE TABLE inpoints(id UUID," //
                        + "name text," //
                        + "uri text," //
                        + " PRIMARY KEY(id));");
            }
        });
    }
}
