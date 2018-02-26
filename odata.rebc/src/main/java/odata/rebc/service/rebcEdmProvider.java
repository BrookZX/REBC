package odata.rebc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.AssociationSetEnd;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class rebcEdmProvider extends EdmProvider {

	private static final Logger LOG = LoggerFactory.getLogger(rebcEdmProvider.class);

	// Service Namespace
	public static final String NAMESPACE = "OData.rebc";

	// EDM Container
	public static final String CONTAINER_NAME = "RebcContainer";

	// Entity Types Names
	public static final String ET_HOUSE = "House";
	public static final FullQualifiedName ET_HOUSE_FQN = new FullQualifiedName(NAMESPACE, ET_HOUSE);

	public static final String ET_LEASE = "Lease";
	public static final FullQualifiedName ET_LEASE_FQN = new FullQualifiedName(NAMESPACE, ET_LEASE);

	// Entity Set Names
	public static final String ES_HOUSES = "Houses";
	public static final String ES_LEASES = "Leases";

	// Associations
	private static final FullQualifiedName ASSOCIATION_LEASE_HOUSE = new FullQualifiedName(NAMESPACE,
			"Lease_House_House_Leases");

	private static final String ASSOCIATION_SET = "Leases_Houses";

	private static final String ROLE_1_1 = "Lease_House";
	private static final String ROLE_1_2 = "House_Leases";
	
	// Action
	public static final String ACTION_LOAD = "Load";
	public static final FullQualifiedName ACTION_LOAD_FQN = new FullQualifiedName(NAMESPACE, ACTION_LOAD);
	private static final FullQualifiedName COMPLEX_TYPE = new FullQualifiedName(NAMESPACE, "Address");

	public static final String PARAMETER_PROJECT = "project";
	public static final String PARAMETER_OWNER = "owner";

 

	public EntityContainerInfo getEntityContainerInfo(String name) throws ODataException {
		if (name == null || CONTAINER_NAME.equals(name)) {
			return new EntityContainerInfo().setName(CONTAINER_NAME).setDefaultEntityContainer(true);
		}

		return null;
	}

	@Override
	public EntitySet getEntitySet(String entityContainer, String name) throws ODataException {

		LOG.debug("getEntitySet()");

		if (CONTAINER_NAME.equals(entityContainer)) {
			if (ES_HOUSES.equals(name)) {
				return new EntitySet().setName(name).setEntityType(ET_HOUSE_FQN);
			} else if (ES_LEASES.equals(name)) {
				return new EntitySet().setName(name).setEntityType(ET_LEASE_FQN);
			}  
		}
		return null;
	}

	@Override
	public EntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
		if (NAMESPACE.equals(entityTypeName.getNamespace())) {
			LOG.debug("getEntityType()");

			// this method is called for each EntityType that are configured in the
			// Schema
			EntityType entityType = null;

			// this method is called for one of the EntityTypes that are configured
			// in the Schema
			if (entityTypeName.equals(ET_HOUSE_FQN)) {

				// Properties
				List<Property> properties = new ArrayList<Property>();
				
				// create EntityType properties
				properties.add( new SimpleProperty().setName("UUID")
						.setType(EdmSimpleTypeKind.String) );
				properties.add( new SimpleProperty().setName("PRO_NAME")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("ADDRESS")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("OWNER")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("UPDATER")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("STATUS")
						.setType(EdmSimpleTypeKind.String));

				// Navigation Properties
				List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
				navigationProperties.add(new NavigationProperty().setName("Leases")
						.setRelationship(ASSOCIATION_LEASE_HOUSE).setFromRole(ROLE_1_2).setToRole(ROLE_1_1));

				// Key
				List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
				keyProperties.add(new PropertyRef().setName("UUID"));
				Key key = new Key().setKeys(keyProperties);

				return new EntityType().setName(ET_HOUSE_FQN.getName()).setProperties(properties).setHasStream(true)
						.setKey(key).setNavigationProperties(navigationProperties);
				
		 

			} else if (entityTypeName.equals(ET_LEASE_FQN)) {
				// Properties
				List<Property> properties = new ArrayList<Property>();
				
				// create EntityType properties
				properties.add( new SimpleProperty().setName("ID")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("Timestamp")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("UUID")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("PRO_NAME")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("ADDRESS")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("PRICE")
						.setType(EdmSimpleTypeKind.Decimal));
				properties.add( new SimpleProperty().setName("LEASE_FROM")
						.setType(EdmSimpleTypeKind.DateTime));
				properties.add( new SimpleProperty().setName("LEASE_TO")
						.setType(EdmSimpleTypeKind.DateTime));
				properties.add( new SimpleProperty().setName("OWNER")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("TENANT")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("APPLIER")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("TERMS")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("STATUS")
						.setType(EdmSimpleTypeKind.String));
				properties.add( new SimpleProperty().setName("UPDATER")
						.setType(EdmSimpleTypeKind.String));

				// Navigation Properties
				List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
				navigationProperties.add(new NavigationProperty().setName("House")
						.setRelationship(ASSOCIATION_LEASE_HOUSE).setFromRole(ROLE_1_1).setToRole(ROLE_1_2));

				// Key
				List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
				keyProperties.add(new PropertyRef().setName("ID"));
				Key key = new Key().setKeys(keyProperties);

				return new EntityType().setName(ET_LEASE_FQN.getName()).setProperties(properties).setKey(key)
						.setNavigationProperties(navigationProperties);
			}

			return entityType;
		}
		return null;
	}

 
	public List<Schema> getSchemas() throws ODataException {

		LOG.debug("getSchemas()");

		// create Schema
		Schema schema = new Schema();
		schema.setNamespace(NAMESPACE);
		

		// add EntityContainer
		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(CONTAINER_NAME).setDefaultEntityContainer(true);
		entityContainers.add(entityContainer);
		schema.setEntityContainers(entityContainers);

		// add EntityTypes
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		entityTypes.add(getEntityType(ET_HOUSE_FQN));
		entityTypes.add(getEntityType(ET_LEASE_FQN));
		schema.setEntityTypes(entityTypes);

  
		// add association
		List<Association> associations = new ArrayList<Association>();
		associations.add(getAssociation(ASSOCIATION_LEASE_HOUSE));
		schema.setAssociations(associations);
		

		// add entityset
		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		entitySets.add(getEntitySet(CONTAINER_NAME, ES_LEASES));
		entitySets.add(getEntitySet(CONTAINER_NAME, ES_HOUSES));
		entityContainer.setEntitySets(entitySets);

//		// add actions
//		List<CsdlAction> actions = new ArrayList<CsdlAction>();
//		actions.addAll(getActions(ACTION_LOAD_FQN));
//		schema.setActions(actions);
		
		// association set
		List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
		associationSets.add(getAssociationSet(CONTAINER_NAME, ASSOCIATION_LEASE_HOUSE,
				ES_HOUSES, ROLE_1_2));
		entityContainer.setAssociationSets(associationSets);
 

		// finally
		List<Schema> schemas = new ArrayList<Schema>();
		schemas.add(schema);

		return schemas;
	}

	public ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataException {
		if (NAMESPACE.equals(edmFQName.getNamespace())) {
			if (COMPLEX_TYPE.getName().equals(edmFQName.getName())) {
				List<Property> properties = new ArrayList<Property>();
				properties.add(new SimpleProperty().setName("Street").setType(EdmSimpleTypeKind.String));
				properties.add(new SimpleProperty().setName("City").setType(EdmSimpleTypeKind.String));
				properties.add(new SimpleProperty().setName("ZipCode").setType(EdmSimpleTypeKind.String));
				properties.add(new SimpleProperty().setName("Country").setType(EdmSimpleTypeKind.String));
				return new ComplexType().setName(COMPLEX_TYPE.getName()).setProperties(properties);
			}
		}
		return null;
	}
	
	public Association getAssociation(FullQualifiedName edmFQName) throws ODataException {
		if (NAMESPACE.equals(edmFQName.getNamespace())) {
			if (ASSOCIATION_LEASE_HOUSE.getName().equals(edmFQName.getName())) {
				return new Association().setName(ASSOCIATION_LEASE_HOUSE.getName())
						.setEnd1(new AssociationEnd().setType(ET_LEASE_FQN).setRole(ROLE_1_1)
								.setMultiplicity(EdmMultiplicity.MANY))
						.setEnd2(new AssociationEnd().setType(ET_HOUSE_FQN).setRole(ROLE_1_2)
								.setMultiplicity(EdmMultiplicity.ONE));
			}
		}
		return null;
	}
	
	public AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association,
			String sourceEntitySetName, String sourceEntitySetRole) throws ODataException {
		if (CONTAINER_NAME.equals(entityContainer)) {
			if (ASSOCIATION_LEASE_HOUSE.equals(association)) {
				return new AssociationSet().setName(ASSOCIATION_SET).setAssociation(ASSOCIATION_LEASE_HOUSE)
						.setEnd1(new AssociationSetEnd().setRole(ROLE_1_2).setEntitySet(ES_HOUSES))
						.setEnd2(new AssociationSetEnd().setRole(ROLE_1_1).setEntitySet(ES_LEASES));
			}
		}
		return null;
	}
	
//	@Override
//	public List<CsdlAction> getActions(final FullQualifiedName actionName) {
//		if (actionName.equals(ACTION_LOAD_FQN)) {
//			// It is allowed to overload actions, so we have to provide a list of Actions
//			// for each action name
//			final List<CsdlAction> actions = new ArrayList<CsdlAction>();
//
//			// Create parameters
//			final List<CsdlParameter> parameters = new ArrayList<CsdlParameter>();
//			final CsdlParameter parameter = new CsdlParameter();
//			parameter.setName(PARAMETER_PROJECT);
//			parameter.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
//			parameters.add(parameter);
//
//			final CsdlParameter parameter2 = new CsdlParameter();
//			parameter2.setName(PARAMETER_OWNER);
//			parameter2.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
//			parameters.add(parameter2);
//
//			// Create the Csdl Action
//			final CsdlAction action = new CsdlAction();
//			action.setName(ACTION_LOAD_FQN.getName());
//			action.setParameters(parameters);
//			actions.add(action);
//
//			return actions;
//		}
//
//		return null;
//	}

//	@Override
//	public CsdlActionImport getActionImport(final FullQualifiedName entityContainer, final String actionImportName) {
//		if (entityContainer.equals(CONTAINER)) {
//			if (actionImportName.equals(ACTION_LOAD_FQN.getName())) {
//				return new CsdlActionImport().setName(actionImportName).setAction(ACTION_LOAD_FQN);
//			}
//		}
//
//		return null;
//	}
}
