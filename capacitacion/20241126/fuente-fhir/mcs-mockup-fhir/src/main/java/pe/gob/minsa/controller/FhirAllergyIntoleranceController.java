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
import pe.gob.minsa.bean.RequestRegisterAllergyIntolerance;
import pe.gob.minsa.bean.RequestSearchAllergyIntolerance;
import pe.gob.minsa.bean.RequestUpdateAllergyIntolerance;
import pe.gob.minsa.service.FhirAllergyIntoleranceService;

@RestController
@RequestMapping(value = "/servicio/v1.0")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class FhirAllergyIntoleranceController {

	@Autowired
	private FhirAllergyIntoleranceService service;

	@PostMapping(value = "/registerAllergyIntolerance", produces = "application/json;charset=UTF-8")
	public GenericResponseBean registerAllergyIntolerance(
			@Validated @RequestBody RequestRegisterAllergyIntolerance request,
			@RequestHeader("username") String username) {
		return service.registerAllergyIntolerance(request);
	}

	@PostMapping(value = "/updateAllergyIntolerance", produces = "application/json;charset=UTF-8")
	public GenericResponseBean updateAllergyIntolerance(@Validated @RequestBody RequestUpdateAllergyIntolerance request,
			@RequestHeader("username") String username) {
		return service.updateAllergyIntolerance(request);
	}

	@PostMapping(value = "/searchAllergyIntolerance", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchPatient(@Validated @RequestBody RequestSearchAllergyIntolerance request,
			@RequestHeader("username") String username) {
		return service.searchAllergyIntolerance(request);
	}

	@GetMapping(value = "/searchAllergyIntoleranceById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean searchAllergyIntoleranceById(
			@NotNull(message = "El campo id es requerido") @Pattern(regexp = "^(\\d*)$", message = "El campo id no cumple con los estandares requeridos, es solo numeros") @PathVariable("id") String id,
			@RequestHeader("username") String username) {
		return service.searchAllergyIntoleranceById(id);
	}

	@GetMapping(value = "/deleteAllergyIntoleranceById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean deleteAllergyIntoleranceById(
			@NotNull(message = "El campo id es requerido") @Pattern(regexp = "^(\\d*)$", message = "El campo id no cumple con los estandares requeridos, es solo numeros") @PathVariable("id") String id,
			@RequestHeader("username") String username) {
		return service.deleteAllergyIntolerance(id);
	}

	@GetMapping(value = "/historyAllergyIntoleranceById/{id}", produces = "application/json;charset=UTF-8")
	public GenericResponseBean historyAllergyIntoleranceById(
			@NotNull(message = "El campo id es requerido") @Pattern(regexp = "^(\\d*)$", message = "El campo id no cumple con los estandares requeridos, es solo numeros") @PathVariable("id") String id,
			@RequestHeader("username") String username) {
		return service.historyAllergyIntoleranceById(id);
	}
}
