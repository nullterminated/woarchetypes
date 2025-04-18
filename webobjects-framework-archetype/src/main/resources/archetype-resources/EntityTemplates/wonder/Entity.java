#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound}if (${symbol_dollar}entity.packageName)
package ${symbol_dollar}entity.packageName;

${symbol_pound}end
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public${symbol_pound}if (${symbol_dollar}{entity.abstractEntity}) abstract${symbol_pound}end class ${symbol_dollar}{entity.classNameWithoutPackage} extends ${symbol_dollar}{entity.prefixClassNameWithOptionalPackage} {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(${symbol_dollar}{entity.classNameWithoutPackage}.class);
}
