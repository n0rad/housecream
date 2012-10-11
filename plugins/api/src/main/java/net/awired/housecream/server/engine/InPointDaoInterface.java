package net.awired.housecream.server.engine;

import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;

public interface InPointDaoInterface {

    InPoint findFromUrl(String url) throws NotFoundException;

}
