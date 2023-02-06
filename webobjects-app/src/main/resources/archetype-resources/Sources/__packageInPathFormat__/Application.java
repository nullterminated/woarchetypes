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
		ERXPatcher.setClassForName(Main.class, Main.class.getSimpleName());

		NSNotificationCenter.defaultCenter().addObserver(this,
				new NSSelector<Void>("willTerminate", ERXConstant.NotificationClassArray),
				ERXApplication.ApplicationWillTerminateNotification, null);
	}

	public void willTerminate(final NSNotification n) {
		// Put code to terminate gracefully here
	}
}
