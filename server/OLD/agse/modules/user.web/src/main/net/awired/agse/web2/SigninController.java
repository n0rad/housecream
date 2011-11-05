package net.awired.agse.web2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.RequestToViewNameTranslator;

/**
 * Handles requests to sign a user in.
 */
@Controller
public class SigninController {

	private static Log logger = LogFactory.getLog(SigninController.class);

	/**
	 * Simply selects the login view to render by returning void and relying on
	 * the default {@link RequestToViewNameTranslator}.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void signin() {
		if (logger.isDebugEnabled()) {
			logger.debug("SigninController invoked");
		}		
	}
}
