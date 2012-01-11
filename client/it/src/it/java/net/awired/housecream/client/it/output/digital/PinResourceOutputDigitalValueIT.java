package net.awired.housecream.client.it.output.digital;

import static org.junit.Assert.assertEquals;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.resource.HccUpdateException;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceOutputDigitalValueIT {

    private static final int PIN_ID = 4;

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test
    public void should_get_value() throws Exception {
        assertEquals((Integer) 1, hcc.getPinResource().getValue(PIN_ID));
    }

    @Test
    public void should_set_value() throws Exception {
        hcc.getPinResource().setValue(PIN_ID, 0);

        assertEquals((Integer) 0, hcc.getPinResource().getValue(PIN_ID));
    }

    @Test
    public void should_set_value2() throws Exception {
        hcc.getPinResource().setValue(PIN_ID, 1);

        assertEquals((Integer) 1, hcc.getPinResource().getValue(PIN_ID));
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_set_null_value() throws Exception {
        hcc.getPinResource().setValue(PIN_ID, null);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_overflow_value() throws Exception {
        hcc.getPinResource().setValue(PIN_ID, -1);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_overflow_value2() throws Exception {
        hcc.getPinResource().setValue(PIN_ID, 2);
    }

}
