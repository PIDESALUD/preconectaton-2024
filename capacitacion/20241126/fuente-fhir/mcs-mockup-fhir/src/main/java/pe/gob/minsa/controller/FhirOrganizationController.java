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
import pe.gob.minsa.bean.RequestRegisterOrganization;
import pe.gob.minsa.bean.RequestSearchOrganization;
import pe.gob.minsa.bean.RequestUpdateOrganization;
import pe.gob.minsa.service.FhirOrganizationService;

@RestController
@RequestMapping(value = "/servicio/v1.0")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class FhirOrganizationController {

	@Autowired
	private FhirOrganizationService service;
	
	@PostMapping(value = "/registerOrganization", produces = "application/json;charset=UTF-8")
	public GenericResponseBean registerOrganization(@Validated @RequestBody RequestRegisterOrganization request, @RequestHeader("username") String username) {		
		return service.registerOrganization(request);
	}
	
	@PostMapping(value = "/updateOrganization", produces = "application/json;charset=UTF-8")
	public GenericResponseBean updateOrganization(@Validated @RequestBody RequestUpdateOrganization request, @RequestHeader("username") String username) {		
		return service.updateOrganization(request);
	}
	
	
	
	@PostMapping(value = "/searchOrganization", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchOrganization(@Validated @RequestBody RequestSearchOrganization request, @RequestHeader("username") String username) {		
		return service.searchOrganization(request);
	}
	
	@GetMapping(value = "/searchOrganizationById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchOrganizationById(
			@NotNull(message = "El campo id es requerido")
			@Pattern(regexp = "^(\\d*)$", message= "El campo id no cumple con los estandares requeridos, es solo numeros")
			@PathVariable("id") String id, @RequestHeader("username") String username) {
		return service.searchOrganizationById(id);
	}
		
	
	@GetMapping(value = "/deleteOrganizationById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean deleteOrganizationById(
			@NotNull(message = "El campo id es requerido")
			@Pattern(regexp = "^(\\d*)$", message= "El campo id no cumple con los estandares requeridos, es solo numeros")
			@PathVariable("id") String id, @RequestHeader("username") String username) {
		return service.deleteOrganization(id);
	}
	
	@GetMapping(value = "/historyOrganizationById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean historyOrganizationById(
			@NotNull(message = "El campo id es requerido")
			@Pattern(regexp = "^(\\d*)$", message= "El campo id no cumple con los estandares requeridos, es solo numeros")
			@PathVariable("id") String id, @RequestHeader("username") String username) {
		return service.historyOrganizationById(id);
	}
}
