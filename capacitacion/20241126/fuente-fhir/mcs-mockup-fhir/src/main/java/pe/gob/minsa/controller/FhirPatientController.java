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
import pe.gob.minsa.bean.RequestRegisterPatient;
import pe.gob.minsa.bean.RequestSearchPatient;
import pe.gob.minsa.bean.RequestUpdatePatient;
import pe.gob.minsa.service.FhirPatientService;

@RestController
@RequestMapping(value = "/servicio/v1.0")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class FhirPatientController {

	@Autowired
	private FhirPatientService service;
	
	@PostMapping(value = "/searchPatient", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchPatient(@Validated @RequestBody RequestSearchPatient request, @RequestHeader("username") String username) {		
		return service.searchPatient(request);
	}
	
	@GetMapping(value = "/searchPatientById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchPatientById(
			@NotNull(message = "El campo id es requerido")
			@Pattern(regexp = "^(\\d*)$", message= "El campo id no cumple con los estandares requeridos, es solo numeros")
			@PathVariable("id") String id, @RequestHeader("username") String username) {
		return service.searchPatientById(id);
	}
	
	@PostMapping(value = "/registerPatient", produces = "application/json;charset=UTF-8")
	public GenericResponseBean registerPatient(@Validated @RequestBody RequestRegisterPatient request, @RequestHeader("username") String username) {		
		return service.registerPatient(request);
	}
	
	@PostMapping(value = "/updatePatient", produces = "application/json;charset=UTF-8")
	public GenericResponseBean updatePatient(@Validated @RequestBody RequestUpdatePatient request, @RequestHeader("username") String username) {		
		return service.updatePatient(request);
	}
	
	@GetMapping(value = "/deletePatientById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean deletePatientById(
			@NotNull(message = "El campo id es requerido")
			@Pattern(regexp = "^(\\d*)$", message= "El campo id no cumple con los estandares requeridos, es solo numeros")
			@PathVariable("id") String id, @RequestHeader("username") String username) {
		return service.deletePatient(id);
	}
	
	@GetMapping(value = "/historyPatientById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean historyPatientById(
			@NotNull(message = "El campo id es requerido")
			@Pattern(regexp = "^(\\d*)$", message= "El campo id no cumple con los estandares requeridos, es solo numeros")
			@PathVariable("id") String id, @RequestHeader("username") String username) {
		return service.historyPatientById(id);
	}
}
