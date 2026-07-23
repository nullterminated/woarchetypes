#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
module ${package}.${artifactId} {
	exports ${package};
	exports ${package}.components;
	exports ${package}.model.eogen;
	exports ${package}.model;

	requires org.slf4j;
	requires org.wocommunity.webobjects.eoaccess;
	requires org.wocommunity.webobjects.eocontrol;
	requires org.wocommunity.webobjects.foundation;
	requires org.wocommunity.webobjects.webobjects;
	requires org.wocommunity.wonder.erextensions;
}
