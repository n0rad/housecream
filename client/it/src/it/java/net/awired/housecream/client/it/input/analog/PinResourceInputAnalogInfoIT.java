package net.awired.housecream.client.it.input.analog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.domain.HccCondition;
import net.awired.housecream.client.common.domain.HccNotify;
import net.awired.housecream.client.common.domain.HccPin;
import net.awired.housecream.client.common.domain.HccPinInfo;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.test.DefaultITDomainHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceInputAnalogInfoIT {
    private static final int PIN_ID = 2;

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test
    public void should_update_pin() throws Exception {
        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
        pin.setName("name");
        pin.setDescription("desc");
        pin.addNotify(new HccNotify(HccCondition.inf_or_equal, 0));

        hcc.getPinResource().setPinInfo(PIN_ID, pin);

        HccPinInfo pinInfo = hcc.getPinResource().getPinInfo(PIN_ID);
        assertTrue(EqualsBuilder.reflectionEquals(pin, pinInfo));
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_null_name() throws Exception {
        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
        pin.setName(null);

        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_empty_name() throws Exception {
        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
        pin.setName("");

        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_null_description() throws Exception {
        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
        pin.setDescription(null);

        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_empty_description() throws Exception {
        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
        pin.setDescription("");

        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_set_start_val() throws Exception {
        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
        pin.setStartVal(0f);

        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    }

    @Test
    public void should_empty_notify() throws Exception {
        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
        pin.setNotifies(null);

        hcc.getPinResource().setPinInfo(PIN_ID, pin);

        assertNull(hcc.getPinResource().getPinInfo(PIN_ID).getNotifies());
    }

    @Test
    public void should_update_notify() throws Exception {
        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
        HccNotify notify = new HccNotify(HccCondition.sup_or_equal, 1);
        pin.addNotify(notify);

        hcc.getPinResource().setPinInfo(PIN_ID, pin);

        List<HccNotify> notifies = hcc.getPinResource().getPinInfo(PIN_ID).getNotifies();
        assertEquals(pin.getNotifies(), notifies);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_notify_for_overflow() throws Exception {
        HccPin pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID);
        HccPinInfo pinInfo = pin.getInfo();
        HccNotify notify = new HccNotify(HccCondition.inf_or_equal, pin.getDescription().getValueMin() - 1);
        pinInfo.addNotify(notify);

        hcc.getPinResource().setPinInfo(PIN_ID, pinInfo);
    }

    @Test(expected = HccUpdateException.class)
    public void should_not_notify_for_overflow2() throws Exception {
        HccPin pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID);
        HccPinInfo pinInfo = pin.getInfo();
        HccNotify notify = new HccNotify(HccCondition.sup_or_equal, pin.getDescription().getValueMax() + 1);
        pinInfo.addNotify(notify);

        hcc.getPinResource().setPinInfo(PIN_ID, pinInfo);
    }

}
