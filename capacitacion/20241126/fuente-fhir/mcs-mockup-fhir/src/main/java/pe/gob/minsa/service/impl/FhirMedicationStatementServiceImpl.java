package pe.gob.minsa.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.MedicationStatement.MedicationStatementStatus;
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
import pe.gob.minsa.bean.RequestRegisterMedicationStatement;
import pe.gob.minsa.bean.RequestSearchMedicationStatement;
import pe.gob.minsa.bean.RequestUpdateMedicationStatement;
import pe.gob.minsa.service.FhirMedicationStatementService;

@Service
public class FhirMedicationStatementServiceImpl implements FhirMedicationStatementService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PropertiesBean properties;

	private ObjectMapper om = new ObjectMapper();

	@Override
	public GenericResponseBean registerMedicationStatement(RequestRegisterMedicationStatement request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {

			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

			Map<String, Object> data = new LinkedHashMap<>();

			try {
				MedicationStatement medicamento = new MedicationStatement();

				medicamento.setStatus(MedicationStatementStatus.ACTIVE);

				CodeableConcept codeMedication = new CodeableConcept();
				codeMedication.addCoding().setCode(request.getCodigoProdFarmaceutico())
						.setDisplay(request.getDescrProdFarmaceutico());
				codeMedication.setText(request.getDescrProdFarmaceutico());
				medicamento.setMedication(codeMedication);

				DateTimeType ret = new DateTimeType(request.getFechaPrescripcion());
				medicamento.setEffective(ret);

				CodeableConcept codeMedicationDosage = new CodeableConcept();
				codeMedicationDosage.addCoding().setCode(request.getCantidad());
				codeMedicationDosage.setText(request.getVia());
				medicamento.addDosage().setText(request.getDescrDosis()).setRoute(codeMedicationDosage);

				/****************************/
				Reference referencePatient = new Reference();				
				referencePatient.setReference("Patient/" + request.getIdPaciente());

				medicamento.setSubject(referencePatient);

				MethodOutcome outcome = client.create().resource(medicamento).execute();

				IIdType id = outcome.getId();
				System.out.println("****** Created MedicationStatement, got ID: " + id);
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
			logger.error(" ** Error registerMedicationStatement: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public GenericResponseBean updateMedicationStatement(RequestUpdateMedicationStatement request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		Map<String, Object> data = new LinkedHashMap<>();

		try {
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {

				MedicationStatement medicamento = client.read().resource(MedicationStatement.class)
						.withId(request.getIdMedicationStatement()).execute();

				try {

					if (request.getEstadoMedicamento().trim() != null
							&& request.getEstadoMedicamento().trim().length() > 0
							&& !request.getEstadoMedicamento().trim().equals("")) {
						if (request.getEstadoMedicamento().trim().toUpperCase().equals("A"))
							medicamento.setStatus(MedicationStatementStatus.ACTIVE);
						if (request.getEstadoMedicamento().trim().toUpperCase().equals("T"))
							medicamento.setStatus(MedicationStatementStatus.COMPLETED);
						if (request.getEstadoMedicamento().trim().toUpperCase().equals("P"))
							medicamento.setStatus(MedicationStatementStatus.ENTEREDINERROR);
						if (request.getEstadoMedicamento().trim().toUpperCase().equals("E"))
							medicamento.setStatus(MedicationStatementStatus.ONHOLD);
						if (request.getEstadoMedicamento().trim().toUpperCase().equals("D"))
							medicamento.setStatus(MedicationStatementStatus.UNKNOWN);
						if (request.getEstadoMedicamento().trim().toUpperCase().equals("NT"))
							medicamento.setStatus(MedicationStatementStatus.NOTTAKEN);
					}

					if (request.getCodigoProdFarmaceutico().trim() != null
							&& request.getCodigoProdFarmaceutico().trim().length() > 0
							&& !request.getCodigoProdFarmaceutico().trim().equals("")
							&& request.getDescrProdFarmaceutico().trim() != null
							&& request.getDescrProdFarmaceutico().trim().length() > 0
							&& !request.getDescrProdFarmaceutico().trim().equals("")) {

						CodeableConcept codeMedication = new CodeableConcept();
						codeMedication.addCoding().setCode(request.getCodigoProdFarmaceutico())
								.setDisplay(request.getDescrProdFarmaceutico());
						codeMedication.setText(request.getDescrProdFarmaceutico());
						medicamento.setMedication(codeMedication);
					}

					if (request.getFechaPrescripcion().trim() != null
							&& request.getFechaPrescripcion().trim().length() > 0
							&& !request.getFechaPrescripcion().trim().equals("")) {
						DateTimeType ret = new DateTimeType(request.getFechaPrescripcion());
						medicamento.setEffective(ret);
					}

					CodeableConcept codeMedicationDosage = new CodeableConcept();
					int contDosage1 = 0;

					if (request.getCantidad().trim() != null && request.getCantidad().trim().length() > 0
							&& !request.getCantidad().trim().equals("")) {
						codeMedicationDosage.addCoding().setCode(request.getCantidad());
						contDosage1++;
					}

					if (request.getVia().trim() != null && request.getVia().trim().length() > 0
							&& !request.getVia().trim().equals("")) {
						codeMedicationDosage.setText(request.getVia());
						contDosage1++;
					}

					List<Dosage> theDosage = new ArrayList();
					Dosage ds = new Dosage();

					if (contDosage1 > 0) {
						ds.setRoute(codeMedicationDosage);
					}

					if (request.getDescrDosis().trim() != null && request.getDescrDosis().trim().length() > 0
							&& !request.getDescrDosis().trim().equals(""))
						ds.setText(request.getDescrDosis());

					theDosage.add(ds);

					medicamento.setDosage(theDosage);

					Reference referencePatient = new Reference();
					referencePatient.setReference("Patient/" + request.getIdPaciente());

					medicamento.setSubject(referencePatient);

					MethodOutcome outcome = client.update().resource(medicamento).execute();

					// Log the ID that the server assigned
					IIdType id = outcome.getId();
					System.out.println("****** Update MedicationStatement, got ID: " + id);
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
				logger.error(" ** Error ResourceNotFoundException updateMedicationStatement: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id medicamento no existe");
			} catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException updateMedicationStatement: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id medicamento fue eliminado");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error updateMedicationStatement: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean deleteMedicationStatement(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
			try {
				
				MethodOutcome outcome = client
						.delete()
						.resourceById(new IdDt("MedicationStatement", Long.parseLong(id)))
						.execute();
				
				System.out.println("****** Delete MedicationStatement, got ID: " + id);
				System.out.println("****** ID VALUE: " + outcome.toString());
				
				responseBean.setCodigo("0000");
				responseBean.setData(null);
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException deleteMedicationStatement: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id MedicationStatement no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceGoneException deleteMedicationStatement: " + e.getMessage());
				responseBean.setCodigo("6001");
				responseBean.setData("El id MedicationStatement fue eliminado");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error deleteMedicationStatement: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean historyMedicationStatementById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());
		Map<String, Object> data = new LinkedHashMap<>();
		boolean borrado = false;
		try {
			
			try {

				Bundle response = client.history()			      
						.onInstance(new IdDt("MedicationStatement",id))
						.returnBundle(Bundle.class)
						.execute();

				int total = response.getTotal();
				
				System.out.println(" total: " + total);
						
				borrado = response.getEntry().stream().filter(entry -> entry.getRequest().getMethod().name().equals("DELETE"))						
				        .collect(Collectors.toList()).isEmpty();
				
				System.out.println(" borrado: " + borrado);
				if(borrado) {
					
					lista1 = response.getEntry().stream()
					        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.MedicationStatement))				        
					        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
					        .collect(Collectors.toList());
					
					Type listType = new TypeToken<List<Object>>() {}.getType();
					
					data.put("totalResults", String.valueOf(total));
					data.put("listHistoryMedicationStatement", new Gson().fromJson(lista1.toString(), listType));				
					
					responseBean.setCodigo("0000");
					responseBean.setData(data);
					
				}else {
					responseBean.setCodigo("6000");
					responseBean.setData("El id MedicationStatement fue eliminado");	
				}
				
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException historyMedicationStatementById: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id MedicationStatement no existe");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error historyMedicationStatementById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}

	@Override
	public GenericResponseBean searchMedicationStatementById(String id) {
		GenericResponseBean responseBean = new GenericResponseBean();
		try {
			
			FhirContext ctx = FhirContext.forR4();
			IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

			try {
				MedicationStatement medicamento = client.read().resource(MedicationStatement.class).withId(id).execute();				
				String medicamentoJson = ctx.newJsonParser().encodeResourceToString(medicamento);
				
				responseBean.setCodigo("0000");
				responseBean.setData(om.readValue(medicamentoJson, Map.class));
				
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Medicamento no existe");
			}catch (ResourceGoneException e) {
				e.printStackTrace();
				logger.error(" ** Error ResourceNotFoundException: " + e.getMessage());
				responseBean.setCodigo("6000");
				responseBean.setData("EL id Medicamento fue eliminado");
			}				
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchMedicationStatementById: " + e.getMessage());
			responseBean.setCodigo("9000");
		}
		return responseBean;
	}

	@Override
	public GenericResponseBean searchMedicationStatement(RequestSearchMedicationStatement request) {
		GenericResponseBean responseBean = new GenericResponseBean();
		List<Object> lista1 = new ArrayList<>();
		
		FhirContext ctx = FhirContext.forR4();
		IGenericClient client = ctx.newRestfulGenericClient(properties.getUrl());

		int skipCount = (request.getPageNumber() - 1) * request.getLimit();
		int total = 0;
		Map<String, Object> data = new LinkedHashMap<>();
		try {
			
			Bundle response = client.search()
					.forResource(MedicationStatement.class)					
					.where(MedicationStatement.CODE.exactly().code(request.getCodeName()))									
					.returnBundle(Bundle.class)
					.execute();

			total = response.getTotal();
			
			System.out.println(" total: " + total);
			if (response.getTotal() > 0) {
				
				lista1 = response.getEntry().stream()
				        .skip(skipCount)
				        .limit(request.getLimit())
				        .filter(entry -> entry.getResource().getResourceType().equals(ResourceType.MedicationStatement))			        
				        .map(entry -> ctx.newJsonParser().encodeResourceToString(entry.getResource()))
				        .collect(Collectors.toList());

				Type listType = new TypeToken<List<Object>>() {}.getType();
				
				data.put("pageNumber", String.valueOf(request.getPageNumber()));
				data.put("resultsPerPage", String.valueOf(request.getLimit()));
				data.put("totalResults", String.valueOf(total));
				data.put("listMedicationStatement", new Gson().fromJson(lista1.toString(), listType));				
				
				responseBean.setCodigo("0000");
				responseBean.setData(data);
		
			} else 				
				responseBean.setCodigo("6000");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" ** Error searchMedicationStatement: " + e.getMessage());
			responseBean.setCodigo("9000");
		}

		return responseBean;
	}

}
