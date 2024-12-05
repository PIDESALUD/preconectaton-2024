package pe.gob.minsa.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.bean.PropertiesBean;
import pe.gob.minsa.bean.RequestRegisterOrganization;
import pe.gob.minsa.bean.RequestSearchOrganization;
import pe.gob.minsa.bean.RequestUpdateOrganization;
import pe.gob.minsa.service.FhirOrganizationService;
import pe.gob.minsa.util.Utilitario;

@Service
public class FhirOrganizationServiceImpl implements FhirOrganizationService{

	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PropertiesBean properties;
	
	private ObjectMapper om = new ObjectMapper();


	@Override
	public GenericResponseBean registerOrganization(RequestRegisterOrganization request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
						
			Map<String, Object> data = new LinkedHashMap<>();			
			
			Organization organizacion = new Organization();		
			organizacion.setActive(Boolean.TRUE);		
			
			/********************** VALORES INDENTIFIER ****************************/
			StringType st = new StringType();
			st.setValue(request.getUbigeo());
			
			organizacion.addIdentifier().setValue(request.getCodigoRenipress());			
			organizacion.setName(request.getNombreEstablecimiento());
			
			organizacion.addAddress()
			.addLine(request.getDireccion())
			.addExtension().setValue(st).setUrl("ninguno");
												
			MethodOutcome outcome = client
					.create()
					.resource(organizacion)
					.execute();

			// Log the ID that the server assigned
			IIdType id = outcome.getId();
			System.out.println("****** Created organizacion, got ID: " + id);
			System.out.println("****** ID VALUE: " + id.getIdPartAsLong());
			
			//data.put("url", String.valueOf(id));
			data.put("id", String.valueOf(id.getIdPartAsLong()));
			
			responseBean.setCodigo("0000");
			responseBean.setData(data);
						
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error registerOrganization: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean updateOrganization(RequestUpdateOrganization request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		Map<String, Object> data = new LinkedHashMap<>();
		
		
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {
				
				Organization organizacion =client.read().resource(Organization.class).withId(request.getIdOrganizacion()).execute();
				
				if(request.getActivo().trim() != null && request.getActivo().trim().length() > 0 && !request.getActivo().trim().equals("")) {
					
					if(Utilitario.validateActivo(request.getActivo()).equals("0000")) {
						if(request.getActivo().trim().toLowerCase().equals("true"))
							organizacion.setActive(Boolean.TRUE);
						
						if(request.getActivo().trim().toLowerCase().equals("false"))
							organizacion.setActive(Boolean.FALSE);
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo activo no cumple con los estandares requeridos, es solo [true|false]");
						
						return responseBean;
					}					
					
				}
				
				
				/********************** VALORES INDENTIFIER ****************************/
				List<Identifier> lstIdentifier = new ArrayList<>();
				Identifier orgIdentifier = new Identifier();				
				StringType st = new StringType();
				List<Address> theAddress = new ArrayList<>();
				Address address = new Address();
				
				if(request.getCodigoRenipress().trim() != null && 
						request.getCodigoRenipress().trim().length() > 0 && 
						 !request.getCodigoRenipress().trim().equals("")) {
										
					orgIdentifier.setValue(request.getCodigoRenipress());					
					lstIdentifier.add(orgIdentifier);
					
					organizacion.setIdentifier(lstIdentifier);
					
					
				}
				
				if(request.getNombreEstablecimiento().trim() != null && 
						request.getNombreEstablecimiento().trim().length() > 0 && 
						 !request.getNombreEstablecimiento().trim().equals("")) {										
					organizacion.setName(request.getNombreEstablecimiento());
				}
				
				if(request.getUbigeo().trim() != null && 
						request.getUbigeo().trim().length() > 0 && 
						 !request.getUbigeo().trim().equals("")
						 ) {
					
					st.setValue(request.getUbigeo());					
					address.addLine(request.getDireccion());
					address.addExtension().setValue(st).setUrl("ninguno");
					
					theAddress.add(address);					
					organizacion.setAddress(theAddress);
				}
								
				MethodOutcome outcome = client
						.update()
						.resource(organizacion)
						.execute();

				// Log the ID that the server assigned
				IIdType id = outcome.getId();
				System.out.println("****** Update organizacion, got ID: " + id);
				System.out.println("****** ID VALUE: " + id.getIdPartAsLong());
				
				//data.put("url", String.valueOf(id));
				data.put("id", String.valueOf(id.getIdPartAsLong()));
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException updateOrganizacion: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Organizacion no existe");
			} catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException updateOrganizacion: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Organizacion fue eliminado");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error updateOrganizacion: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean deleteOrganization(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {

				MethodOutcome outcome = client
						.delete()
						.resourceById(new IdDt("Organization", Long.parseLong(id)))
						.execute();
				
				System.out.println("****** Delete Organization, got ID: " + id);
				System.out.println("****** ID VALUE: " + outcome.toString());
				
				responseBean.setCodigo("0000");
				responseBean.setData(null);
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException deleteOrganization: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Organization no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceGoneException deleteOrganization: " + e.getMessage());
				responseBean.setCodigo("6001");
				responseBean.setData("El id Organization fue eliminado");
			}			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error deleteOrganization: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean historyOrganizationById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
		Map<String, Object> data = new LinkedHashMap<>();
		boolean borrado = false;
		try {
			
			try {

				Bundle response = client.history()			      
						.onInstance(new IdDt("Organization",id))
						.returnBundle(Bundle.class)
						.execute();

				int total = response.getTotal();
				
				System.out.println(" total: " + total);
						
				borrado = response.getEntry().stream().filter(entry -> entry.getRequest().getMethod().name().equals("DELETE"))						
				        .collect(Collectors.toList()).isEmpty();
				
				System.out.println(" borrado: " + borrado);
				if(borrado) {
					
					lista1 = response.getEntry().stream()
					        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.Organization))				        
					        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
					        .collect(Collectors.toList());
					
					Type listType = new TypeToken<List<Object>>() {}.getType();
					
					data.put("totalResults", String.valueOf(total));
					data.put("listHistoryOrganization", new Gson().fromJson(lista1.toString(), listType));				
					
					responseBean.setCodigo("0000");
					responseBean.setData(data);
					
				}else {
					responseBean.setCodigo("6000");
					responseBean.setData("El Organization fue eliminado");	
				}
				
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException historyOrganizationById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Organization no existe");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error historyOrganizationById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}

	@Override
	public GenericResponseBean searchOrganizationById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

			try {
				Organization organizacion = client.read().resource(Organization.class).withId(id).execute();				
				String organizacionJson = ctx.newJsonParser().encodeResourceToString(organizacion);
				
				responseBean.setCodigo("0000");
				responseBean.setData(om.readValue(organizacionJson, Map.class));
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException searchOrganizationById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Organization no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException searchOrganizationById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Organization fue eliminado");
			}				
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchOrganizationById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean searchOrganization(RequestSearchOrganization request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

		int skipCount = (request.getPageNumber() - 1) * request.getLimit();
		int total = 0;
		Map<String, Object> data = new LinkedHashMap<>();
		try {
			
			Bundle response = client.search()
					.forResource(Organization.class)					
					//.where(Practitioner.ACTIVE.isMissing(Boolean.FALSE))
					.where(Organization.NAME.contains().value(request.getName()))					
					.returnBundle(Bundle.class)
					.execute();

			total = response.getTotal();
			
			System.out.println(" total: " + total);
			if (response.getTotal() > 0) {
				
				lista1 = response.getEntry().stream()
				        .skip(skipCount)
				        .limit(request.getLimit())
				        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.Organization))			        
				        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
				        .collect(Collectors.toList());

				Type listType = new TypeToken<List<Object>>() {}.getType();
				
				data.put("pageNumber", String.valueOf(request.getPageNumber()));
				data.put("resultsPerPage", String.valueOf(request.getLimit()));
				data.put("totalResults", String.valueOf(total));
				data.put("listOrganization", new Gson().fromJson(lista1.toString(), listType));				
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
		
			} else 				
				responseBean.setCodigo("6000");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchOrganization: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}

}
