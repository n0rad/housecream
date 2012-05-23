package net.awired.housecream.client.it.input.digital;

import net.awired.housecream.client.HccTestRule;
import org.junit.Rule;

public class PinResourceInputDigitalIT {

    private static final int PIN_ID = 5;

    @Rule
    public HccTestRule hcc = new HccTestRule();

    //    @Test
    //    public void should_get_valid_input_pin() throws Exception {
    //
    //        HccPinDescription pin = hcc.getPinResource().getPinDescription(PIN_ID);
    //
    //        assertTrue(EqualsBuilder.reflectionEquals(pin, DefaultITDomainHelper.buildDefaultPin(PIN_ID)
    //                .getDescription()));
    //    }

}
