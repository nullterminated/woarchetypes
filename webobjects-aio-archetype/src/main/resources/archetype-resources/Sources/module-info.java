#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
open module ${artifactId} {
	requires com.h2database;
	requires org.slf4j;
	requires org.apache.logging.log4j.core;
	requires transitive org.wocommunity.webobjects.foundation;
	requires transitive org.wocommunity.webobjects.eocontrol;
	requires transitive org.wocommunity.webobjects.eoaccess;
	requires transitive org.wocommunity.webobjects.webobjects;
	requires transitive org.wocommunity.webobjects.jdbcadaptor;
	requires org.wocommunity.webobjects.woextensions;
	requires org.wocommunity.webobjects.dtwgeneration;
	requires org.wocommunity.webobjects.eoproject;
	requires transitive org.wocommunity.webobjects.directtoweb;
	requires org.wocommunity.wonder.erextensions;
	requires org.wocommunity.wonder.directtoweb;
	requires org.wocommunity.wonder.err2d2w;
	requires org.wocommunity.wonder.vertxwoadaptor;
	requires org.wocommunity.wonder.erpersistentsessionstorage;
	requires org.wocommunity.wonder.jdbcadaptor.h2;
	requires org.wocommunity.wonder.erattachment;
	
	exports ${package};
	exports ${package}.components;
}