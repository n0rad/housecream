package org.housecream.server.storage.updater;

import static org.fest.assertions.api.Assertions.assertThat;
import org.housecream.server.storage.dao.CassandraRule;
import org.junit.Rule;
import org.junit.Test;

public class DbUpdaterTest {

    @Rule
    public CassandraRule cassandra = new CassandraRule();

    @Test
    public void should_update() throws Exception {
        DbUpdater dbUpdater = new DbUpdater(cassandra.getSession());

        dbUpdater.updateToLatest();

        assertThat(dbUpdater.getCurrentVersion()).isNotNull(); //sux

    }

}
