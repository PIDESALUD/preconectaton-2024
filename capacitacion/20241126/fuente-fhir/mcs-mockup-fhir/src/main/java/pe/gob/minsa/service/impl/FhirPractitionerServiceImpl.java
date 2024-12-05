package pe.gob.minsa.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.HumanName.NameUse;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Practitioner.PractitionerQualificationComponent;
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
import pe.gob.minsa.bean.RequestRegisterProfesional;
import pe.gob.minsa.bean.RequestSearchProfesional;
import pe.gob.minsa.bean.RequestUpdateProfesional;
import pe.gob.minsa.service.FhirPractitionerService;
import pe.gob.minsa.util.Utilitario;

@Service
public class FhirPractitionerServiceImpl implements FhirPractitionerService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PropertiesBean properties;
	
	private ObjectMapper om = new ObjectMapper();

	@Override
	public GenericResponseBean registerProfesional(RequestRegisterProfesional request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
						
			Map<String, Object> data = new LinkedHashMap<>();			
			
			Practitioner profesional = new Practitioner();		
			profesional.setActive(Boolean.TRUE);		
			
			/********************** VALORES INDENTIFIER ****************************/
			Period period = new Period();
			period.setStart(new Date());
			
			CodeableConcept codeTipo = new CodeableConcept();
			codeTipo.addCoding().setCode(request.getTipoDocumentoProfesional()).setDisplay(Utilitario.getTipoDocumento(request.getTipoDocumentoProfesional()));
			codeTipo.addCoding().setCode(Utilitario.getNomenglaturaPais(request.getNombrePais())).setDisplay(request.getNombrePais());
			
			profesional.addIdentifier()
			.setUse(IdentifierUse.OFFICIAL)
			.setType(codeTipo)		
			.setValue(Utilitario.getNomenglaturaPais(request.getNombrePais())+"/"+request.getDniProfesional()+"-"+request.getDigitoVerificacionProfesional())
			.setPeriod(period);
			
			
			/********************** VALORES NAME ****************************/		
			List<StringType> nombres = new ArrayList<>();
			for(String item: request.getNombresProfesional()) {
				nombres.add(new StringType(item));
			}
			
			profesional.addName()
			.setUse(NameUse.OFFICIAL)
			.setFamily(request.getApellidosProfesional())
			.setGiven(nombres);
			
			/********************** VALORES TELECOM ****************************/
			profesional.addTelecom()
			.setSystem(ContactPointSystem.PHONE)
			.setUse(ContactPointUse.MOBILE)
			.setValue(request.getNroCelularProfesional())
			.setUse(ContactPointUse.MOBILE)
			;
			
			/********************** VALORES BASICOS ****************************/
						
			List<StringType> nombresC = new ArrayList<>();
			for(String item: request.getNombresContactoFamiliar()) {
				nombresC.add(new StringType(item));
			}
			
			profesional.addName()
			.setFamily(request.getApellidosContactoFamiliar())
			.setGiven(nombresC);
			
			
			CodeableConcept codeTipoColegio = new CodeableConcept();
			codeTipoColegio.addCoding().setCode(request.getCodigoColegio()).setDisplay(request.getNombreColegio());
					
			
			profesional.addQualification()
			.setCode(codeTipoColegio)
			.addIdentifier().setValue(request.getNroColegiatura());
						
			MethodOutcome outcome = client
					.create()
					.resource(profesional)
					.execute();

			// Log the ID that the server assigned
			IIdType id = outcome.getId();
			System.out.println("****** Created profesional, got ID: " + id);
			System.out.println("****** ID VALUE: " + id.getIdPartAsLong());
			
			//data.put("url", String.valueOf(id));
			data.put("id", String.valueOf(id.getIdPartAsLong()));
			
			responseBean.setCodigo("0000");
			responseBean.setData(data);
						
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error registerProfesional: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean updateProfesional(RequestUpdateProfesional request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		Map<String, Object> data = new LinkedHashMap<>();
		String valueIdentifierProfesional = "";
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {
				
				Practitioner profesional = client.read().resource(Practitioner.class).withId(request.getIdProfesional()).execute();
				
				if(request.getActivo().trim() != null && request.getActivo().trim().length() > 0 && !request.getActivo().trim().equals("")) {
					
					if(Utilitario.validateActivo(request.getActivo()).equals("0000")) {
						if(request.getActivo().trim().toLowerCase().equals("true"))
							profesional.setActive(Boolean.TRUE);
						
						if(request.getActivo().trim().toLowerCase().equals("false"))
							profesional.setActive(Boolean.FALSE);
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo activo no cumple con los estandares requeridos, es solo [true|false]");
						
						return responseBean;
					}					
					
				}
				
				
				/********************** VALORES INDENTIFIER ****************************/
				Period period = new Period();
				period.setStart(new Date());
				
				CodeableConcept codeTipo = new CodeableConcept();
				
				List<Identifier> lstIdentifier = new ArrayList<>();
				Identifier profesionalIdentifier = new Identifier();
				
				if(request.getTipoDocumentoProfesional().trim() != null && 
					request.getTipoDocumentoProfesional().trim().length() > 0 && 
					 !request.getTipoDocumentoProfesional().trim().equals("")) {					
					if(Utilitario.validateTipoDocumento(request.getTipoDocumentoProfesional()).equals("0000") &&
							request.getTipoDocumentoProfesional().length() == 1) {
						codeTipo.addCoding().setCode(request.getTipoDocumentoProfesional()).setDisplay(Utilitario.getTipoDocumento(request.getTipoDocumentoProfesional()));
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo tipoDocumentoProfesional no cumple con los estandares requeridos, es solo numeros [1-4] y longitud de 1 caracter númerico");
						
						return responseBean;
					}		
				}
				
				if(request.getNombrePais().trim() != null && 
						request.getNombrePais().trim().length() > 0 && 
						 !request.getNombrePais().trim().equals("")) {
					codeTipo.addCoding().setCode(Utilitario.getNomenglaturaPais(request.getNombrePais())).setDisplay(request.getNombrePais());
				}
								
				if(request.getDniProfesional().trim() != null && 
						request.getDniProfesional().trim().length() > 0 && 
						 !request.getDniProfesional().trim().equals("")) {					
					if(Utilitario.validateNumero(request.getDniProfesional()).equals("0000")) {
						valueIdentifierProfesional += Utilitario.getNomenglaturaPais(request.getNombrePais())+"/"+request.getDniProfesional();
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo dniProfesional no cumple con los estandares requeridos, es solo numeros");
						
						return responseBean;
					}	
					
				}
				
				if(request.getDigitoVerificacionProfesional().trim() != null && 
						request.getDigitoVerificacionProfesional().trim().length() > 0 && 
						 !request.getDigitoVerificacionProfesional().trim().equals("")) {
					
					if(Utilitario.validateNumero(request.getDigitoVerificacionProfesional()).equals("0000") && 
							request.getDigitoVerificacionProfesional().length() == 1) {
						valueIdentifierProfesional += "-"+request.getDigitoVerificacionProfesional();
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo digitoVerificacionProfesional no cumple con los estandares requeridos, es solo numeros y longitud de 1 caracter númerico");
						
						return responseBean;
					}	
					
				}
				
				if(request.getTipoDocumentoProfesional().trim() != null && 
						request.getTipoDocumentoProfesional().trim().length() > 0 && 
						 !request.getTipoDocumentoProfesional().trim().equals("") &&						 
					request.getDniProfesional().trim() != null && 
					 request.getDniProfesional().trim().length() > 0 && 
					  !request.getDniProfesional().trim().equals("")) {		
					
					System.out.println(" ** ENTRO IDENTIFIER PROFESIONAL !!!");
					
					profesionalIdentifier
					.setUse(IdentifierUse.OFFICIAL)
					.setType(codeTipo)				
					.setValue(valueIdentifierProfesional)
					.setPeriod(period);					
					
					lstIdentifier.add(profesionalIdentifier);					
					profesional.setIdentifier(lstIdentifier);
					
				}
								
				/********************** VALORES NAME ****************************/	
				List<HumanName> lstHumanName = new ArrayList<>();
				HumanName profesionalHuaman = new HumanName();
				int countH = 0;
				
				if(request.getApellidosProfesional().trim() != null && 
						request.getApellidosProfesional().trim().length() > 0 && 
						 !request.getApellidosProfesional().trim().equals("")) {					
					profesionalHuaman.setFamily(request.getApellidosProfesional());
					countH++;
				}
				
				if(!request.getNombresProfesional().isEmpty() && request.getNombresProfesional() != null) {
					List<StringType> nombres = new ArrayList<>();
					for(String item: request.getNombresProfesional()) {
						nombres.add(new StringType(item));
					}
					
					profesionalHuaman.setGiven(nombres);					
					countH++;
				}
				
				if(countH > 0) {
					profesionalHuaman.setUse(NameUse.OFFICIAL);
					lstHumanName.add(profesionalHuaman);				
					profesional.setName(lstHumanName);	
				}			
											
				/********************** VALORES BASICOS CONTACTOS FAMILIARES ****************************/
				List<StringType> nombresC = new ArrayList<>();
				List<HumanName> lstContactComp = new ArrayList<>();
				HumanName contactos = new HumanName();
				int countCont = 0;
				if(!request.getNombresContactoFamiliar().isEmpty() &&
						request.getNombresContactoFamiliar() != null) {
					for(String item: request.getNombresContactoFamiliar()) {
						nombresC.add(new StringType(item));
					}
					contactos.setGiven(nombresC);
					countCont++;
				}
				
				if(request.getApellidosContactoFamiliar().trim() != null && 
						request.getApellidosContactoFamiliar().trim().length() > 0 && 
						 !request.getApellidosContactoFamiliar().trim().equals("")) {	
					contactos.setFamily(request.getApellidosContactoFamiliar());
					countCont++;
				}
				
				if(countCont>0) {					
					lstContactComp.add(contactos);					
					profesional.setName(lstContactComp);
				}
					
				/********************** VALORES COLEGIATURA ****************************/
				
				CodeableConcept codeTipoColegio = new CodeableConcept();
				List<Identifier> theIdentifierProfesional = new ArrayList<>();
				
				Identifier identifierProfesional = new Identifier();
				
				List<PractitionerQualificationComponent> theQualification = new ArrayList<>();				
				PractitionerQualificationComponent profesionalColegiatura = new PractitionerQualificationComponent();
				
				if(request.getCodigoColegio().trim() != null && 
						request.getCodigoColegio().trim().length() > 0 && 
						 !request.getCodigoColegio().trim().equals("") &&
						 request.getNombreColegio().trim() != null && 
							request.getNombreColegio().trim().length() > 0 && 
							 !request.getNombreColegio().trim().equals("")) {	
					
					codeTipoColegio.addCoding().setCode(request.getCodigoColegio()).setDisplay(request.getNombreColegio());

					profesionalColegiatura.setCode(codeTipoColegio);
					
					identifierProfesional.setValue(request.getNroColegiatura());
					theIdentifierProfesional.add(identifierProfesional);
					
					profesionalColegiatura.setIdentifier(theIdentifierProfesional);
									
					theQualification.add(profesionalColegiatura);				
										
					profesional.setQualification(theQualification);
				}
								
				MethodOutcome outcome = client
						.update()
						.resource(profesional)
						.execute();

				// Log the ID that the server assigned
				IIdType id = outcome.getId();
				System.out.println("****** Update profesional, got ID: " + id);
				System.out.println("****** ID VALUE: " + id.getIdPartAsLong());
				
				//data.put("url", String.valueOf(id));
				data.put("id", String.valueOf(id.getIdPartAsLong()));
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException updateProfesional: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Profesional no existe");
			} catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException updateProfesional: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Profesional fue eliminado");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error updateProfesional: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean deleteProfesional(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {

				MethodOutcome outcome = client
						.delete()
						.resourceById(new IdDt("Practitioner", Long.parseLong(id)))
						.execute();
				
				System.out.println("****** Delete Practitioner, got ID: " + id);
				System.out.println("****** ID VALUE: " + outcome.toString());
				
				responseBean.setCodigo("0000");
				responseBean.setData(null);
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException deleteProfesional: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Profesional no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceGoneException deleteProfesional: " + e.getMessage());
				responseBean.setCodigo("6001");
				responseBean.setData("El id Profesional fue eliminado");
			}			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error deleteProfesional: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean historyProfesionalById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
		Map<String, Object> data = new LinkedHashMap<>();
		boolean borrado = false;
		try {
			
			try {

				Bundle response = client.history()			      
						.onInstance(new IdDt("Practitioner",id))
						.returnBundle(Bundle.class)
						.execute();

				int total = response.getTotal();
				
				System.out.println(" total: " + total);
						
				borrado = response.getEntry().stream().filter(entry -> entry.getRequest().getMethod().name().equals("DELETE"))						
				        .collect(Collectors.toList()).isEmpty();
				
				System.out.println(" borrado: " + borrado);
				if(borrado) {
					
					lista1 = response.getEntry().stream()
					        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.Practitioner))				        
					        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
					        .collect(Collectors.toList());
					
					Type listType = new TypeToken<List<Object>>() {}.getType();
					
					data.put("totalResults", String.valueOf(total));
					data.put("listHistoryPractitioner", new Gson().fromJson(lista1.toString(), listType));				
					
					responseBean.setCodigo("0000");
					responseBean.setData(data);
					
				}else {
					responseBean.setCodigo("6000");
					responseBean.setData("El Profesional fue eliminado");	
				}
				
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException historyProfesionalById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Profesional no existe");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error historyProfesionalById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}

	@Override
	public GenericResponseBean searchProfesionalById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

			try {
				Practitioner profesional = client.read().resource(Practitioner.class).withId(id).execute();				
				String profesionalJson = ctx.newJsonParser().encodeResourceToString(profesional);
				
				responseBean.setCodigo("0000");
				responseBean.setData(om.readValue(profesionalJson, Map.class));
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException searchProfesionalById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Profesional no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException searchProfesionalById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Profesional fue eliminado");
			}				
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchProfesionalById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean searchProfesional(RequestSearchProfesional request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

		int skipCount = (request.getPageNumber() - 1) * request.getLimit();
		int total = 0;
		Map<String, Object> data = new LinkedHashMap<>();
		try {
			
			Bundle response = client.search()
					.forResource(Practitioner.class)					
					//.where(Practitioner.ACTIVE.isMissing(Boolean.FALSE))
					.where(Practitioner.NAME.matches().value(request.getName()))					
					.returnBundle(Bundle.class)
					.execute();

			total = response.getTotal();
			
			System.out.println(" total: " + total);
			if (response.getTotal() > 0) {
				
				lista1 = response.getEntry().stream()
				        .skip(skipCount)
				        .limit(request.getLimit())
				        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.Practitioner))			        
				        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
				        .collect(Collectors.toList());

				Type listType = new TypeToken<List<Object>>() {}.getType();
				
				data.put("pageNumber", String.valueOf(request.getPageNumber()));
				data.put("resultsPerPage", String.valueOf(request.getLimit()));
				data.put("totalResults", String.valueOf(total));
				data.put("listPractitioner", new Gson().fromJson(lista1.toString(), listType));				
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
		
			} else 				
				responseBean.setCodigo("6000");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchPractitioner: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	} 
	




}
