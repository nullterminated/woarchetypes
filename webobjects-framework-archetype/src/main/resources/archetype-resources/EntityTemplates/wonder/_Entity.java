#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
// DO NOT EDIT!  Make changes to ${symbol_dollar}{entity.classNameWithOptionalPackage}.java instead.
${symbol_pound}if (${symbol_dollar}entity.superclassPackageName)
package ${symbol_dollar}entity.superclassPackageName;

${symbol_pound}end
import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;

import er.extensions.eof.*;
import er.extensions.eof.ERXKey.Type;
import er.extensions.foundation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class ${symbol_dollar}{entity.prefixClassNameWithoutPackage} extends ${symbol_pound}if (${symbol_dollar}entity.parentClassNameSet)${symbol_dollar}{entity.parentClassName}${symbol_pound}elseif (${symbol_dollar}entity.partialEntitySet)er.extensions.partials.ERXPartial<${symbol_dollar}{entity.partialEntity.className}>${symbol_pound}elseif (${symbol_dollar}entity.parentSet)${symbol_dollar}{entity.parent.classNameWithDefault}${symbol_pound}elseif (${symbol_dollar}EOGenericRecord)${symbol_dollar}{EOGenericRecord}${symbol_pound}else ERXGenericRecord${symbol_pound}end {
${symbol_pound}if (${symbol_dollar}entity.partialEntitySet)
  public static final String ENTITY_NAME = "${symbol_dollar}entity.partialEntity.name";
${symbol_pound}else
  public static final String ENTITY_NAME = "${symbol_dollar}entity.name";
${symbol_pound}end

  // Attribute Keys
${symbol_pound}foreach (${symbol_dollar}attribute in ${symbol_dollar}entity.sortedClassAttributes)
${symbol_pound}if (!${symbol_dollar}attribute.inherited)
  public static final ERXKey<${symbol_dollar}attribute.javaClassName> ${symbol_dollar}{attribute.uppercaseUnderscoreName} = new ERXKey<${symbol_dollar}attribute.javaClassName>("${symbol_dollar}attribute.name", Type.Attribute);
${symbol_pound}end
${symbol_pound}end

  // Relationship Keys
${symbol_pound}foreach (${symbol_dollar}relationship in ${symbol_dollar}entity.sortedClassRelationships)
${symbol_pound}if (!${symbol_dollar}relationship.inherited)
  public static final ERXKey<${symbol_dollar}relationship.actualDestination.classNameWithDefault> ${symbol_dollar}{relationship.uppercaseUnderscoreName} = new ERXKey<${symbol_dollar}relationship.actualDestination.classNameWithDefault>("${symbol_dollar}relationship.name",${symbol_pound}if(${symbol_dollar}relationship.toMany) Type.ToManyRelationship${symbol_pound}else Type.ToOneRelationship${symbol_pound}end);
${symbol_pound}end
${symbol_pound}end

  // Attributes
${symbol_pound}foreach (${symbol_dollar}attribute in ${symbol_dollar}entity.sortedClassAttributes)
${symbol_pound}if (!${symbol_dollar}attribute.inherited)
  public static final String ${symbol_dollar}{attribute.uppercaseUnderscoreName}_KEY = ${symbol_dollar}{attribute.uppercaseUnderscoreName}.key();
${symbol_pound}end
${symbol_pound}end

  // Relationships
${symbol_pound}foreach (${symbol_dollar}relationship in ${symbol_dollar}entity.sortedClassRelationships)
${symbol_pound}if (!${symbol_dollar}relationship.inherited)
  public static final String ${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY = ${symbol_dollar}{relationship.uppercaseUnderscoreName}.key();
${symbol_pound}end
${symbol_pound}end

  private static final Logger log = LoggerFactory.getLogger(${symbol_dollar}{entity.prefixClassNameWithoutPackage}.class);

${symbol_pound}if (!${symbol_dollar}entity.partialEntitySet)
${symbol_pound}if (${symbol_dollar}entity.parentSet)
  @Override
${symbol_pound}end
  public ${symbol_dollar}entity.classNameWithOptionalPackage localInstanceIn(EOEditingContext editingContext) {
    ${symbol_dollar}entity.classNameWithOptionalPackage localInstance = (${symbol_dollar}entity.classNameWithOptionalPackage)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

${symbol_pound}end
${symbol_pound}foreach (${symbol_dollar}attribute in ${symbol_dollar}entity.sortedClassAttributes)
${symbol_pound}if (!${symbol_dollar}attribute.inherited)
${symbol_pound}if (${symbol_dollar}attribute.userInfo.ERXConstantClassName)
  public ${symbol_dollar}attribute.userInfo.ERXConstantClassName ${symbol_dollar}{attribute.name}() {
    Number value = (Number)storedValueForKey(${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{attribute.uppercaseUnderscoreName}_KEY);
    return (${symbol_dollar}attribute.userInfo.ERXConstantClassName)value;
  }

  public void set${symbol_dollar}{attribute.capitalizedName}(${symbol_dollar}attribute.userInfo.ERXConstantClassName value) {
    log.debug( "updating ${symbol_dollar}attribute.name from {} to {}", ${symbol_dollar}{attribute.name}(), value);
    takeStoredValueForKey(value, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{attribute.uppercaseUnderscoreName}_KEY);
  }
${symbol_pound}else
  public ${symbol_dollar}attribute.javaClassName ${symbol_dollar}{attribute.name}() {
    return (${symbol_dollar}attribute.javaClassName) storedValueForKey(${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{attribute.uppercaseUnderscoreName}_KEY);
  }

  public void set${symbol_dollar}{attribute.capitalizedName}(${symbol_dollar}attribute.javaClassName value) {
    log.debug( "updating ${symbol_dollar}attribute.name from {} to {}", ${symbol_dollar}{attribute.name}(), value);
    takeStoredValueForKey(value, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{attribute.uppercaseUnderscoreName}_KEY);
  }
${symbol_pound}end

${symbol_pound}end
${symbol_pound}end
${symbol_pound}foreach (${symbol_dollar}relationship in ${symbol_dollar}entity.sortedClassToOneRelationships)
${symbol_pound}if (!${symbol_dollar}relationship.inherited)
  public ${symbol_dollar}relationship.actualDestination.classNameWithDefault ${symbol_dollar}{relationship.name}() {
    return (${symbol_dollar}relationship.actualDestination.classNameWithDefault)storedValueForKey(${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
  }

  public void set${symbol_dollar}{relationship.capitalizedName}(${symbol_dollar}relationship.actualDestination.classNameWithDefault value) {
    takeStoredValueForKey(value, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
  }

  public void set${symbol_dollar}{relationship.capitalizedName}Relationship(${symbol_dollar}relationship.actualDestination.classNameWithDefault value) {
    log.debug("updating ${symbol_dollar}relationship.name from {} to {}", ${symbol_dollar}{relationship.name}(), value);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      set${symbol_dollar}{relationship.capitalizedName}(value);
    }
    else if (value == null) {
      ${symbol_dollar}relationship.actualDestination.classNameWithDefault oldValue = ${symbol_dollar}{relationship.name}();
      if (oldValue != null) {
        removeObjectFromBothSidesOfRelationshipWithKey(oldValue, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
      }
    } else {
      addObjectToBothSidesOfRelationshipWithKey(value, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
    }
  }

${symbol_pound}end
${symbol_pound}end
${symbol_pound}foreach (${symbol_dollar}relationship in ${symbol_dollar}entity.sortedClassToManyRelationships)
${symbol_pound}if (!${symbol_dollar}relationship.inherited)
  public NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}> ${symbol_dollar}{relationship.name}() {
    return (NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}>)storedValueForKey(${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
  }

${symbol_pound}if (!${symbol_dollar}relationship.inverseRelationship || ${symbol_dollar}relationship.flattened || !${symbol_dollar}relationship.inverseRelationship.classProperty)
  public NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}> ${symbol_dollar}{relationship.name}(EOQualifier qualifier) {
    return ${symbol_dollar}{relationship.name}(qualifier, null);
  }
${symbol_pound}else
  public NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}> ${symbol_dollar}{relationship.name}(EOQualifier qualifier) {
    return ${symbol_dollar}{relationship.name}(qualifier, null, false);
  }

  public NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}> ${symbol_dollar}{relationship.name}(EOQualifier qualifier, boolean fetch) {
    return ${symbol_dollar}{relationship.name}(qualifier, null, fetch);
  }
${symbol_pound}end

  public NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}> ${symbol_dollar}{relationship.name}(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings${symbol_pound}if (${symbol_dollar}relationship.inverseRelationship && !${symbol_dollar}relationship.flattened && ${symbol_dollar}relationship.inverseRelationship.classProperty), boolean fetch${symbol_pound}end) {
    NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}> results;
${symbol_pound}if (${symbol_dollar}relationship.inverseRelationship && !${symbol_dollar}relationship.flattened && ${symbol_dollar}relationship.inverseRelationship.classProperty)
    if (fetch) {
      EOQualifier fullQualifier;
${symbol_pound}if (${symbol_dollar}{relationship.actualDestination.genericRecord})
      EOQualifier inverseQualifier = ERXQ.equals("${symbol_dollar}{relationship.inverseRelationship.name}", this);
${symbol_pound}else
      EOQualifier inverseQualifier = ERXQ.equals(${symbol_dollar}{relationship.actualDestination.classNameWithDefault}.${symbol_dollar}{relationship.inverseRelationship.uppercaseUnderscoreName}_KEY, this);
${symbol_pound}end

      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        fullQualifier = ERXQ.and(qualifier, inverseQualifier);
      }

${symbol_pound}if (${symbol_dollar}{relationship.actualDestination.genericRecord})
      ERXFetchSpecification<${symbol_dollar}{entity.classNameWithOptionalPackage}> fetchSpec = new ERXFetchSpecification<${symbol_dollar}{entity.classNameWithOptionalPackage}>("${symbol_dollar}{relationship.actualDestination.name}", qualifier, sortOrderings);
      results = (NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}>)editingContext().objectsWithFetchSpecification(fetchSpec);
${symbol_pound}else
      results = ${symbol_dollar}{relationship.actualDestination.classNameWithDefault}.fetch${symbol_dollar}{relationship.actualDestination.pluralName}(editingContext(), fullQualifier, sortOrderings);
${symbol_pound}end
    }
    else {
${symbol_pound}end
      results = ${symbol_dollar}{relationship.name}();
      if (qualifier != null) {
        results = (NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<${symbol_dollar}{relationship.actualDestination.classNameWithDefault}>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
${symbol_pound}if (${symbol_dollar}relationship.inverseRelationship && !${symbol_dollar}relationship.flattened && ${symbol_dollar}relationship.inverseRelationship.classProperty)
    }
${symbol_pound}end
    return results;
  }

  public void addTo${symbol_dollar}{relationship.capitalizedName}(${symbol_dollar}relationship.actualDestination.classNameWithDefault object) {
    includeObjectIntoPropertyWithKey(object, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
  }

  public void removeFrom${symbol_dollar}{relationship.capitalizedName}(${symbol_dollar}relationship.actualDestination.classNameWithDefault object) {
    excludeObjectFromPropertyWithKey(object, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
  }

  public void addTo${symbol_dollar}{relationship.capitalizedName}Relationship(${symbol_dollar}relationship.actualDestination.classNameWithDefault object) {
    log.debug("adding {} to ${symbol_dollar}{relationship.name} relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      addTo${symbol_dollar}{relationship.capitalizedName}(object);
    }
    else {
      addObjectToBothSidesOfRelationshipWithKey(object, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
    }
  }

  public void removeFrom${symbol_dollar}{relationship.capitalizedName}Relationship(${symbol_dollar}relationship.actualDestination.classNameWithDefault object) {
    log.debug("removing {} from ${symbol_dollar}{relationship.name} relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      removeFrom${symbol_dollar}{relationship.capitalizedName}(object);
    }
    else {
      removeObjectFromBothSidesOfRelationshipWithKey(object, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
    }
  }

  public ${symbol_dollar}relationship.actualDestination.classNameWithDefault create${symbol_dollar}{relationship.capitalizedName}Relationship() {
    EOEnterpriseObject eo = EOUtilities.createAndInsertInstance(editingContext(), ${symbol_pound}if(${symbol_dollar}{relationship.actualDestination.genericRecord})"${symbol_dollar}{relationship.actualDestination.name}"${symbol_pound}else ${symbol_dollar}{relationship.actualDestination.classNameWithDefault}.ENTITY_NAME ${symbol_pound}end);
    addObjectToBothSidesOfRelationshipWithKey(eo, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
    return (${symbol_dollar}relationship.actualDestination.classNameWithDefault) eo;
  }

  public void delete${symbol_dollar}{relationship.capitalizedName}Relationship(${symbol_dollar}relationship.actualDestination.classNameWithDefault object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.${symbol_dollar}{relationship.uppercaseUnderscoreName}_KEY);
${symbol_pound}if (!${symbol_dollar}relationship.ownsDestination)
    editingContext().deleteObject(object);
${symbol_pound}end
  }

  public void deleteAll${symbol_dollar}{relationship.capitalizedName}Relationships() {
    Enumeration<${symbol_dollar}relationship.actualDestination.classNameWithDefault> objects = ${symbol_dollar}{relationship.name}().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      delete${symbol_dollar}{relationship.capitalizedName}Relationship(objects.nextElement());
    }
  }

${symbol_pound}end
${symbol_pound}end

  public ${symbol_pound}if (!${symbol_dollar}entity.partialEntitySet)static ${symbol_pound}end${symbol_dollar}{entity.classNameWithOptionalPackage}${symbol_pound}if (!${symbol_dollar}entity.partialEntitySet) create${symbol_pound}else init${symbol_pound}end${symbol_dollar}{entity.name}(EOEditingContext editingContext${symbol_pound}foreach (${symbol_dollar}attribute in ${symbol_dollar}entity.sortedClassAttributes)
${symbol_pound}if (!${symbol_dollar}attribute.allowsNull)
${symbol_pound}set (${symbol_dollar}restrictingQualifierKey = 'false')
${symbol_pound}foreach (${symbol_dollar}qualifierKey in ${symbol_dollar}entity.restrictingQualifierKeys)${symbol_pound}if (${symbol_dollar}attribute.name == ${symbol_dollar}qualifierKey)${symbol_pound}set (${symbol_dollar}restrictingQualifierKey = 'true')${symbol_pound}end${symbol_pound}end
${symbol_pound}if (${symbol_dollar}restrictingQualifierKey == 'false')
${symbol_pound}if (${symbol_dollar}attribute.userInfo.ERXConstantClassName), ${symbol_dollar}{attribute.userInfo.ERXConstantClassName}${symbol_pound}else, ${symbol_dollar}{attribute.javaClassName}${symbol_pound}end ${symbol_dollar}{attribute.name}
${symbol_pound}end
${symbol_pound}end
${symbol_pound}end
${symbol_pound}foreach (${symbol_dollar}relationship in ${symbol_dollar}entity.sortedClassToOneRelationships)
${symbol_pound}if (${symbol_dollar}relationship.mandatory && !(${symbol_dollar}relationship.ownsDestination && ${symbol_dollar}relationship.propagatesPrimaryKey)), ${symbol_dollar}{relationship.actualDestination.classNameWithDefault} ${symbol_dollar}{relationship.name}${symbol_pound}end
${symbol_pound}end
) {
    ${symbol_dollar}{entity.classNameWithOptionalPackage} eo = (${symbol_dollar}{entity.classNameWithOptionalPackage})${symbol_pound}if (${symbol_dollar}entity.partialEntitySet)this;${symbol_pound}else EOUtilities.createAndInsertInstance(editingContext, ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.ENTITY_NAME);${symbol_pound}end

${symbol_pound}foreach (${symbol_dollar}attribute in ${symbol_dollar}entity.sortedClassAttributes)
${symbol_pound}if (!${symbol_dollar}attribute.allowsNull)
${symbol_pound}set (${symbol_dollar}restrictingQualifierKey = 'false')
${symbol_pound}foreach (${symbol_dollar}qualifierKey in ${symbol_dollar}entity.restrictingQualifierKeys)
${symbol_pound}if (${symbol_dollar}attribute.name == ${symbol_dollar}qualifierKey)
${symbol_pound}set (${symbol_dollar}restrictingQualifierKey = 'true')
${symbol_pound}end
${symbol_pound}end
${symbol_pound}if (${symbol_dollar}restrictingQualifierKey == 'false')
    eo.set${symbol_dollar}{attribute.capitalizedName}(${symbol_dollar}{attribute.name});
${symbol_pound}end
${symbol_pound}end
${symbol_pound}end
${symbol_pound}foreach (${symbol_dollar}relationship in ${symbol_dollar}entity.sortedClassToOneRelationships)
${symbol_pound}if (${symbol_dollar}relationship.mandatory && !(${symbol_dollar}relationship.ownsDestination && ${symbol_dollar}relationship.propagatesPrimaryKey))
    eo.set${symbol_dollar}{relationship.capitalizedName}Relationship(${symbol_dollar}{relationship.name});
${symbol_pound}end
${symbol_pound}end
    return eo;
  }
${symbol_pound}if (!${symbol_dollar}entity.partialEntitySet)

${symbol_pound}if (${symbol_dollar}entity.parentSet)
  public static ERXFetchSpecification<${symbol_dollar}{entity.classNameWithOptionalPackage}> fetchSpecFor${symbol_dollar}{entity.name}() {
    return new ERXFetchSpecification<${symbol_dollar}{entity.classNameWithOptionalPackage}>(${symbol_dollar}{entity.prefixClassNameWithoutPackage}.ENTITY_NAME, null, null, false, true, null);
  }
${symbol_pound}else
  public static ERXFetchSpecification<${symbol_dollar}{entity.classNameWithOptionalPackage}> fetchSpec() {
    return new ERXFetchSpecification<${symbol_dollar}{entity.classNameWithOptionalPackage}>(${symbol_dollar}{entity.prefixClassNameWithoutPackage}.ENTITY_NAME, null, null, false, true, null);
  }
${symbol_pound}end

  public static NSArray<${symbol_dollar}{entity.classNameWithOptionalPackage}> fetchAll${symbol_dollar}{entity.pluralName}(EOEditingContext editingContext) {
    return ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.fetchAll${symbol_dollar}{entity.pluralName}(editingContext, null);
  }

  public static NSArray<${symbol_dollar}{entity.classNameWithOptionalPackage}> fetchAll${symbol_dollar}{entity.pluralName}(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.fetch${symbol_dollar}{entity.pluralName}(editingContext, null, sortOrderings);
  }

  public static NSArray<${symbol_dollar}{entity.classNameWithOptionalPackage}> fetch${symbol_dollar}{entity.pluralName}(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<${symbol_dollar}{entity.classNameWithOptionalPackage}> fetchSpec = new ERXFetchSpecification<${symbol_dollar}{entity.classNameWithOptionalPackage}>(${symbol_dollar}{entity.prefixClassNameWithoutPackage}.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<${symbol_dollar}{entity.classNameWithOptionalPackage}> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static ${symbol_dollar}{entity.classNameWithOptionalPackage} fetch${symbol_dollar}{entity.name}(EOEditingContext editingContext, String keyName, Object value) {
    return ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.fetch${symbol_dollar}{entity.name}(editingContext, ERXQ.equals(keyName, value));
  }

  public static ${symbol_dollar}{entity.classNameWithOptionalPackage} fetch${symbol_dollar}{entity.name}(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<${symbol_dollar}{entity.classNameWithOptionalPackage}> eoObjects = ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.fetch${symbol_dollar}{entity.pluralName}(editingContext, qualifier, null);
    ${symbol_dollar}{entity.classNameWithOptionalPackage} eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one ${symbol_dollar}{entity.name} that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static ${symbol_dollar}{entity.classNameWithOptionalPackage} fetchRequired${symbol_dollar}{entity.name}(EOEditingContext editingContext, String keyName, Object value) {
    return ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.fetchRequired${symbol_dollar}{entity.name}(editingContext, ERXQ.equals(keyName, value));
  }

  public static ${symbol_dollar}{entity.classNameWithOptionalPackage} fetchRequired${symbol_dollar}{entity.name}(EOEditingContext editingContext, EOQualifier qualifier) {
    ${symbol_dollar}{entity.classNameWithOptionalPackage} eoObject = ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.fetch${symbol_dollar}{entity.name}(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no ${symbol_dollar}{entity.name} that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static ${symbol_dollar}{entity.classNameWithOptionalPackage} localInstanceIn(EOEditingContext editingContext, ${symbol_dollar}{entity.classNameWithOptionalPackage} eo) {
    ${symbol_dollar}{entity.classNameWithOptionalPackage} localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
${symbol_pound}end
${symbol_pound}foreach (${symbol_dollar}fetchSpecification in ${symbol_dollar}entity.sortedFetchSpecs)
${symbol_pound}if (true || ${symbol_dollar}fetchSpecification.distinctBindings.size() > 0)
  public static NSArray${symbol_pound}if (${symbol_dollar}fetchSpecification.fetchEnterpriseObjects)<${symbol_dollar}{entity.className}>${symbol_pound}else<NSDictionary>${symbol_pound}end fetch${symbol_dollar}{fetchSpecification.capitalizedName}(EOEditingContext editingContext, NSDictionary<String, Object> bindings) {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("${symbol_dollar}{fetchSpecification.name}", ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.ENTITY_NAME);
    fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray${symbol_pound}if (${symbol_dollar}fetchSpecification.fetchEnterpriseObjects)<${symbol_dollar}{entity.className}>${symbol_pound}else<NSDictionary>${symbol_pound}end)editingContext.objectsWithFetchSpecification(fetchSpec);
  }

${symbol_pound}end
  public static NSArray${symbol_pound}if (${symbol_dollar}fetchSpecification.fetchEnterpriseObjects)<${symbol_dollar}{entity.className}>${symbol_pound}else<NSDictionary>${symbol_pound}end fetch${symbol_dollar}{fetchSpecification.capitalizedName}(EOEditingContext editingContext${symbol_pound}foreach (${symbol_dollar}binding in ${symbol_dollar}fetchSpecification.distinctBindings),
  ${symbol_dollar}{binding.attributePath.childClassName} ${symbol_dollar}{binding.name}Binding${symbol_pound}end)
  {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("${symbol_dollar}{fetchSpecification.name}", ${symbol_dollar}{entity.prefixClassNameWithoutPackage}.ENTITY_NAME);
${symbol_pound}if (${symbol_dollar}fetchSpecification.distinctBindings.size() > 0)
    NSMutableDictionary<String, Object> bindings = new NSMutableDictionary<String, Object>();
${symbol_pound}foreach (${symbol_dollar}binding in ${symbol_dollar}fetchSpecification.distinctBindings)
    bindings.takeValueForKey(${symbol_dollar}{binding.name}Binding, "${symbol_dollar}{binding.name}");
${symbol_pound}end
    fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
${symbol_pound}end
    return (NSArray${symbol_pound}if (${symbol_dollar}fetchSpecification.fetchEnterpriseObjects)<${symbol_dollar}{entity.className}>${symbol_pound}else<NSDictionary>${symbol_pound}end)editingContext.objectsWithFetchSpecification(fetchSpec);
  }

${symbol_pound}end
}
