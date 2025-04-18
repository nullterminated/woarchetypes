#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.components;

import com.webobjects.appserver.WOContext;
import er.extensions.components.ERXComponent;

public class MyComponent extends ERXComponent {
    public MyComponent(WOContext context) {
        super(context);
    }
}