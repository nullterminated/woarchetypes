#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import er.extensions.ERXFrameworkPrincipal;

public class ${artifactId} extends ERXFrameworkPrincipal {
	protected static ${artifactId} sharedInstance;
	@SuppressWarnings("unchecked")
	public final static Class<? extends ERXFrameworkPrincipal> REQUIRES[] = new Class[] {};

	static {
		setUpFrameworkPrincipalClass(${artifactId}.class);
	}

	public static ${artifactId} sharedInstance() {
		if (sharedInstance == null) {
			sharedInstance = sharedInstance(${artifactId}.class);
		}
		return sharedInstance;
	}

	@Override
	public void finishInitialization() {
		log.debug("${artifactId} loaded");
	}
}
