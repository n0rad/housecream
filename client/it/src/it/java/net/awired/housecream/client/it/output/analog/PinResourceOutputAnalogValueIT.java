package net.awired.housecream.client.it.output.analog;

import net.awired.housecream.client.HccTestRule;
import org.junit.Rule;

public class PinResourceOutputAnalogValueIT {

    private static final int PIN_ID = 3;

    @Rule
    public HccTestRule hcc = new HccTestRule();
    //
    //    @Test
    //    public void should_get_value() throws Exception {
    //        assertEquals((Float) 60f, hcc.getPinResource().getValue(PIN_ID));
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
    //        hcc.getPinResource().setValue(PIN_ID, 40f);
    //
    //        assertEquals((Float) 40f, hcc.getPinResource().getValue(PIN_ID));
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_overflow_value() throws Exception {
    //        HccPin pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID);
    //
    //        hcc.getPinResource().setValue(PIN_ID, pin.getDescription().getValueMax() + 1);
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_overflow_value2() throws Exception {
    //        HccPin pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID);
    //
    //        hcc.getPinResource().setValue(PIN_ID, pin.getDescription().getValueMin() - 1);
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_set_value_using_step() throws Exception {
    //        hcc.getPinResource().setValue(PIN_ID, 1f);
    //    }
    //
    //    public void should_set_value_using_step() throws Exception {
    //        hcc.getPinResource().setValue(PIN_ID, 2f);
    //
    //        assertEquals((Float) 2f, hcc.getPinResource().getValue(PIN_ID));
    //    }

}
