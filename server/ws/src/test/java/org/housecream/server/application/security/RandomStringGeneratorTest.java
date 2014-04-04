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
package org.housecream.server.application.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RandomStringGeneratorTest {
    
    private static RandomStringGenerator generator = new RandomStringGenerator();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_fail_for_numeric_string_and_zero_length() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("length < 1: 0");
        generator.numeric(0);
    }

    @Test
    public void should_fail_for_numeric_string_and_negative_length() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("length < 1: -1");
        generator.numeric(-1);
    }

    @Test
    public void should_return_numeric_string_with_correct_composition_for_length_1() throws Exception {
        assertThat(generator.numeric(1)).isIn("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }

    @Test
    public void should_return_numeric_string_with_correct_composition_for_average_length() throws Exception {
        String result = generator.numeric(20);
        assertThat(result).hasSize(20);
        assertThat(result).matches("\\d+");
    }

    @Test
    public void should_return_diffent_numeric_strings_with_consecutive_calls() throws Exception {
        assertThat(generator.numeric(20)).isNotEqualTo(generator.numeric(20));
    }

    @Test
    public void should_fail_for_base64_string_and_zero_length() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("length < 1: 0");
        generator.base64(0);
    }

    @Test
    public void should_fail_for_base64_string_and_negative_length() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("length < 1: -1");
        generator.base64(-1);
    }

    @Test
    public void should_return_base64_string_with_valid_length() throws Exception {
        String result = generator.base64(20);
        assertThat(result).hasSize(20);
    }
}
