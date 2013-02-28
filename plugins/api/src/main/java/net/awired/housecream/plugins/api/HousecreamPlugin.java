package net.awired.housecream.plugins.api;

import java.net.URI;
import javax.validation.ValidationException;
import org.apache.camel.Message;

public interface HousecreamPlugin {

    String scheme();

    //TODO MOVE
    //
    //    Float getCurrentValue(Point point, CamelContext camelContext);
    //

    boolean isCommand();

    /**
     * @return can return null if valid.
     */
    URI validateAndNormalizeUri(URI pointUri) throws ValidationException;

    Float readInValue(Message in) throws Exception;

}
