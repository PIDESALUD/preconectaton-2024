package pe.gob.minsa.service.impl;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Period;
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
import pe.gob.minsa.bean.RequestRegisterCondition;
import pe.gob.minsa.bean.RequestSearchCondition;
import pe.gob.minsa.bean.RequestUpdateCondition;
import pe.gob.minsa.service.FhirConditionService;

@Service
public class FhirConditionServiceImpl implements FhirConditionService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PropertiesBean properties;

	private ObjectMapper om = new ObjectMapper();

	@Override
	public GenericResponseBean registerCondition(RequestRegisterCondition request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {

			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

			Map<String, Object> data = new LinkedHashMap<>();

			try {
				Condition condicion = new Condition();

				// C|S|P|D|R|E
				CodeableConcept codeEstatus = new CodeableConcept();
				if (request.getConfirmaCondicion().toUpperCase().trim().equals("C"))
					codeEstatus.addCoding().setCode("confirmed");
				if (request.getConfirmaCondicion().toUpperCase().trim().equals("S"))
					codeEstatus.addCoding().setCode("unconfirmed");
				if (request.getConfirmaCondicion().toUpperCase().trim().equals("P"))
					codeEstatus.addCoding().setCode("provisional");
				if (request.getConfirmaCondicion().toUpperCase().trim().equals("D"))
					codeEstatus.addCoding().setCode("differential");
				if (request.getConfirmaCondicion().toUpperCase().trim().equals("R"))
					codeEstatus.addCoding().setCode("refuted");
				if (request.getConfirmaCondicion().toUpperCase().trim().equals("I"))
					codeEstatus.addCoding().setCode("entered-in-error");

				condicion.setVerificationStatus(codeEstatus);

				CodeableConcept codeTipoYDescrCondicion = new CodeableConcept();
				codeTipoYDescrCondicion.addCoding().setCode(request.getCodigoCondicion())
						.setDisplay(request.getDescrCondicion());
				codeTipoYDescrCondicion.setText(request.getDescrCondicion());
				condicion.setCode(codeTipoYDescrCondicion);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Period period = new Period();
				period.setStart(sdf.parse(request.getFechaCondicion()));
				condicion.setOnset(period);

				// DateTimeType ret = new DateTimeType(request.getFechaCondicion());
				// condicion.setOnset(ret);

				if (!request.getComentarios().trim().equals("") && request.getComentarios() != null)
					condicion.addNote().setText(request.getComentarios());

				/**********************
				 * INICIO VALIDACION DE PACIENTE
				 ****************************/
				Reference referencePatient = new Reference();
				referencePatient.setReference("Patient/" + request.getIdPaciente());

				condicion.setSubject(referencePatient);

				MethodOutcome outcome = client.create().resource(condicion).execute();

				IIdType id = outcome.getId();
				System.out.println("****** Created Condition, got ID: " + id);
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
			logger.error(" ** Error registerCondition: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean updateCondition(RequestUpdateCondition request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		Map<String, Object> data = new LinkedHashMap<>();

		try {
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {

				Condition condicion = client.read().resource(Condition.class)
						.withId(request.getIdCondition()).execute();
				
				try {
					
					if(request.getFechaCondicion().trim() != null && 
							request.getFechaCondicion().trim().length() > 0 && 
							!request.getFechaCondicion().trim().equals("")) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Period period = new Period();
						period.setStart(sdf.parse(request.getFechaCondicion()));
						condicion.setOnset(period);
					}
					
					if(request.getCodigoCondicion().trim() != null && 
							request.getCodigoCondicion().trim().length() > 0 && 
							!request.getCodigoCondicion().trim().equals("") &&
							request.getDescrCondicion().trim() != null && 
							request.getDescrCondicion().trim().length() > 0 && 
							!request.getDescrCondicion().trim().equals("")) {
						
						CodeableConcept codeTipoYDescrCondicion = new CodeableConcept();
						codeTipoYDescrCondicion.addCoding().setCode(request.getCodigoCondicion())
								.setDisplay(request.getDescrCondicion());
						codeTipoYDescrCondicion.setText(request.getDescrCondicion());
						condicion.setCode(codeTipoYDescrCondicion);
						
					}
					
					if(request.getConfirmaCondicion().trim() != null && 
							request.getConfirmaCondicion().trim().length() > 0 && 
							!request.getConfirmaCondicion().trim().equals("")) {
						CodeableConcept codeEstatus = new CodeableConcept();
						if (request.getConfirmaCondicion().toUpperCase().trim().equals("C"))
							codeEstatus.addCoding().setCode("confirmed");
						if (request.getConfirmaCondicion().toUpperCase().trim().equals("S"))
							codeEstatus.addCoding().setCode("unconfirmed");
						if (request.getConfirmaCondicion().toUpperCase().trim().equals("P"))
							codeEstatus.addCoding().setCode("provisional");
						if (request.getConfirmaCondicion().toUpperCase().trim().equals("D"))
							codeEstatus.addCoding().setCode("differential");
						if (request.getConfirmaCondicion().toUpperCase().trim().equals("R"))
							codeEstatus.addCoding().setCode("refuted");
						if (request.getConfirmaCondicion().toUpperCase().trim().equals("I"))
							codeEstatus.addCoding().setCode("entered-in-error");

						condicion.setVerificationStatus(codeEstatus);
					}
					
					if (!request.getComentarios().trim().equals("") && request.getComentarios() != null) {
						List<Annotation> theNote = new ArrayList<>();
						Annotation annotation = new Annotation();
						annotation.setText(request.getComentarios());						
						theNote.add(annotation);
						condicion.setNote(theNote);						
					}
						
					
					Reference referencePatient = new Reference();
					referencePatient.setReference("Patient/" + request.getIdPaciente());

					condicion.setSubject(referencePatient);

					MethodOutcome outcome = client.update().resource(condicion).execute();

					// Log the ID that the server assigned
					IIdType id = outcome.getId();
					System.out.println("****** Update Condition, got ID: " + id);
					System.out.println("****** ID VALUE: " + id.getIdPartAsLong());

					// data.put("url", String.valueOf(id));
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
				logger.error(" ** Error ResourceNotFoundException updateCondition: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Condition no existe");
			} catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException updateCondition: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Condition fue eliminado");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error updateCondition: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean deleteCondition(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {
				
				MethodOutcome outcome = client
						.delete()
						.resourceById(new IdDt("Condition", Long.parseLong(id)))
						.execute();
				
				System.out.println("****** Delete Condition, got ID: " + id);
				System.out.println("****** ID VALUE: " + outcome.toString());
				
				responseBean.setCodigo("0000");
				responseBean.setData(null);
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException deleteCondition: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Condition no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceGoneException deleteCondition: " + e.getMessage());
				responseBean.setCodigo("6001");
				responseBean.setData("El id Condition fue eliminado");
			}			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error deleteCondition: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean historyConditionById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
		Map<String, Object> data = new LinkedHashMap<>();
		boolean borrado = false;
		try {
			
			try {

				Bundle response = client.history()			      
						.onInstance(new IdDt("Condition",id))
						.returnBundle(Bundle.class)
						.execute();

				int total = response.getTotal();
				
				System.out.println(" total: " + total);
						
				borrado = response.getEntry().stream().filter(entry -> entry.getRequest().getMethod().name().equals("DELETE"))						
				        .collect(Collectors.toList()).isEmpty();
				
				System.out.println(" borrado: " + borrado);
				if(borrado) {
					
					lista1 = response.getEntry().stream()
					        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.Condition))				        
					        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
					        .collect(Collectors.toList());
					
					Type listType = new TypeToken<List<Object>>() {}.getType();
					
					data.put("totalResults", String.valueOf(total));
					data.put("listHistoryCondition", new Gson().fromJson(lista1.toString(), listType));				
					
					responseBean.setCodigo("0000");
					responseBean.setData(data);
					
				}else {
					responseBean.setCodigo("6000");
					responseBean.setData("El id Condicion fue eliminado");	
				}
				
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException historyConditionById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Condicion no existe");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error historyConditionById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}

	@Override
	public GenericResponseBean searchConditionById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

			try {
				Condition condicion = client.read().resource(Condition.class).withId(id).execute();				
				String conditionJson = ctx.newJsonParser().encodeResourceToString(condicion);
				
				responseBean.setCodigo("0000");
				responseBean.setData(om.readValue(conditionJson, Map.class));
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Condicion no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Condicion fue eliminado");
			}				
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchConditionById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean searchCondition(RequestSearchCondition request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

		int skipCount = (request.getPageNumber() - 1) * request.getLimit();
		int total = 0;
		Map<String, Object> data = new LinkedHashMap<>();
		try {
			
			Bundle response = client.search()
					.forResource(Condition.class)					
					.where(Condition.CODE.exactly().code(request.getCodeName()))									
					.returnBundle(Bundle.class)
					.execute();

			total = response.getTotal();
			
			System.out.println(" total: " + total);
			if (response.getTotal() > 0) {
				
				lista1 = response.getEntry().stream()
				        .skip(skipCount)
				        .limit(request.getLimit())
				        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.Condition))			        
				        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
				        .collect(Collectors.toList());

				Type listType = new TypeToken<List<Object>>() {}.getType();
				
				data.put("pageNumber", String.valueOf(request.getPageNumber()));
				data.put("resultsPerPage", String.valueOf(request.getLimit()));
				data.put("totalResults", String.valueOf(total));
				data.put("listCondition", new Gson().fromJson(lista1.toString(), listType));				
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
		
			} else 				
				responseBean.setCodigo("6000");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchCondition: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}

}
