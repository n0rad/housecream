package net.awired.housecream.client.it.output.analog;

import net.awired.housecream.client.HccTestRule;
import org.junit.Rule;

public class PinResourceOutputAnalogInfoIT {

    private static final int PIN_ID = 3;

    @Rule
    public HccTestRule hcc = new HccTestRule();
    //
    //    @Test
    //    public void should_update_pin() throws Exception {
    //        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
    //        pin.setName("name");
    //        pin.setDescription("desc");
    //        pin.setStartVal(60f);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    //
    //        HccPinInfo pinInfo = hcc.getPinResource().getPinInfo(PIN_ID);
    //        assertTrue(EqualsBuilder.reflectionEquals(pin, pinInfo));
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_null_name() throws Exception {
    //        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
    //        pin.setName(null);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_empty_name() throws Exception {
    //        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
    //        pin.setName("");
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_null_description() throws Exception {
    //        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
    //        pin.setDescription(null);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_empty_description() throws Exception {
    //        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
    //        pin.setDescription("");
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_empty_start_val() throws Exception {
    //        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
    //        pin.setStartVal(null);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    //    }
    //
    //    @Test
    //    public void should_empty_notify() throws Exception {
    //        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
    //        pin.setNotifies(null);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    //
    //        assertNull(hcc.getPinResource().getPinInfo(PIN_ID).getNotifies());
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_update_notify() throws Exception {
    //        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
    //        HccPinNotify notify = new HccPinNotify(HccPinNotifyCondition.sup_or_equal, 1);
    //        pin.addNotify(notify);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    //    }
    //
    //    @Test
    //    public void should_set_start_val() throws Exception {
    //        HccPinInfo pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID).getInfo();
    //        pin.setStartVal(70f);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pin);
    //
    //        assertEquals((Float) 70f, hcc.getPinResource().getPinInfo(PIN_ID).getStartVal());
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_invalid_start_val() throws Exception {
    //        HccPin pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID);
    //        HccPinInfo pinInfo = pin.getInfo();
    //        pinInfo.setStartVal(pin.getDescription().getValueMin() - 1);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pinInfo);
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_invalid_start_val2() throws Exception {
    //        HccPin pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID);
    //        HccPinInfo pinInfo = pin.getInfo();
    //        pinInfo.setStartVal(pin.getDescription().getValueMax() + 1);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pinInfo);
    //    }
    //
    //    @Test(expected = HccUpdateException.class)
    //    public void should_not_valid_start_val_using_step() throws Exception {
    //        HccPin pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID);
    //        HccPinInfo pinInfo = pin.getInfo();
    //        pinInfo.setStartVal(1f);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pinInfo);
    //    }
    //
    //    @Test
    //    public void should_valid_start_val_using_step() throws Exception {
    //        HccPin pin = DefaultITDomainHelper.buildDefaultPin(PIN_ID);
    //        HccPinInfo pinInfo = pin.getInfo();
    //        pinInfo.setStartVal(2f);
    //
    //        hcc.getPinResource().setPinInfo(PIN_ID, pinInfo);
    //
    //        assertEquals((Float) 2f, hcc.getPinResource().getPinInfo(PIN_ID).getStartVal());
    //    }

}
