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
package org.housecream.server.api.domain.config;


import java.util.Locale;
import java.util.TimeZone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import fr.norad.jaxrs.doc.api.Description;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Config {

    public static final TimeZone systemTimezone = TimeZone.getDefault();

    public static final void initTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    @Description("Activate OAuth2 security globally on the server")
    private boolean securityEnabled = false;
    private int securitySeedLength = 50;
    private int securityDefaultTokenLifetimeSeconds = 60;
    private int securityDefaultRefreshTokenLifetimeSeconds = 60;
    private int securityMaxFailedLoginAttempt = 3;
    private int securityFailedLoginLifetimeSeconds = 120;
    private int securityRandomStringLength = 128;

    private Locale locale = Locale.getDefault();
    private TimeZone timeZone = systemTimezone;
    private TemperatureUnit temperatureUnit = TemperatureUnit.Celcius;
    private SpeedUnit speedUnit = SpeedUnit.KmPerHour;

}
