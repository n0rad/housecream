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
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.core.lang.exception.NotFoundException;

public class InPointDaoTest {

    @Rule
    public CassandraDaoRule<InPointDao> db = new CassandraDaoRule<>(InPointDao.class, "session");

    @Test
    public void should_find_simple_map() throws Exception {
        InPoint inPoint = new InPoint();
        inPoint.setName("salut!");
        inPoint.setId(UUID.randomUUID());
        inPoint.setUri(URI.create("genre:style"));
        db.dao().save(inPoint);

        InPoint find = db.dao().find(inPoint.getId());

        assertThat(find.getId()).isEqualTo(inPoint.getId());
        assertThat(find.getName()).isEqualTo("salut!");
        assertThat(find.getUri()).isEqualTo(URI.create("genre:style"));
    }

    @Test
    public void should_provide_uuid() throws Exception {
        InPoint inPoint = new InPoint();
        inPoint.setName("salut!");
        inPoint.setUri(URI.create("genre:style"));
        db.dao().save(inPoint);

        assertThat(inPoint.getId()).isNotNull();
    }

    @Test
    public void should_find_all() throws Exception {
        InPoint inPoint = new InPoint();
        inPoint.setName("salut!");
        inPoint.setUri(URI.create("genre:style"));
        db.dao().save(inPoint);

        InPoint inPoint2 = new InPoint();
        inPoint2.setName("salut!2");
        inPoint2.setUri(URI.create("genre:style2"));
        db.dao().save(inPoint2);

        List<InPoint> findAll = db.dao().findAll();

        assertThat(findAll).contains(inPoint, inPoint2);
    }

    @Test
    public void should_delete() throws Exception {
        InPoint inPoint = new InPoint();
        inPoint.setName("salut!");
        inPoint.setUri(URI.create("genre:style"));
        db.dao().save(inPoint);

        db.dao().delete(inPoint.getId());

        assertThat(db.dao().findAll()).isEmpty();
    }

    @Test
    public void should_delete_all() throws Exception {
        InPoint inPoint = new InPoint();
        inPoint.setName("salut!");
        inPoint.setUri(URI.create("genre:style"));
        db.dao().save(inPoint);

        InPoint inPoint2 = new InPoint();
        inPoint2.setName("salut!2");
        inPoint2.setUri(URI.create("genre:style2"));
        db.dao().save(inPoint2);

        db.dao().deleteAll();

        assertThat(db.dao().findAll()).isEmpty();
    }

    @Test(expected = NotFoundException.class)
    public void should_throw_exception_on_not_found() throws Exception {
        db.dao().find(UUID.randomUUID());
    }
}
