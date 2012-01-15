package net.awired.housecream.client.it.reserved;

import static org.junit.Assert.assertNull;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.resource.HccUpdateException;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceReservedValueIT {

    private static final int PIN_ID = 0;

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test
    public void should_not_get_value() throws Exception {
        Float pinValue = hcc.getPinResource().getValue(PIN_ID);

        assertNull(pinValue);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_set_value() throws Exception {
        hcc.getPinResource().setValue(PIN_ID, 1f);
    }

}
