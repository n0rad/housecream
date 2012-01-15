package net.awired.housecream.client.it.notused;

import static org.junit.Assert.assertNull;
import java.util.Arrays;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.domain.HccCondition;
import net.awired.housecream.client.common.domain.HccNotify;
import net.awired.housecream.client.common.domain.HccPinInfo;
import net.awired.housecream.client.common.resource.HccUpdateException;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceNotUsedInfoIT {

    private static final int PIN_ID = 1;

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test
    public void should_get_null_info() throws Exception {

        HccPinInfo pin = hcc.getPinResource().getPinInfo(PIN_ID);

        assertNull(pin);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_name() throws Exception {
        HccPinInfo pin = new HccPinInfo();
        pin.setName("name");

        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_description() throws Exception {
        HccPinInfo pin = new HccPinInfo();
        pin.setDescription("desc");

        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_notify() throws Exception {
        HccPinInfo pin = new HccPinInfo();
        pin.setNotifies(Arrays.asList(new HccNotify(HccCondition.inf_or_equal, 0)));

        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_update_start_val() throws Exception {
        HccPinInfo pin = new HccPinInfo();
        pin.setStartVal(0f);

        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    }

}
