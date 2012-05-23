package net.awired.housecream.client.it.output.digital;

import net.awired.housecream.client.HccTestRule;
import org.junit.Rule;

public class PinResourceOutputDigitalValueIT {

    private static final int PIN_ID = 4;

    @Rule
    public HccTestRule hcc = new HccTestRule();
    //
    //    @Test
    //    public void should_get_value() throws Exception {
    //        assertEquals((Float) 1f, hcc.getPinResource().getValue(PIN_ID));
    //    }
    //
    //    @Test
    //    public void should_set_value() throws Exception {
    //        hcc.getPinResource().setValue(PIN_ID, 0f);
    //
    //        assertEquals((Float) 0f, hcc.getPinResource().getValue(PIN_ID));
    //    }
    //
    //    @Test
    //    public void should_set_value2() throws Exception {
    //        hcc.getPinResource().setValue(PIN_ID, 1f);
    //
    //        assertEquals((Float) 1f, hcc.getPinResource().getValue(PIN_ID));
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_overflow_value() throws Exception {
    //        hcc.getPinResource().setValue(PIN_ID, -1f);
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_overflow_value2() throws Exception {
    //        hcc.getPinResource().setValue(PIN_ID, 2f);
    //    }

}
