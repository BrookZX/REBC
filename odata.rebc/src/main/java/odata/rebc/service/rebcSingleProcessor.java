package odata.rebc.service;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.api.uri.info.PutMergePatchUriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odata.rebc.model.OModel;
import odata.rebc.service.rebcEdmProvider;

public class rebcSingleProcessor extends ODataSingleProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(rebcSingleProcessor.class);
	private OModel oModel = new OModel();
	
	public ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException {

		if (uriInfo.getNavigationSegments().size() == 0) {
			EdmEntitySet entitySet = uriInfo.getStartEntitySet();

			if (rebcEdmProvider.ES_HOUSES.equals(entitySet.getName())) {
				String uuid = getKeyValueString(uriInfo.getKeyPredicates().get(0));
				Map<String, Object> data = oModel.getHouse(uuid);

				if (data != null) {
					URI serviceRoot = getContext().getPathInfo().getServiceRoot();
					ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties
							.serviceRoot(serviceRoot);

					return EntityProvider.writeEntry(contentType, entitySet, data, propertiesBuilder.build());
				}
			} else if (rebcEdmProvider.ES_LEASES.equals(entitySet.getName())) {
				// int id = getKeyValue(uriInfo.getKeyPredicates().get(0));
				// Map<String, Object> data = dataStore.getManufacturer(id);
				//
				// if (data != null) {
				// URI serviceRoot = getContext().getPathInfo().getServiceRoot();
				// ODataEntityProviderPropertiesBuilder propertiesBuilder =
				// EntityProviderWriteProperties.serviceRoot(serviceRoot);
				//
				// return EntityProvider.writeEntry(contentType, entitySet, data,
				// propertiesBuilder.build());
				// }
				return null;
			}

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

		} else if (uriInfo.getNavigationSegments().size() == 1) {
			// navigation first level, simplified example for illustration purposes only
			EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
			if (rebcEdmProvider.ES_HOUSES.equals(entitySet.getName())) {
				String uuid = getKeyValueString(uriInfo.getKeyPredicates().get(0));
				return EntityProvider.writeEntry(contentType, uriInfo.getTargetEntitySet(),
						oModel.getHouse(uuid),
						EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
			}

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
		}
		return null;
	}
	

	public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException {

		EdmEntitySet entitySet;

		if (uriInfo.getNavigationSegments().size() == 0) {
			entitySet = uriInfo.getStartEntitySet();
			
			LOG.debug("Read entity set:"+entitySet.getName());

			if (rebcEdmProvider.ES_HOUSES.equals(entitySet.getName())) {
				List<Map<String, Object>> houses = oModel.readEntitySetData(entitySet, uriInfo.getKeyPredicates());
				 
				return EntityProvider.writeFeed(contentType, entitySet, houses,
						EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
			} else if (rebcEdmProvider.ES_LEASES.equals(entitySet.getName())) {
				return null;
			}

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

		} else if (uriInfo.getNavigationSegments().size() == 1) {
			// navigation first level, simplified example for illustration
			// purposes only
			entitySet = uriInfo.getTargetEntitySet();

			if (rebcEdmProvider.ES_HOUSES.equals(entitySet.getName())) { 
				String uuid = getKeyValueString(uriInfo.getKeyPredicates().get(0));

				List<Map<String, Object>> houses = new ArrayList<Map<String, Object>>();

				houses.add(oModel.getHouse(uuid));  
 
				return EntityProvider.writeFeed(contentType, entitySet, houses,
						EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
			}

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
		}

		throw new ODataNotImplementedException();
	}
	
	@Override
	public ODataResponse updateEntity(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, boolean merge, String contentType) throws ODataException {
	EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(false).build();

	  LOG.debug("updateEntity");
		
	  ODataEntry entry = EntityProvider.readEntry(requestContentType, uriInfo.getTargetEntitySet(), content, properties);
	  //if something goes wrong in deserialization this is managed via the ExceptionMapper,
	  //no need for an application to do exception handling here an convert the exceptions in HTTP exceptions

	  Map<String, Object> newData = entry.getProperties();
	  LOG.debug("Updating entity:"+newData);

	  if (rebcEdmProvider.ES_HOUSES.equals(uriInfo.getTargetEntitySet().getName())) {
	    String uuid = getKeyValueString(uriInfo.getKeyPredicates().get(0));
	    
	    LOG.debug("Updating category id "+ uuid);
	    Map<String, Object> oldData = oModel.getHouse(uuid);
	    if (oldData==null) {
	    	LOG.debug("House id "+uuid+" does not exist");
	    	//if there is no entry with this key available, one should return "404 Not Found"
	    	return ODataResponse.status(HttpStatusCodes.NOT_FOUND).build();
	    } else {
	    	oModel.updateEntityData(uriInfo.getTargetEntitySet(), uriInfo.getKeyPredicates(), oldData, newData);
	    }
	  } else if (rebcEdmProvider.ES_LEASES.equals(uriInfo.getTargetEntitySet().getName())) {
//	    String id = getKeyValueString(uriInfo.getKeyPredicates().get(0));
//	    
//	    LOG.debug("Updating product id "+id);
//	    //Map<String, Object> oldData = oModel.getProduct(id);
//	    
//	    if (oldData==null) {
//	    	LOG.debug("Product id "+id+" does not exist");
//	    	//if there is no entry with this key available, one should return "404 Not Found"
//	    	return ODataResponse.status(HttpStatusCodes.NOT_FOUND).build();
//	    } else {
//
//	    	dataStore.updateProduct(oldData, newData);
//	    }
	    
	  }

	  //we can return Status Code 204 No Content because the URI Parsing already guarantees that
	  //a) only valid URIs are dispatched (also checked against the metadata)
	  //b) 404 Not Found is already returned above, when the entry does not exist 
	  return ODataResponse.status(HttpStatusCodes.OK).build();
	}
	
	@Override
	public ODataResponse createEntity(PostUriInfo uriInfo, InputStream content, 
	String requestContentType, String contentType) throws ODataException {
		
	  LOG.debug("createEntity");
		
		throw new ODataNotImplementedException();
	}

	private int getKeyValue(KeyPredicate key) throws ODataException {
		EdmProperty property = key.getProperty();
		EdmSimpleType type = (EdmSimpleType) property.getType();
		return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(), Integer.class);
	}
	
	private String getKeyValueString(KeyPredicate key) throws ODataException {
		EdmProperty property = key.getProperty();
		EdmSimpleType type = (EdmSimpleType) property.getType();
		return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(), String.class);
	}
}
