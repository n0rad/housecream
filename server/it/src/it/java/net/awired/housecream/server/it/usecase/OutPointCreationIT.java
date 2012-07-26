package net.awired.housecream.server.it.usecase;

import static org.junit.Assert.assertEquals;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.housecream.server.common.domain.outPoint.OutPoint;
import net.awired.housecream.server.common.domain.outPoint.OutPointType;
import net.awired.housecream.server.it.HcsTestRule;
import net.awired.housecream.server.it.RestServerRule;
import net.awired.housecream.server.it.restmcu.RestMcuEmptyBoardResource;
import net.awired.housecream.server.it.restmcu.RestMcuEmptyPinResource;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class OutPointCreationIT {

    @Rule
    public HcsTestRule hcs = new HcsTestRule();
    private static RestMcuBoard board;

    @Before
    public void before() {
        board = new RestMcuBoard();
    }

    public static class SwitchResource extends RestMcuEmptyPinResource {
        @Override
        public Float getPinValue(Integer pinId) throws NotFoundException {
            if (pinId == 4) {
                return 1f;
            }
            return 0f;
        }
    }

    public static class SwitchBoardResource extends RestMcuEmptyBoardResource {
        @Override
        public RestMcuBoard getBoard() {
            return board;
        }

        @Override
        public void setBoard(RestMcuBoard board2) throws UpdateException {
            board = board2;
        }
    }

    @ClassRule
    public static RestServerRule restMcuPin = new RestServerRule(5879, SwitchResource.class,
            SwitchBoardResource.class);

    @Test
    public void should_update_notify_url_when_create_point() throws Exception {
        OutPoint outPoint = new OutPoint();
        outPoint.setType(OutPointType.LIGHT);
        outPoint.setName("my light");
        outPoint.setUrl("restmcu://127.0.0.1:5879/pin/4");

        Long pointId = hcs.getOutPointResource().createOutPoint(outPoint);

        Float pointValue = hcs.getOutPointResource().getPointValue(pointId);

        assertEquals((Float) 1f, pointValue);
    }
}
