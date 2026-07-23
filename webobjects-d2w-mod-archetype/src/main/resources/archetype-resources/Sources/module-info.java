#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
module ${artifactId} {
	exports ${package};
	exports ${package}.components;

	requires org.slf4j;
	requires org.wocommunity.webobjects.directtoweb;
	requires org.wocommunity.webobjects.dtwgeneration;
	requires org.wocommunity.webobjects.eocontrol;
	requires org.wocommunity.webobjects.foundation;
	requires org.wocommunity.webobjects.webobjects;
	requires org.wocommunity.wonder.directtoweb;
	requires org.wocommunity.wonder.erextensions;
}