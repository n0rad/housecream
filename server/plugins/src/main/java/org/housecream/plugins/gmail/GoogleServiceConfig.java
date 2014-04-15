package org.housecream.plugins.gmail;

import org.housecream.server.api.domain.config.Config;
import fr.norad.jaxrs.doc.api.Description;
import lombok.Getter;

@Getter
public class GoogleServiceConfig extends Config {

    @Description("Housecream instance's Client id declared on Google to access Google services")
    private String googleServiceClientId;
    @Description("Housecream instance's Client secret declared on Google to access Google services")
    private String googleServiceClientSecret;

}
