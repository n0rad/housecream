package net.awired.housecream.client.it;

import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.common.resource.PinNotFoundException;
import org.junit.Rule;
import org.junit.Test;

public class PinResourceUnknownIT {

    @Rule
    public HccTestRule hcc = new HccTestRule();

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin() throws Exception {
        hcc.getPinResource().getPin(42);
    }

    @Test(expected = PinNotFoundException.class)
    public void should_throw_exception_on_unknown_pin2() throws Exception {
        hcc.getPinResource().getPin(-1);
    }

}
