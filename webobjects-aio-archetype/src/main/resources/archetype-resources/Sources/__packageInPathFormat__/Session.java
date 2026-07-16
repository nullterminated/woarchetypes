#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import er.extensions.appserver.ERXSession;

public class Session extends ERXSession {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(Session.class);

	public Session() {
		LOG.debug("Session created: " + sessionID());

		setStoresIDsInCookies(false);
		setStoresIDsInURLs(true);
		_javaScriptEnabled = Boolean.FALSE;
	}
}
