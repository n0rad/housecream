/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.application;

import java.util.Arrays;
import javax.ws.rs.Path;
import org.springframework.stereotype.Service;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.parser.ModelJacksonParser;
import fr.norad.jaxrs.doc.resource.GenericDocService;

@Service
@Path("/")
public class DocService extends GenericDocService {

    private final DocConfig config;

    public DocService() {
        config = new DocConfig(Arrays.asList("org.housecream"));
        config.setModelParser(new ModelJacksonParser(config));
    }

    @Override
    protected DocConfig getDocConfig() {
        return config;
    }

}
