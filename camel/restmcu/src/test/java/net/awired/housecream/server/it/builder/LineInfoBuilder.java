package net.awired.housecream.server.it.builder;

import net.awired.housecream.server.it.restmcu.LatchLineResource.LineInfo;
import net.awired.restmcu.api.domain.line.RestMcuLine;
import net.awired.restmcu.api.domain.line.RestMcuLineDirection;
import net.awired.restmcu.api.domain.line.RestMcuLineSettings;

public class LineInfoBuilder {
    private String name;
    private float value;
    private RestMcuLineDirection direction;

    public LineInfo build() {
        LineInfo pinInfo = new LineInfo();
        pinInfo.description = new RestMcuLine();
        pinInfo.settings = new RestMcuLineSettings();
        pinInfo.value = value;
        pinInfo.settings.setName(name);
        pinInfo.description.setDirection(direction);
        return pinInfo;
    }

    public LineInfoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public LineInfoBuilder value(float value) {
        this.value = value;
        return this;
    }

    public LineInfoBuilder direction(RestMcuLineDirection direction) {
        this.direction = direction;
        return this;
    }

}
