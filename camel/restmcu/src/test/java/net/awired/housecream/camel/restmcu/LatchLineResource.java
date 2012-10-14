package net.awired.housecream.camel.restmcu;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.restmcu.api.domain.line.RestMcuLine;
import net.awired.restmcu.api.domain.line.RestMcuLineSettings;
import net.awired.restmcu.api.resource.client.RestMcuLineResource;

public class LatchLineResource implements RestMcuLineResource {

    public static class LineInfo {
        public RestMcuLineSettings settings;
        public RestMcuLine description;
        public float value;
        public CountDownLatch valueLatch = new CountDownLatch(1);
    }

    public Map<Integer, LineInfo> lines = new HashMap<Integer, LatchLineResource.LineInfo>();

    public void line(int lineId, LineInfo lineInfo) {
        lines.put(lineId, lineInfo);
    }

    public LineInfo line(int lineId) {
        return lines.get(lineId);
    }

    public float awaitLineValue(int lineId) throws InterruptedException {
        LineInfo lineInfo = lines.get(lineId);
        if (!lineInfo.valueLatch.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("Countdown timeout");
        }
        return lineInfo.value;
    }

    @Override
    public RestMcuLine getLine(Integer lineId) throws NotFoundException {
        LineInfo lineInfo = lines.get(lineId);
        if (lineInfo == null) {
            throw new NotFoundException("line not found" + lineId);
        }
        return lineInfo.description;
    }

    @Override
    public RestMcuLineSettings getLineSettings(Integer lineId) throws NotFoundException, UpdateException {
        LineInfo lineInfo = lines.get(lineId);
        if (lineInfo == null) {
            throw new NotFoundException("line not found" + lineId);
        }
        return lineInfo.settings;
    }

    @Override
    public void setLineSettings(Integer lineId, RestMcuLineSettings lineSettings) throws NotFoundException,
            UpdateException {
        LineInfo lineInfo = lines.get(lineId);
        if (lineInfo == null) {
            throw new NotFoundException("line not found" + lineId);
        }

        if (lineSettings.getName() != null) {
            lineInfo.settings.setName(lineSettings.getName());
        }

        if (lineSettings.getNotifies() != null) {
            lineInfo.settings.setNotifies(lineSettings.getNotifies());
        }
    }

    @Override
    public Float getLineValue(Integer lineId) throws NotFoundException {
        LineInfo lineInfo = lines.get(lineId);
        if (lineInfo == null) {
            throw new NotFoundException("line not found" + lineId);
        }
        return lineInfo.value;
    }

    @Override
    public void setLineValue(Integer lineId, Float value) throws NotFoundException, UpdateException {
        LineInfo lineInfo = lines.get(lineId);
        if (lineInfo == null) {
            throw new NotFoundException("line not found" + lineId);
        }
        lineInfo.value = value;
        lineInfo.valueLatch.countDown();
    }

}