package org.housecream.server.storage.updater;

import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import java.util.HashSet;
import java.util.Set;
import org.housecream.server.Housecream;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import fr.norad.core.updater.ApplicationUpdater;
import fr.norad.core.updater.Version;

public class DbUpdater extends ApplicationUpdater {

    private final Session session;
    private final PreparedStatement updateStatement;
    private final PreparedStatement selectStatement;

    public DbUpdater(Session session) {
        super(Housecream.HOUSECREAM_NAME, new V0(session));
        this.session = session;
        try {
            session.execute("CREATE TABLE version (version text PRIMARY KEY, names set<text>)");
        } catch (AlreadyExistsException e) {
            //nothing to do
        }
        updateStatement = session.prepare("insert into version(version, names) VALUES (?, ?)").enableTracing();
        selectStatement = session.prepare("select names from version where version = ?").enableTracing();
    }

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
    protected Set<String> getUpdatedNames(Version version) {
        ResultSet res = session.execute(selectStatement.bind(version.toString()));
        if (res.isExhausted()) {
            return null;
        }
        Set<String> set = res.one().getSet("names", String.class);
        return set;
    }

    @Override
    protected void addUpdatedName(Version version, String name) {
        HashSet<Object> hashSet = new HashSet<>();
        hashSet.add(name);
        Set<String> updatedNames = getUpdatedNames(version);
        if (updatedNames != null) {
            hashSet.addAll(updatedNames);
        }
        session.execute(updateStatement.bind(version.toString(), hashSet));
    }
}
