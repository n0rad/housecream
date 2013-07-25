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
package org.housecream.server.storage.dao;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.UUID;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.junit.Rule;
import org.junit.Test;

public class InPointDaoTest {

    @Rule
    public AchillesRule<InPointDao> db = new AchillesRule<>("org.housecream", InPointDao.class);

    @Test
    public void should_() throws Exception {
        InPoint inPoint = new InPoint();
        inPoint.setName("salut!");
        inPoint.setId(UUID.randomUUID());
        db.dao().save(inPoint);

        assertThat(db.dao().find(inPoint.getId()).getName()).isEqualTo("salut!");
    }
}
