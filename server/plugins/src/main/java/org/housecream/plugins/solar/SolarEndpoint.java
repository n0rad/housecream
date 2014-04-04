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
package org.housecream.plugins.solar;

import java.net.URI;
import java.util.TimeZone;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * solar:Europe/Paris?latitude=42&longitude=43 // notify for all events
 * solar:Europe/Paris?latitude=42&longitude=43&globalphase= // TODO notify for specific events, phase, global phase
 * solar:Europe/Paris?latitude=42&longitude=43&twilight= // TODO notify with specific twilight event only
 * solar:Europe/Paris?latitude=42&longitude=43&offset= // TODO notify with offset
 */
public class SolarEndpoint extends DefaultEndpoint {

    private TimeZone timezone;
    private Double latitude;
    private Double longitude;

    public SolarEndpoint(String uri, SolarComponent component) {
        super(uri, component);
        URI realUri = getEndpointConfiguration().getURI();
        timezone = TimeZone.getTimeZone(realUri.getHost() + realUri.getPath());
    }

    @Override
    public Producer createProducer() throws Exception {
        return null;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new SolarConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    /////////////////////////////////////////

    public TimeZone getTimezone() {
        return timezone;
    }

    public void setTimezone(TimeZone timezone) {
        this.timezone = timezone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
