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
import pe.gob.minsa.bean.RequestRegisterProfesional;
import pe.gob.minsa.bean.RequestSearchProfesional;
import pe.gob.minsa.bean.RequestUpdateProfesional;
import pe.gob.minsa.service.FhirPractitionerService;

@RestController
@RequestMapping(value = "/servicio/v1.0")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class FhirPractitionerController {

	@Autowired
	private FhirPractitionerService service;
	
	@PostMapping(value = "/searchPractitioner", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchPractitioner(@Validated @RequestBody RequestSearchProfesional request, @RequestHeader("username") String username) {		
		return service.searchProfesional(request);
	}
	
	@GetMapping(value = "/searchPractitionerById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchPractitionerById(
			@NotNull(message = "El campo id es requerido")
			@Pattern(regexp = "^(\\d*)$", message= "El campo id no cumple con los estandares requeridos, es solo numeros")
			@PathVariable("id") String id, @RequestHeader("username") String username) {
		return service.searchProfesionalById(id);
	}
	
	@PostMapping(value = "/registerPractitioner", produces = "application/json;charset=UTF-8")
	public GenericResponseBean registerPatient(@Validated @RequestBody RequestRegisterProfesional request, @RequestHeader("username") String username) {		
		return service.registerProfesional(request);
	}
	
	@PostMapping(value = "/updatePractitioner", produces = "application/json;charset=UTF-8")
	public GenericResponseBean updatePractitioner(@Validated @RequestBody RequestUpdateProfesional request, @RequestHeader("username") String username) {		
		return service.updateProfesional(request);
	}
	
	@GetMapping(value = "/deletePractitionerById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean deletePatientById(
			@NotNull(message = "El campo id es requerido")
			@Pattern(regexp = "^(\\d*)$", message= "El campo id no cumple con los estandares requeridos, es solo numeros")
			@PathVariable("id") String id, @RequestHeader("username") String username) {
		return service.deleteProfesional(id);
	}
	
	@GetMapping(value = "/historyPractitionerById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean historyPractitionerById(
			@NotNull(message = "El campo id es requerido")
			@Pattern(regexp = "^(\\d*)$", message= "El campo id no cumple con los estandares requeridos, es solo numeros")
			@PathVariable("id") String id, @RequestHeader("username") String username) {
		return service.historyProfesionalById(id);
	}
}
