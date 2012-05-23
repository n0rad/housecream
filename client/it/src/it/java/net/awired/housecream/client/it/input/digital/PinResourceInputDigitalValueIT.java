package net.awired.housecream.client.it.input.digital;

import net.awired.housecream.client.HccTestRule;
import org.junit.Rule;

public class PinResourceInputDigitalValueIT {

    private static final int PIN_ID = 5;

    @Rule
    public HccTestRule hcc = new HccTestRule();
    //
    //    @Test
    //    public void should_get_value() throws Exception {
    //        assertEquals((Float) 1f, hcc.getPinResource().getValue(PIN_ID));
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_set_value() throws Exception {
    //        hcc.getPinResource().setValue(PIN_ID, 0f);
    //    }

}
