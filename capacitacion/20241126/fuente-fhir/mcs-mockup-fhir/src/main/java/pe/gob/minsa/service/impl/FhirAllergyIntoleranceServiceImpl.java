package pe.gob.minsa.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCategoryEnumFactory;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCriticality;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
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
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.bean.PropertiesBean;
import pe.gob.minsa.bean.RequestRegisterAllergyIntolerance;
import pe.gob.minsa.bean.RequestSearchAllergyIntolerance;
import pe.gob.minsa.bean.RequestUpdateAllergyIntolerance;
import pe.gob.minsa.service.FhirAllergyIntoleranceService;

@Service
public class FhirAllergyIntoleranceServiceImpl implements FhirAllergyIntoleranceService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PropertiesBean properties;
	
	private ObjectMapper om = new ObjectMapper();

	@Override
	public GenericResponseBean registerAllergyIntolerance(RequestRegisterAllergyIntolerance request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
						
			Map<String, Object> data = new LinkedHashMap<>();			
			
			AllergyIntolerance alergia = new AllergyIntolerance();		
			
			CodeableConcept codeTipo = new CodeableConcept();
			codeTipo.addCoding().setCode(Boolean.TRUE.toString());			
			
			alergia.setClinicalStatus(codeTipo);
			
			if(request.getTipoAlergia().trim().toUpperCase().equals("A"))			
				alergia.setType(AllergyIntoleranceType.ALLERGY);
			else if(request.getTipoAlergia().trim().toUpperCase().equals("I"))			
				alergia.setType(AllergyIntoleranceType.INTOLERANCE);
			else
				alergia.setType(AllergyIntoleranceType.NULL);
			
			if(!request.getCategoriaAlergia().isEmpty() && request.getCategoriaAlergia() != null) {
				
				List<Enumeration<AllergyIntoleranceCategory>> theCategory = new ArrayList<>();
				Enumeration<AllergyIntoleranceCategory> myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
			    
				for(String item: request.getCategoriaAlergia()) {
					if(item.trim().toUpperCase().equals("C")) {
						myEnumFactory.setValue(AllergyIntoleranceCategory.FOOD);
						theCategory.add(myEnumFactory);
						myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
					}
					if(item.trim().toUpperCase().equals("M")) {
						myEnumFactory.setValue(AllergyIntoleranceCategory.MEDICATION);
						theCategory.add(myEnumFactory);
						myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
					}
					if(item.trim().toUpperCase().equals("E")) {
						myEnumFactory.setValue(AllergyIntoleranceCategory.ENVIRONMENT);
						theCategory.add(myEnumFactory);
						myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
					}
					if(item.trim().toUpperCase().equals("B")) {
						myEnumFactory.setValue(AllergyIntoleranceCategory.BIOLOGIC);
						theCategory.add(myEnumFactory);
						myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
					}
					if(item.trim().toUpperCase().equals("N")) {
						myEnumFactory.setValue(AllergyIntoleranceCategory.NULL);
						theCategory.add(myEnumFactory);
						myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
					}
				}
				
				alergia.setCategory(theCategory);
				
			}
			
			if(request.getGradoCriticidad().trim().toUpperCase().equals("B"))
				alergia.setCriticality(AllergyIntoleranceCriticality.LOW);
			else if(request.getGradoCriticidad().trim().toUpperCase().equals("A"))
				alergia.setCriticality(AllergyIntoleranceCriticality.HIGH);
			else if(request.getGradoCriticidad().trim().toUpperCase().equals("I"))
				alergia.setCriticality(AllergyIntoleranceCriticality.UNABLETOASSESS);
			else
				alergia.setCriticality(AllergyIntoleranceCriticality.NULL);
			
			CodeableConcept codeDescrAlergia = new CodeableConcept();
			codeDescrAlergia.addCoding().setCode(request.getCodigoAlergia()).setDisplay(request.getDescrAlergia());
			codeDescrAlergia.setText(request.getDescrAlergia());
			alergia.setCode(codeDescrAlergia);
			
			CodeableConcept codeClinicaStatus = new CodeableConcept();
			codeClinicaStatus.addCoding().setCode(request.getConfirmaAlergia());			
			alergia.setClinicalStatus(codeClinicaStatus);
			
			/********************** INICIO VALIDACION DE PACIENTE ****************************/
			try {
				
				Reference referencePatient = new Reference();
				referencePatient.setReference("Patient/"+request.getIdPaciente());
				
				alergia.setPatient(referencePatient);
							
				MethodOutcome outcome = client
						.create()
						.resource(alergia)
						.execute();

				IIdType id = outcome.getId();
				System.out.println("****** Created AllergyIntolerance, got ID: " + id);
				System.out.println("****** ID VALUE: " + id.getIdPartAsLong());

				data.put("id", String.valueOf(id.getIdPartAsLong()));
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
				
			} catch (InvalidRequestException e) {
				e.printStackTrace();
				logger.error(" ** Error InvalidRequestException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id paciente no existe o fue eliminado");
			}catch (UnprocessableEntityException e) {
				e.printStackTrace();
				logger.error(" ** Error UnprocessableEntityException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id ingresado no hace referenecia a un idPaciente");
			}					
			/********************** FIN ****************************/
			
						
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error registerAllergyIntolerance: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean updateAllergyIntolerance(RequestUpdateAllergyIntolerance request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		Map<String, Object> data = new LinkedHashMap<>();
		
		try {
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {
			
				AllergyIntolerance alergia = client.read().resource(AllergyIntolerance.class).withId(request.getIdAllergy()).execute();
				
				if(request.getActivo().trim() != null && request.getActivo().trim().length() > 0 && !request.getActivo().trim().equals("")) {
					CodeableConcept codeTipo = new CodeableConcept();					
					if(request.getActivo().trim().toLowerCase().equals("true")) {
						codeTipo.addCoding().setCode(Boolean.TRUE.toString());
						alergia.setClinicalStatus(codeTipo);
					}											
					if(request.getActivo().trim().toLowerCase().equals("false")) {
						codeTipo.addCoding().setCode(Boolean.FALSE.toString());
						alergia.setClinicalStatus(codeTipo);
					}
				}
				
				if(request.getTipoAlergia().trim().length()>0 && !request.getTipoAlergia().trim().equals("")) {
					if(request.getTipoAlergia().trim().toUpperCase().equals("A"))			
						alergia.setType(AllergyIntoleranceType.ALLERGY);
					else if(request.getTipoAlergia().trim().toUpperCase().equals("I"))			
						alergia.setType(AllergyIntoleranceType.INTOLERANCE);
					else
						alergia.setType(AllergyIntoleranceType.NULL);
				}
				
				if(!request.getCategoriaAlergia().isEmpty() && request.getCategoriaAlergia() != null) {
					
					List<Enumeration<AllergyIntoleranceCategory>> theCategory = new ArrayList<>();
					Enumeration<AllergyIntoleranceCategory> myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
				    
					for(String item: request.getCategoriaAlergia()) {
						if(item.trim().toUpperCase().equals("C")) {
							myEnumFactory.setValue(AllergyIntoleranceCategory.FOOD);
							theCategory.add(myEnumFactory);
							myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
						}
						if(item.trim().toUpperCase().equals("M")) {
							myEnumFactory.setValue(AllergyIntoleranceCategory.MEDICATION);
							theCategory.add(myEnumFactory);
							myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
						}
						if(item.trim().toUpperCase().equals("E")) {
							myEnumFactory.setValue(AllergyIntoleranceCategory.ENVIRONMENT);
							theCategory.add(myEnumFactory);
							myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
						}
						if(item.trim().toUpperCase().equals("B")) {
							myEnumFactory.setValue(AllergyIntoleranceCategory.BIOLOGIC);
							theCategory.add(myEnumFactory);
							myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
						}
						if(item.trim().toUpperCase().equals("N")) {
							myEnumFactory.setValue(AllergyIntoleranceCategory.NULL);
							theCategory.add(myEnumFactory);
							myEnumFactory = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
						}
					}
					
					alergia.setCategory(theCategory);
					
				}
				
				if(request.getGradoCriticidad().trim().length()>0 && !request.getGradoCriticidad().trim().equals("")) {
					if(request.getGradoCriticidad().trim().toUpperCase().equals("B"))
						alergia.setCriticality(AllergyIntoleranceCriticality.LOW);
					else if(request.getGradoCriticidad().trim().toUpperCase().equals("A"))
						alergia.setCriticality(AllergyIntoleranceCriticality.HIGH);
					else if(request.getGradoCriticidad().trim().toUpperCase().equals("I"))
						alergia.setCriticality(AllergyIntoleranceCriticality.UNABLETOASSESS);
					else
						alergia.setCriticality(AllergyIntoleranceCriticality.NULL);
				}
				
				if(request.getCodigoAlergia().trim().length()>0 && !request.getCodigoAlergia().trim().equals("") &&
						request.getCodigoAlergia().trim() != null &&
						request.getDescrAlergia().trim().length()>0 && !request.getDescrAlergia().trim().equals("") &&
						request.getDescrAlergia().trim() != null
						) {
					CodeableConcept codeDescrAlergia = new CodeableConcept();
					codeDescrAlergia.addCoding().setCode(request.getCodigoAlergia()).setDisplay(request.getDescrAlergia());
					codeDescrAlergia.setText(request.getDescrAlergia());
					alergia.setCode(codeDescrAlergia);
				}
				
				if(request.getConfirmaAlergia().trim().length()>0 && !request.getConfirmaAlergia().trim().equals("") &&
						request.getConfirmaAlergia().trim() != null) {
					CodeableConcept codeClinicaStatus = new CodeableConcept();
					codeClinicaStatus.addCoding().setCode(request.getConfirmaAlergia());			
					alergia.setClinicalStatus(codeClinicaStatus);
				}
				
				try {
					Reference referencePatient = new Reference();
					referencePatient.setReference("Patient/"+request.getIdPaciente());
					
					alergia.setPatient(referencePatient);
					
					
					MethodOutcome outcome = client
							.update()
							.resource(alergia)
							.execute();
		
					// Log the ID that the server assigned
					IIdType id = outcome.getId();
					System.out.println("****** Update AllergyIntolerance, got ID: " + id);
					System.out.println("****** ID VALUE: " + id.getIdPartAsLong());
					
					//data.put("url", String.valueOf(id));
					data.put("id", String.valueOf(id.getIdPartAsLong()));
					
					responseBean.setCodigo("0000");
					responseBean.setData(data);
			
				
			} catch (InvalidRequestException e) {
				e.printStackTrace();
				logger.error(" ** Error InvalidRequestException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id paciente no existe o fue eliminado");
			}catch (UnprocessableEntityException e) {
				e.printStackTrace();
				logger.error(" ** Error UnprocessableEntityException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id ingresado no hace referenecia a un idPaciente");
			}			
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			logger.error(" ** Error ResourceNotFoundException updateAllergyIntolerance: " + e.getMessage());
			responseBean.setCodigo("6000");
			responseBean.setData("EL id Alergia no existe");
		} catch (ResourceGoneException e) {
			e.printStackTrace();
			logger.error(" ** Error ResourceNotFoundException updateAllergyIntolerance: " + e.getMessage());
			responseBean.setCodigo("6000");
			responseBean.setData("EL id Alergia fue eliminado");
		}
	} catch (Exception e) {
		e.printStackTrace();
		logger.error(" ** Error updateAllergyIntolerance: " + e.getMessage());
		responseBean.setCodigo("9000");
	}
	return responseBean;
	}

	@Override
	public GenericResponseBean deleteAllergyIntolerance(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {

				MethodOutcome outcome = client
						.delete()
						.resourceById(new IdDt("AllergyIntolerance", Long.parseLong(id)))
						.execute();
				
				System.out.println("****** Delete AllergyIntolerance, got ID: " + id);
				System.out.println("****** ID VALUE: " + outcome.toString());
				
				responseBean.setCodigo("0000");
				responseBean.setData(null);
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException deleteAllergyIntolerance: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Alergia no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceGoneException deleteAllergyIntolerance: " + e.getMessage());
				responseBean.setCodigo("6001");
				responseBean.setData("El id Alergia fue eliminado");
			}			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error deleteAllergyIntolerance: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean historyAllergyIntoleranceById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
		Map<String, Object> data = new LinkedHashMap<>();
		boolean borrado = false;
		try {
			
			try {

				Bundle response = client.history()			      
						.onInstance(new IdDt("AllergyIntolerance",id))
						.returnBundle(Bundle.class)
						.execute();

				int total = response.getTotal();
				
				System.out.println(" total: " + total);
						
				borrado = response.getEntry().stream().filter(entry -> entry.getRequest().getMethod().name().equals("DELETE"))						
				        .collect(Collectors.toList()).isEmpty();
				
				System.out.println(" borrado: " + borrado);
				if(borrado) {
					
					lista1 = response.getEntry().stream()
					        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.AllergyIntolerance))				        
					        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
					        .collect(Collectors.toList());
					
					Type listType = new TypeToken<List<Object>>() {}.getType();
					
					data.put("totalResults", String.valueOf(total));
					data.put("listHistoryAllergyIntolerance", new Gson().fromJson(lista1.toString(), listType));				
					
					responseBean.setCodigo("0000");
					responseBean.setData(data);
					
				}else {
					responseBean.setCodigo("6000");
					responseBean.setData("El id Alergia fue eliminado");	
				}
				
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException historyPatientById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Alergia no existe");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error historyAllergyIntoleranceById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}

	@Override
	public GenericResponseBean searchAllergyIntoleranceById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

			try {
				AllergyIntolerance alergia = client.read().resource(AllergyIntolerance.class).withId(id).execute();				
				String alergiaJson = ctx.newJsonParser().encodeResourceToString(alergia);
				
				responseBean.setCodigo("0000");
				responseBean.setData(om.readValue(alergiaJson, Map.class));
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Alergia no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Alergia fue eliminado");
			}				
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchAllergyIntoleranceById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean searchAllergyIntolerance(RequestSearchAllergyIntolerance request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

		int skipCount = (request.getPageNumber() - 1) * request.getLimit();
		int total = 0;
		Map<String, Object> data = new LinkedHashMap<>();
		try {
			
			Bundle response = client.search()
					.forResource(AllergyIntolerance.class)					
					.where(AllergyIntolerance.CODE.exactly().code(request.getCodeName()))					
					.returnBundle(Bundle.class)
					.execute();

			total = response.getTotal();
			
			System.out.println(" total: " + total);
			if (response.getTotal() > 0) {
				
				lista1 = response.getEntry().stream()
				        .skip(skipCount)
				        .limit(request.getLimit())
				        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.AllergyIntolerance))			        
				        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
				        .collect(Collectors.toList());

				Type listType = new TypeToken<List<Object>>() {}.getType();
				
				data.put("pageNumber", String.valueOf(request.getPageNumber()));
				data.put("resultsPerPage", String.valueOf(request.getLimit()));
				data.put("totalResults", String.valueOf(total));
				data.put("listAllergyIntolerance", new Gson().fromJson(lista1.toString(), listType));				
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
		
			} else 				
				responseBean.setCodigo("6000");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchAllergyIntolerance: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}

}
