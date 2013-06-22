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

import static net.awired.core.updater.Version.V;
import java.util.HashSet;
import net.awired.core.updater.Update;
import net.awired.core.updater.UpdateRunner;
import net.awired.core.updater.Version;

public class DbUpdater extends UpdateRunner {

    public DbUpdater() {
        super("Housecream DB", new HashSet<Update>() {
            {
                add(new Update(V(0), new UpdaterV0()));
            }
        });
    }

    @Override
    protected Version getCurrentVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setNewVersion(Version version) {
        // TODO Auto-generated method stub

    }

}
