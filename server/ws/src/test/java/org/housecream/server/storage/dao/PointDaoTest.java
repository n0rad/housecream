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
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.exception.PointNotFoundException;
import org.housecream.server.storage.CassandraDaoRule;
import org.junit.Rule;
import org.junit.Test;

public class PointDaoTest {

    @Rule
    public CassandraDaoRule<PointDao> db = new CassandraDaoRule<>(PointDao.class);

    @Test
    public void should_find_simple_map() throws Exception {
        Point point = new Point();
        point.setName("salut!");
        point.setId(UUID.randomUUID());
        point.setUri(URI.create("genre:style"));
        db.dao().save(point);

        Point find = db.dao().find(point.getId());

        assertThat(find.getId()).isEqualTo(point.getId());
        assertThat(find.getName()).isEqualTo("salut!");
        assertThat(find.getUri()).isEqualTo(URI.create("genre:style"));
    }

    @Test
    public void should_provide_uuid() throws Exception {
        Point point = new Point();
        point.setName("salut!");
        point.setUri(URI.create("genre:style"));
        db.dao().save(point);

        assertThat(point.getId()).isNotNull();
    }

    @Test
    public void should_find_all() throws Exception {
        Point point = new Point();
        point.setName("salut!");
        point.setUri(URI.create("genre:style"));
        db.dao().save(point);

        Point point2 = new Point();
        point2.setName("salut!2");
        point2.setUri(URI.create("genre:style2"));
        db.dao().save(point2);

        List<Point> findAll = db.dao().findAll();

        assertThat(findAll).contains(point, point2);
    }

    @Test
    public void should_delete() throws Exception {
        db.truncateColumnFamilies();

        Point point = new Point();
        point.setName("salut!");
        point.setUri(URI.create("genre:style"));
        db.dao().save(point);

        db.dao().delete(point.getId());

        assertThat(db.dao().findAll()).isEmpty();
    }

    @Test
    public void should_delete_all() throws Exception {
        Point point = new Point();
        point.setName("salut!");
        point.setUri(URI.create("genre:style"));
        db.dao().save(point);

        Point point2 = new Point();
        point2.setName("salut!2");
        point2.setUri(URI.create("genre:style2"));
        db.dao().save(point2);

        db.dao().deleteAll();

        assertThat(db.dao().findAll()).isEmpty();
    }

    @Test(expected = PointNotFoundException.class)
    public void should_throw_exception_on_not_found() throws Exception {
        db.dao().find(UUID.randomUUID());
    }
}
