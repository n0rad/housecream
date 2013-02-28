package net.awired.housecream.plugins.api;

import java.net.URI;
import javax.validation.ValidationException;

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

}
