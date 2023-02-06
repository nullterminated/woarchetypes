#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.components;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;
import com.webobjects.appserver.WOActionResults;

public class Main extends ERXComponent {
	/**
	 * Default serial version id
	 */
	private static final long serialVersionUID = 1L;
	private String formText;

	public Main(WOContext context) {
		super(context);
	}

	/**
	 * @return the formText
	 */
	public String formText() {
		return formText;
	}

	/**
	 * @param formText the formText to set
	 */
	public void setFormText(String formText) {
		this.formText = formText;
	}

	public WOActionResults echo() {
		return null;
	}
}
