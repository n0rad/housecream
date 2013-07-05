package net.awired.housecream.server.storage.dao;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.UUID;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import org.junit.Rule;
import org.junit.Test;

public class InPointDaoTest {

    @Rule
    public AchillesRule<InPointDao> db = new AchillesRule<>("net.awired", InPointDao.class);

    @Test
    public void should_() throws Exception {
        InPoint inPoint = new InPoint();
        inPoint.setName("salut!");
        inPoint.setId(UUID.randomUUID());
        db.dao().save(inPoint);

        assertThat(db.dao().find(inPoint.getId()).getName()).isEqualTo("salut!");
    }
}
