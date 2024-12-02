package pe.gob.minsa.service.impl;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Address.AddressUse;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.HumanName.NameUse;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Patient.ContactComponent;
import org.hl7.fhir.r4.model.Period;
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
import pe.gob.minsa.bean.RequestRegisterPatient;
import pe.gob.minsa.bean.RequestSearchPatient;
import pe.gob.minsa.bean.RequestUpdatePatient;
import pe.gob.minsa.service.FhirPatientService;
import pe.gob.minsa.util.Utilitario;

@Service
public class FhirPatientServiceImpl implements FhirPatientService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PropertiesBean properties;
	
	private ObjectMapper om = new ObjectMapper(); 
	
	@Override
	public GenericResponseBean searchPatientById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

			try {
				Patient patient = client.read().resource(Patient.class).withId(id).execute();				
				String patientJson = ctx.newJsonParser().encodeResourceToString(patient);
				
				responseBean.setCodigo("0000");
				responseBean.setData(om.readValue(patientJson, Map.class));
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id paciente no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id paciente fue eliminado");
			}				
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchPatientById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	
	@Override
	public GenericResponseBean searchPatient(RequestSearchPatient request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

		int skipCount = (request.getPageNumber() - 1) * request.getLimit();
		int total = 0;
		Map<String, Object> data = new LinkedHashMap<>();
		try {
			
			Bundle response = client.search()
					.forResource(Patient.class)					
					.where(Patient.NAME.matches().value(request.getName()))					
					.returnBundle(Bundle.class)
					.execute();

			total = response.getTotal();
			
			System.out.println(" total: " + total);
			if (response.getTotal() > 0) {
				
				lista1 = response.getEntry().stream()
				        .skip(skipCount)
				        .limit(request.getLimit())
				        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.Patient))			        
				        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
				        .collect(Collectors.toList());

				Type listType = new TypeToken<List<Object>>() {}.getType();
				
				data.put("pageNumber", String.valueOf(request.getPageNumber()));
				data.put("resultsPerPage", String.valueOf(request.getLimit()));
				data.put("totalResults", String.valueOf(total));
				data.put("listPatient", new Gson().fromJson(lista1.toString(), listType));				
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
		
			} else 				
				responseBean.setCodigo("6000");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchPatient: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}


	@Override
	public GenericResponseBean registerPatient(RequestRegisterPatient request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
						
			Map<String, Object> data = new LinkedHashMap<>();			
			
			Patient paciente = new Patient();		
			paciente.setActive(Boolean.TRUE);		
			
			/********************** VALORES INDENTIFIER ****************************/
			Period period = new Period();
			period.setStart(new Date());
			
			CodeableConcept codeTipo = new CodeableConcept();
			codeTipo.addCoding().setCode(request.getTipoDocumentoPaciente()).setDisplay(Utilitario.getTipoDocumento(request.getTipoDocumentoPaciente()));
			codeTipo.addCoding().setCode(Utilitario.getNomenglaturaPais(request.getNombrePais())).setDisplay(request.getNombrePais());
			
			paciente.addIdentifier()
			.setUse(IdentifierUse.OFFICIAL)
			.setType(codeTipo)		
			.setValue(Utilitario.getNomenglaturaPais(request.getNombrePais())+"/"+request.getDniPaciente()+"-"+request.getDigitoVerificacionPaciente())
			.setPeriod(period);
			
			
			/********************** VALORES NAME ****************************/		
			List<StringType> nombres = new ArrayList<>();
			for(String item: request.getNombresPaciente()) {
				nombres.add(new StringType(item));
			}
			
			paciente.addName()
			.setUse(NameUse.OFFICIAL)
			.setFamily(request.getApellidosPaciente())
			.setGiven(nombres);
			
			/********************** VALORES TELECOM ****************************/
			paciente.addTelecom()
			.setSystem(ContactPointSystem.PHONE)
			.setUse(ContactPointUse.MOBILE)
			.setValue(request.getNroCelularPaciente())
			.setUse(ContactPointUse.MOBILE)
			;
			
			/********************** VALORES BASICOS ****************************/
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(request.getGeneroPaciente().equals("M"))
				paciente.setGender(AdministrativeGender.MALE);	
			if(request.getGeneroPaciente().equals("F"))
				paciente.setGender(AdministrativeGender.FEMALE);
			
			paciente.setBirthDate(sdf.parse(request.getFechaNacimientoPaciente()));
			paciente.addAddress()
			.setUse(AddressUse.HOME)
			.setText(request.getDireccionPaciente())
			.setCity(request.getProvinciaPaciente())			
			.setDistrict(request.getDistritoPaciente())
			.setState(request.getDepartamentoPaciente());
			
			List<StringType> nombresC = new ArrayList<>();
			for(String item: request.getNombresContactoFamiliar()) {
				nombresC.add(new StringType(item));
			}
			
			HumanName contactos = new HumanName();
			contactos.setFamily(request.getApellidosContactoFamiliar());
			contactos.setGiven(nombresC);
			
			
			paciente.addContact()
			.setName(contactos)
			.addTelecom()
			.setSystem(ContactPointSystem.PHONE)
			.setValue(request.getNroContactoFamiliar());
			
			MethodOutcome outcome = client
					.create()
					.resource(paciente)
					.execute();

			// Log the ID that the server assigned
			IIdType id = outcome.getId();
			System.out.println("****** Created patient, got ID: " + id);
			System.out.println("****** ID VALUE: " + id.getIdPartAsLong());
			
			//data.put("url", String.valueOf(id));
			data.put("id", String.valueOf(id.getIdPartAsLong()));
			
			responseBean.setCodigo("0000");
			responseBean.setData(data);
						
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error registerPatient: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}


	@Override
	public GenericResponseBean deletePatient(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {

				MethodOutcome outcome = client
						.delete()
						.resourceById(new IdDt("Patient", Long.parseLong(id)))
						.execute();
				
				System.out.println("****** Delete patient, got ID: " + id);
				System.out.println("****** ID VALUE: " + outcome.toString());
				
				responseBean.setCodigo("0000");
				responseBean.setData(null);
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException deletePatient: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id paciente no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceGoneException deletePatient: " + e.getMessage());
				responseBean.setCodigo("6001");
				responseBean.setData("El id paciente fue eliminado");
			}			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error deletePatient: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}


	@Override
	public GenericResponseBean updatePatient(RequestUpdatePatient request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		Map<String, Object> data = new LinkedHashMap<>();
		String valueIdentifierPatient = "";
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {
				
				Patient paciente = client.read().resource(Patient.class).withId(request.getIdPaciente()).execute();
				
				if(request.getActivo().trim() != null && request.getActivo().trim().length() > 0 && !request.getActivo().trim().equals("")) {
					
					if(Utilitario.validateActivo(request.getActivo()).equals("0000")) {
						if(request.getActivo().trim().toLowerCase().equals("true"))
							paciente.setActive(Boolean.TRUE);
						
						if(request.getActivo().trim().toLowerCase().equals("false"))
							paciente.setActive(Boolean.FALSE);
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
				Identifier patientIdentifier = new Identifier();
				
				if(request.getTipoDocumentoPaciente().trim() != null && 
					request.getTipoDocumentoPaciente().trim().length() > 0 && 
					 !request.getTipoDocumentoPaciente().trim().equals("")) {					
					if(Utilitario.validateTipoDocumento(request.getTipoDocumentoPaciente()).equals("0000") &&
							request.getTipoDocumentoPaciente().length() == 1) {
						codeTipo.addCoding().setCode(request.getTipoDocumentoPaciente()).setDisplay(Utilitario.getTipoDocumento(request.getTipoDocumentoPaciente()));
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo tipoDocumentoPaciente no cumple con los estandares requeridos, es solo numeros [1-4] y longitud de 1 caracter númerico");
						
						return responseBean;
					}		
				}
				
				if(request.getNombrePais().trim() != null && 
						request.getNombrePais().trim().length() > 0 && 
						 !request.getNombrePais().trim().equals("")) {
					codeTipo.addCoding().setCode(Utilitario.getNomenglaturaPais(request.getNombrePais())).setDisplay(request.getNombrePais());
				}
								
				if(request.getDniPaciente().trim() != null && 
						request.getDniPaciente().trim().length() > 0 && 
						 !request.getDniPaciente().trim().equals("")) {					
					if(Utilitario.validateNumero(request.getDniPaciente()).equals("0000")) {
						valueIdentifierPatient += Utilitario.getNomenglaturaPais(request.getNombrePais())+"/"+request.getDniPaciente();
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo dniPaciente no cumple con los estandares requeridos, es solo numeros");
						
						return responseBean;
					}	
					
				}
				
				if(request.getDigitoVerificacionPaciente().trim() != null && 
						request.getDigitoVerificacionPaciente().trim().length() > 0 && 
						 !request.getDigitoVerificacionPaciente().trim().equals("")) {
					
					if(Utilitario.validateNumero(request.getDigitoVerificacionPaciente()).equals("0000") && 
							request.getDigitoVerificacionPaciente().length() == 1) {
						valueIdentifierPatient += "-"+request.getDigitoVerificacionPaciente();
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo digitoVerificacionPaciente no cumple con los estandares requeridos, es solo numeros y longitud de 1 caracter númerico");
						
						return responseBean;
					}	
					
				}
				
				if(request.getTipoDocumentoPaciente().trim() != null && 
						request.getTipoDocumentoPaciente().trim().length() > 0 && 
						 !request.getTipoDocumentoPaciente().trim().equals("") &&						 
					request.getDniPaciente().trim() != null && 
					 request.getDniPaciente().trim().length() > 0 && 
					  !request.getDniPaciente().trim().equals("")) {		
					
					patientIdentifier
					.setUse(IdentifierUse.OFFICIAL)
					.setType(codeTipo)				
					.setValue(valueIdentifierPatient)
					.setPeriod(period);					
					
					lstIdentifier.add(patientIdentifier);					
					paciente.setIdentifier(lstIdentifier);
					
				}
								
				/********************** VALORES NAME ****************************/	
				List<HumanName> lstHumanName = new ArrayList<>();
				HumanName patientHuaman = new HumanName();
				int countH = 0;
				
				if(request.getApellidosPaciente().trim() != null && 
						request.getApellidosPaciente().trim().length() > 0 && 
						 !request.getApellidosPaciente().trim().equals("")) {					
					patientHuaman.setFamily(request.getApellidosPaciente());
					countH++;
				}
				
				if(!request.getNombresPaciente().isEmpty() && request.getNombresPaciente() != null) {
					List<StringType> nombres = new ArrayList<>();
					for(String item: request.getNombresPaciente()) {
						nombres.add(new StringType(item));
					}
					
					patientHuaman.setGiven(nombres);					
					countH++;
				}
				
				if(countH > 0) {
					patientHuaman.setUse(NameUse.OFFICIAL);
					lstHumanName.add(patientHuaman);				
					paciente.setName(lstHumanName);	
				}				
				
				/********************** VALORES TELECOM ****************************/
				
				if(request.getNroCelularPaciente().trim() != null && 
						request.getNroCelularPaciente().trim().length() > 0 && 
						 !request.getNroCelularPaciente().trim().equals("")) {					
					
					if(Utilitario.validateNumero(request.getNroCelularPaciente()).equals("0000")) {						
						
						List<ContactPoint> lstContact = new ArrayList<>();
						ContactPoint patientContact = new ContactPoint();
						patientContact
						.setSystem(ContactPointSystem.PHONE)
						.setUse(ContactPointUse.MOBILE)
						.setValue(request.getNroCelularPaciente())
						.setUse(ContactPointUse.MOBILE);
						
						lstContact.add(patientContact);
						
						paciente.setTelecom(lstContact);
						
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo nroCelularPaciente no cumple con los estandares requeridos, es solo numeros");
						
						return responseBean;
					}	
					
				}
				
				
				/********************** VALORES BASICOS ****************************/
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				if(request.getGeneroPaciente().trim() != null && 
						request.getGeneroPaciente().trim().length() > 0 && 
						 !request.getGeneroPaciente().trim().equals("")) {		
					
					if(Utilitario.validateGenero(request.getGeneroPaciente()).equals("0000")) {						
						
						if(request.getGeneroPaciente().toUpperCase().equals("M"))
							paciente.setGender(AdministrativeGender.MALE);	
						if(request.getGeneroPaciente().toUpperCase().equals("F"))
							paciente.setGender(AdministrativeGender.FEMALE);
						
						
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo generoPaciente debe contener el siguiente valor (M - F)");
						
						return responseBean;
					}						
					
				}
				
				if(request.getFechaNacimientoPaciente().trim() != null && 
						request.getFechaNacimientoPaciente().trim().length() > 0 && 
						 !request.getFechaNacimientoPaciente().trim().equals("")) {							
					if(Utilitario.validateFechaNacimiento(request.getFechaNacimientoPaciente()).equals("0000")) {
						paciente.setBirthDate(sdf.parse(request.getFechaNacimientoPaciente()));
					}else {
						responseBean.setCodigo("6000");
						responseBean.setData("El campo fechaNacimientoPaciente  no cumple con los estandares requeridos YYYY-MM-DD o la fecha es invalida");
						
						return responseBean;
					}						
					
				}
				
				List<Address> lstAddress = new ArrayList<>();
				Address patientAddress = new Address();
				int countPA = 0;
				
				if(request.getDireccionPaciente().trim() != null && 
						request.getDireccionPaciente().trim().length() > 0 && 
						 !request.getDireccionPaciente().trim().equals("")) {	
					patientAddress.setText(request.getDireccionPaciente());
					countPA++;
				}
				
				if(request.getProvinciaPaciente().trim() != null && 
						request.getProvinciaPaciente().trim().length() > 0 && 
						 !request.getProvinciaPaciente().trim().equals("")) {	
					patientAddress.setCity(request.getProvinciaPaciente());	
					countPA++;
				}
				
				if(request.getDistritoPaciente().trim() != null && 
						request.getDistritoPaciente().trim().length() > 0 && 
						 !request.getDistritoPaciente().trim().equals("")) {	
					patientAddress.setDistrict(request.getDistritoPaciente());	
					countPA++;
				}
				
				if(request.getDepartamentoPaciente().trim() != null && 
						request.getDepartamentoPaciente().trim().length() > 0 && 
						 !request.getDepartamentoPaciente().trim().equals("")) {	
					patientAddress.setState(request.getDepartamentoPaciente());	
					countPA++;
				}
				
				if(countPA>0) {
					patientAddress.setUse(AddressUse.HOME);
					lstAddress.add(patientAddress);				
					paciente.setAddress(lstAddress);
				}
				
				/********************** VALORES BASICOS CONTACTOS FAMILIARES ****************************/
				List<StringType> nombresC = new ArrayList<>();
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
				
				List<ContactComponent> lstContactComp = new ArrayList<>();
				ContactComponent patientContactComp = new ContactComponent();				
				int countPatientContactComp = 0;
				
				if(countCont>0) {
					patientContactComp.setName(contactos);
					countPatientContactComp++;
				}
				
				if(request.getNroContactoFamiliar().trim() != null && 
						request.getNroContactoFamiliar().trim().length() > 0 && 
						 !request.getNroContactoFamiliar().trim().equals("")) {										
					
					List<ContactPoint> lstContactPoint = new ArrayList<>();
					ContactPoint patientContactPoint = new ContactPoint();
					
					patientContactPoint.setSystem(ContactPointSystem.PHONE)
					.setValue(request.getNroContactoFamiliar());
					
					patientContactComp.setTelecom(lstContactPoint);
					countPatientContactComp++;
				}
				
				if(countPatientContactComp>0) {					
					lstContactComp.add(patientContactComp);				
					paciente.setContact(lstContactComp);					
					
				}				
				
				MethodOutcome outcome = client
						.update()
						.resource(paciente)
						.execute();

				// Log the ID that the server assigned
				IIdType id = outcome.getId();
				System.out.println("****** Update patient, got ID: " + id);
				System.out.println("****** ID VALUE: " + id.getIdPartAsLong());
				
				//data.put("url", String.valueOf(id));
				data.put("id", String.valueOf(id.getIdPartAsLong()));
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException updatePatient: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id paciente no existe");
			} catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException updatePatient: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id paciente fue eliminado");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error updatePatient: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}


	@SuppressWarnings("static-access")
	@Override
	public GenericResponseBean historyPatientById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
		Map<String, Object> data = new LinkedHashMap<>();
		boolean borrado = false;
		try {
			
			try {

				Bundle response = client.history()			      
						.onInstance(new IdDt("Patient",id))
						.returnBundle(Bundle.class)
						.execute();

				int total = response.getTotal();
				
				System.out.println(" total: " + total);
						
				borrado = response.getEntry().stream().filter(entry -> entry.getRequest().getMethod().name().equals("DELETE"))						
				        .collect(Collectors.toList()).isEmpty();
				
				System.out.println(" borrado: " + borrado);
				if(borrado) {
					
					lista1 = response.getEntry().stream()
					        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.Patient))				        
					        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
					        .collect(Collectors.toList());
					
					Type listType = new TypeToken<List<Object>>() {}.getType();
					
					data.put("totalResults", String.valueOf(total));
					data.put("listHistoryPatient", new Gson().fromJson(lista1.toString(), listType));				
					
					responseBean.setCodigo("0000");
					responseBean.setData(data);
					
				}else {
					responseBean.setCodigo("6000");
					responseBean.setData("El paciente fue eliminado");	
				}
				
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException historyPatientById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id paciente no existe");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error historyPatientById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}






}
