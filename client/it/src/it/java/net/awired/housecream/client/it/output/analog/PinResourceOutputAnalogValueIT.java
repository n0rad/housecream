package net.awired.housecream.client.it.output.analog;

import static org.junit.Assert.assertEquals;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.domain.HccPin;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.test.DefaultTestDomainHelper;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceOutputAnalogValueIT {

    private static final int PIN_ID = 3;

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test
    public void should_get_value() throws Exception {
        assertEquals((Integer) 60, hcc.getPinResource().getValue(PIN_ID));
    }

    @Test
    public void should_set_value() throws Exception {
        hcc.getPinResource().setValue(PIN_ID, 0);

        assertEquals((Integer) 0, hcc.getPinResource().getValue(PIN_ID));
    }

    @Test
    public void should_set_value2() throws Exception {
        hcc.getPinResource().setValue(PIN_ID, 40);

        assertEquals((Integer) 40, hcc.getPinResource().getValue(PIN_ID));
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_set_null_value() throws Exception {
        hcc.getPinResource().setValue(PIN_ID, null);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_overflow_value() throws Exception {
        HccPin pin = DefaultTestDomainHelper.buildDefaultPin(PIN_ID);

        hcc.getPinResource().setValue(PIN_ID, pin.getDescription().getValueMax() + 1);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_overflow_value2() throws Exception {
        HccPin pin = DefaultTestDomainHelper.buildDefaultPin(PIN_ID);

        hcc.getPinResource().setValue(PIN_ID, pin.getDescription().getValueMin() - 1);
    }

}
