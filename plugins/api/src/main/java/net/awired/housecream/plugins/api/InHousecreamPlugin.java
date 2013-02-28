package net.awired.housecream.plugins.api;

import org.apache.camel.Message;

public interface InHousecreamPlugin extends HousecreamPlugin {

    //    String getInFormDirective();

    Float readInValue(Message in) throws Exception;

}
