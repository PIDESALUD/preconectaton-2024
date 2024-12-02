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
import pe.gob.minsa.bean.RequestRegisterCondition;
import pe.gob.minsa.bean.RequestSearchCondition;
import pe.gob.minsa.bean.RequestUpdateCondition;
import pe.gob.minsa.service.FhirConditionService;

@RestController
@RequestMapping(value = "/servicio/v1.0")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class FhirConditionController {

	@Autowired
	private FhirConditionService service;

	@PostMapping(value = "/registerCondition", produces = "application/json;charset=UTF-8")
	public GenericResponseBean registerCondition(
			@Validated @RequestBody RequestRegisterCondition request,
			@RequestHeader("username") String username) {
		return service.registerCondition(request);
	}
	
	@PostMapping(value = "/updateCondition", produces = "application/json;charset=UTF-8")
	public GenericResponseBean updateCondition(@Validated @RequestBody RequestUpdateCondition request,
			@RequestHeader("username") String username) {
		return service.updateCondition(request);
	}

	@PostMapping(value = "/searchCondition", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchPatient(@Validated @RequestBody RequestSearchCondition request,
			@RequestHeader("username") String username) {
		return service.searchCondition(request);
	}

	@GetMapping(value = "/searchConditionById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchAllergyIntoleranceById(
			@NotNull(message = "El campo id es requerido") @Pattern(regexp = "^(\\d*)$", message = "El campo id no cumple con los estandares requeridos, es solo numeros") @PathVariable("id") String id,
			@RequestHeader("username") String username) {
		return service.searchConditionById(id);
	}

	@GetMapping(value = "/deleteConditionById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean deleteConditionById(
			@NotNull(message = "El campo id es requerido") @Pattern(regexp = "^(\\d*)$", message = "El campo id no cumple con los estandares requeridos, es solo numeros") @PathVariable("id") String id,
			@RequestHeader("username") String username) {
		return service.deleteCondition(id);
	}

	@GetMapping(value = "/historyConditionById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean historyConditionById(
			@NotNull(message = "El campo id es requerido") @Pattern(regexp = "^(\\d*)$", message = "El campo id no cumple con los estandares requeridos, es solo numeros") @PathVariable("id") String id,
			@RequestHeader("username") String username) {
		return service.historyConditionById(id);
	}

}
