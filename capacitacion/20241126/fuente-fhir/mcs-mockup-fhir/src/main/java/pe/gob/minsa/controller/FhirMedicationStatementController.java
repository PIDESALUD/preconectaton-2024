package pe.gob.minsa.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.bean.RequestRegisterMedicationStatement;
import pe.gob.minsa.bean.RequestSearchMedicationStatement;
import pe.gob.minsa.bean.RequestUpdateMedicationStatement;
import pe.gob.minsa.service.FhirMedicationStatementService;

@RestController
@RequestMapping(value = "/servicio/v1.0")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class FhirMedicationStatementController {

	@Autowired
	private FhirMedicationStatementService service;

	@PostMapping(value = "/registerMedicationStatement", produces = "application/json;charset=UTF-8")
	public GenericResponseBean registerMedicationStatement(
			@Validated @RequestBody RequestRegisterMedicationStatement request,
			@RequestHeader("username") String username) {
		return service.registerMedicationStatement(request);
	}
	
	@PostMapping(value = "/updateMedicationStatement", produces = "application/json;charset=UTF-8")
	public GenericResponseBean updateMedicationStatement(@Validated @RequestBody RequestUpdateMedicationStatement request,
			@RequestHeader("username") String username) {
		return service.updateMedicationStatement(request);
	}

	@PostMapping(value = "/searchMedicationStatement", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchPatient(@Validated @RequestBody RequestSearchMedicationStatement request,
			@RequestHeader("username") String username) {
		return service.searchMedicationStatement(request);
	}

	@GetMapping(value = "/searchMedicationStatementById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchAllergyIntoleranceById(
			@NotNull(message = "El campo id es requerido") @Pattern(regexp = "^(\\d*)$", message = "El campo id no cumple con los estandares requeridos, es solo numeros") @PathVariable("id") String id,
			@RequestHeader("username") String username) {
		return service.searchMedicationStatementById(id);
	}

	@GetMapping(value = "/deleteMedicationStatementById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean deleteMedicationStatementById(
			@NotNull(message = "El campo id es requerido") @Pattern(regexp = "^(\\d*)$", message = "El campo id no cumple con los estandares requeridos, es solo numeros") @PathVariable("id") String id,
			@RequestHeader("username") String username) {
		return service.deleteMedicationStatement(id);
	}

	@GetMapping(value = "/historyMedicationStatementById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean historyMedicationStatementById(
			@NotNull(message = "El campo id es requerido") @Pattern(regexp = "^(\\d*)$", message = "El campo id no cumple con los estandares requeridos, es solo numeros") @PathVariable("id") String id,
			@RequestHeader("username") String username) {
		return service.historyMedicationStatementById(id);
	}

}
