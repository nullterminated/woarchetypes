#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.components;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.appserver.WOResponse;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WPage;
import com.webobjects.directtoweb.ERD2WContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.appserver.navigation.ERXNavigationManager;
import er.extensions.components.ERXStatelessComponent;
import er.extensions.localization.ERXLocalizer;
import er.r2d2w.ERR2d2wUtils;

public class PageWrapper extends ERXStatelessComponent {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	private static final NSArray<String> availableTimeZones = new NSArray<>(new String[] { "US/Hawaii", "US/Alaska",
			"US/Pacific", "US/Arizona", "US/Mountain", "US/Central", "US/Eastern", "GMT", "Asia/Tokyo" });

	public PageWrapper(final WOContext aContext) {
		super(aContext);
	}

	@Override
	public void appendToResponse(final WOResponse response, final WOContext context) {
		super.appendToResponse(response, context);
		if (ERR2d2wUtils.acceptsXHTML(context().request())) {
			ERR2d2wUtils.setXHTMLContentType(response);
		}
	}

	/**
	 * @return the availableTimeZones
	 */
	public NSArray<String> availableTimeZones() {
		return availableTimeZones;
	}

	public D2WContext d2wContext() {
		if (context().page() instanceof D2WPage) {
			final D2WPage d2wPage = (D2WPage) context().page();
			return d2wPage.d2wContext();
		}
		return null;
	}

	public boolean hasMultipleLanguages() {
		return ERXLocalizer.availableLanguages().count() > 1;
	}

	public String iconsURL() {
		final WOResourceManager rm = WOApplication.application().resourceManager();
		final NSArray<String> languages = new NSArray<>(localizer().language());
		return rm.pathURLForResourceNamed("img/icons.svg", "ERR2d2w", languages).toExternalForm();
	}

	public NSKeyValueCoding navigationContext() {
		NSKeyValueCoding _navigationContext = null;

		if (context().page() instanceof D2WPage) {
			_navigationContext = d2wContext();
		}

		if (_navigationContext == null) {
			_navigationContext = ERD2WContext.newContext(session());
		}

		ERXNavigationManager.manager().navigationStateForSession(session());
		return _navigationContext;
	}
}
