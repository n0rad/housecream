package net.awired.housecream.client.it;

import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.domain.pin.HccPin;
import net.awired.housecream.client.common.resource.HccPinNotFoundException;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceUnknownIT {

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin() throws Exception {
        hcc.getPinResource().getPin(42);
    }

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin2() throws Exception {
        hcc.getPinResource().getPin(-1);
    }

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin3() throws Exception {
        hcc.getPinResource().getPin(42);
    }

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin4() throws Exception {
        hcc.getPinResource().getPin(-1);
    }

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin5() throws Exception {
        hcc.getPinResource().getPinValue(42);
    }

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin6() throws Exception {
        hcc.getPinResource().getPinValue(-1);
    }

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin13() throws Exception {
        hcc.getPinResource().setPin(42, new HccPin());
    }

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin14() throws Exception {
        hcc.getPinResource().setPin(-1, new HccPin());
    }

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin15() throws Exception {
        hcc.getPinResource().setPinValue(42, 0f);
    }

    @Test(expected = HccPinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin16() throws Exception {
        hcc.getPinResource().setPinValue(-1, 0f);
    }

}
