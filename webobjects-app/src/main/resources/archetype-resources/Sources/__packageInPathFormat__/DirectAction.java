#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WORequest;

import er.extensions.appserver.ERXDirectAction;

import ${package}.components.Main;

public class DirectAction extends ERXDirectAction {
	public DirectAction(final WORequest request) {
		super(request);
	}

	@Override
	public WOActionResults defaultAction() {
		return pageWithName(Main.class.getName());
	}
	
	public Application application() {
		return (Application) WOApplication.application();
	}
	
	@Override
	public Session session() {
		return (Session) super.session();
	}
}
