package net.awired.housecream.client.it.input.digital;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.domain.pin.HccCondition;
import net.awired.housecream.client.common.domain.pin.HccNotify;
import net.awired.housecream.client.common.domain.pin.HccPin;
import net.awired.housecream.client.common.domain.pin.HccPinInfo;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.test.DefaultITDomainHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceInputDigitalInfoIT {
    private static final int PIN_ID = 5;

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

    @Test
    public void should_empty_start_val() throws Exception {
        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
        pin.setStartVal(null);

        hcc.getPinResource().setPinInfo(PIN_ID, pin);

        assertNull(hcc.getPinResource().getPinInfo(PIN_ID).getStartVal());
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

        assertEquals(pin.getNotifies(), hcc.getPinResource().getPinInfo(PIN_ID).getNotifies());
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
