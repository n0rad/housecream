package net.awired.housecream.client.it.input.digital;

import static org.junit.Assert.assertTrue;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.domain.HccPinDescription;
import net.awired.housecream.client.common.test.DefaultTestDomainHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceInputDigitalIT {

    private static final int PIN_ID = 5;

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test
    public void should_get_valid_input_pin() throws Exception {

        HccPinDescription pin = hcc.getPinResource().getPinDescription(PIN_ID);

        assertTrue(EqualsBuilder.reflectionEquals(pin, DefaultTestDomainHelper.buildDefaultPin(PIN_ID)
                .getDescription()));
    }

}
