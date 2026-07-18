#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

import er.extensions.appserver.ERXApplication;
import er.extensions.eof.ERXConstant;
import er.extensions.foundation.ERXPatcher;
import ${package}.components.Main;

public class Application extends ERXApplication {
	public static void main(final String[] argv) {
		ERXApplication.main(argv, Application.class);
	}

	public Application() {
		ERXApplication.log.info("Welcome to " + name() + " !");
		/* ** put your initialization code in here ** */
		setAllowsConcurrentRequestHandling(true);
	}

	@Override
	protected Class<Session> _sessionClass() {
		return Session.class;
	}

	@Override
	public void finishInitialization() {
		super.finishInitialization();

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
		
		ERXPatcher.setClassForName(Main.class, Main.class.getSimpleName());

		NSNotificationCenter.defaultCenter().addObserver(this,
				new NSSelector<Void>("willTerminate", ERXConstant.NotificationClassArray),
				ERXApplication.ApplicationWillTerminateNotification, null);
	}

	public void willTerminate(final NSNotification n) {
		// Put code to terminate gracefully here
	}
}
