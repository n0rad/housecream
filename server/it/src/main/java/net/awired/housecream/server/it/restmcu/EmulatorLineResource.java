package net.awired.housecream.server.it.restmcu;

import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.ajsl.web.rest.RestContext;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.resource.server.RestMcuNotifyResource;

public class EmulatorLineResource extends LatchLineResource {

    private final EmulatorBoardResource boardResource;

    public EmulatorLineResource(EmulatorBoardResource boardResource) {
        this.boardResource = boardResource;
    }

    @Override
    public void setLineValue(Integer lineId, Float value) throws NotFoundException, UpdateException {
        Float oldValue = lines.get(lineId).value;
        super.setLineValue(lineId, value);
        notifyChange(lineId, oldValue);
    }

    private void notifyChange(Integer lineId, Float oldValue) {
        RestMcuNotifyResource client = new RestContext().prepareClient(RestMcuNotifyResource.class, boardResource
                .getBoardSettings().getNotifyUrl(), null, true);

        RestMcuLineNotification lineNotification = new RestMcuLineNotification();
        lineNotification.setId(lineId);
        //        lineNotification.setNotify(notify);
        lineNotification.setOldValue(oldValue);
        String source = boardResource.getBoardSettings().getIp() + ":" + boardResource.getBoardSettings().getPort();
        lineNotification.setSource(source);
        lineNotification.setValue(lines.get(lineId).value);
        client.lineNotification(lineNotification);
    }
}
