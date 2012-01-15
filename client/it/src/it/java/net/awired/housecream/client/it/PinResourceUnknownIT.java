package net.awired.housecream.client.it;

import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.domain.HccPinInfo;
import net.awired.housecream.client.common.resource.PinNotFoundException;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceUnknownIT {

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin() throws Exception {
        hcc.getPinResource().getPinDescription(42);
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin2() throws Exception {
        hcc.getPinResource().getPinDescription(-1);
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin3() throws Exception {
        hcc.getPinResource().getPinInfo(42);
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin4() throws Exception {
        hcc.getPinResource().getPinInfo(-1);
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin5() throws Exception {
        hcc.getPinResource().getValue(42);
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin6() throws Exception {
        hcc.getPinResource().getValue(-1);
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin13() throws Exception {
        hcc.getPinResource().setPinInfo(42, new HccPinInfo());
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin14() throws Exception {
        hcc.getPinResource().setPinInfo(-1, new HccPinInfo());
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin15() throws Exception {
        hcc.getPinResource().setValue(42, 0f);
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin16() throws Exception {
        hcc.getPinResource().setValue(-1, 0f);
    }

}
