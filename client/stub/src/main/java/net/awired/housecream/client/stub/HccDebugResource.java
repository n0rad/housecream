package net.awired.housecream.client.stub;

import net.awired.housecream.client.common.test.DefaultTestDebugResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HccDebugResource implements DefaultTestDebugResource {

    @Autowired
    private HccContext context;

    @Override
    public void setDebugValue(int pinId, Float value) {
        //        context.setdebugValue(pinId, value);
    }

}
