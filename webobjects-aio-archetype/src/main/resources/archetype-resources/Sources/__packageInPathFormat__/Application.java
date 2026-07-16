#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.util.TimeZone;

import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WORequestHandler;
import com.webobjects.foundation.NSTimeZone;

import er.extensions.appserver.ERXApplication;
import er.extensions.appserver.navigation.ERXNavigationManager;

public class Application extends ERXApplication {

	static {
		// Set default time zones
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		NSTimeZone.setDefaultTimeZone(NSTimeZone.getGMT());
	}

	private static Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(final String argv[]) {
		ERXApplication.main(argv, Application.class);
	}

	@Override
	public void didFinishLaunching() {
		/* ** put your post initialization code in here ** */
		LOG.info("Welcome to ${artifactId}!");
	}

	@Override
	public void finishInitialization() {
		/*
		 * log4j2 reads its config out of log4j2.properties instead of the system
		 * properties. The pom file places our Resources/log4j2.properties in the root
		 * of target/classes so it will work. However, we want to read our logger
		 * pattern from the wonder properties, so we reference a system property in the
		 * log4j2.properties for the appender pattern. Unfortunately because of load
		 * order, log4j2.properties are loaded before Wonder's ERXProperties. Therefore,
		 * we call reconfigure here to reset the logging properties after we finish
		 * loading everything in wonder.
		 */
		LoggerContext.getContext(false).reconfigure();
		LOG.info("logger reconfigured");
		
		// Initialize the navigation manager
		ERXNavigationManager.manager().configureNavigation();

		// Set the default request handler to the direct action request handler
		final String key = directActionRequestHandlerKey();
		final WORequestHandler handler = requestHandlerForKey(key);
		setDefaultRequestHandler(handler);
	}

}
