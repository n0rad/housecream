package net.awired.agse.web2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.RequestToViewNameTranslator;

/**
 * Handles requests for the application welcome page.
 */
@Controller
public class WelcomeController {

	private static Log logger = LogFactory.getLog(WelcomeController.class);
	
	/**
	 * Simply selects the welcome view to render by returning void and relying
	 * on the default {@link RequestToViewNameTranslator}.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void welcome() {
		if (logger.isDebugEnabled()) {
			logger.debug("WelcomeController invoked");
		}		
	}
}
