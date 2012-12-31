package net.awired.housecream.server.engine;

import java.net.URI;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;

public interface InPointDaoInterface {

    InPoint findFromUriStart(URI uri) throws NotFoundException;

}
