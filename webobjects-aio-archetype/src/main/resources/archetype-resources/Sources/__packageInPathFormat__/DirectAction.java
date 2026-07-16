#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.webobjects.appserver.WORequest;

import er.directtoweb.ERD2WDirectAction;

public class DirectAction extends ERD2WDirectAction {
	public DirectAction(WORequest request) {
		super(request);
	}

	/**
	 * Checks if a page configuration is allowed to render.
	 *
	 * @param pageConfiguration
	 */
	protected boolean allowPageConfiguration(String pageConfiguration) {
		return true;
	}
}
