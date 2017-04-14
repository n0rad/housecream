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
package org.housecream.solar.enumeration;

import static org.fest.assertions.api.Assertions.assertThat;
import org.housecream.solar.enumeration.SolarTimeEvent;
import org.junit.Test;

public class SolarTimeEventTest {

    @Test
    public void should_find_previous_in_middle() throws Exception {
        assertThat(SolarTimeEvent.civilDawn.findPrevious()).isEqualTo(SolarTimeEvent.nauticalDawn);
    }

    @Test
    public void should_find_previous_at_end() throws Exception {
        assertThat(SolarTimeEvent.astronomicalDusk.findPrevious()).isEqualTo(SolarTimeEvent.nauticalDusk);
    }

    @Test
    public void should_find_previous_at_beginning() throws Exception {
        assertThat(SolarTimeEvent.nadir.findPrevious()).isEqualTo(SolarTimeEvent.astronomicalDusk);
    }

    @Test
    public void should_find_previous_at_beginning_plus_one() throws Exception {
        assertThat(SolarTimeEvent.astronomicalDawn.findPrevious()).isEqualTo(SolarTimeEvent.nadir);
    }

}
