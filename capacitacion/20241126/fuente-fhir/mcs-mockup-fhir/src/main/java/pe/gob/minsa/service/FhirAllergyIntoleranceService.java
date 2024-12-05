package pe.gob.minsa.service;

import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.bean.RequestRegisterAllergyIntolerance;
import pe.gob.minsa.bean.RequestSearchAllergyIntolerance;
import pe.gob.minsa.bean.RequestUpdateAllergyIntolerance;

public interface FhirAllergyIntoleranceService {

	
	GenericResponseBean registerAllergyIntolerance(RequestRegisterAllergyIntolerance request);
	GenericResponseBean updateAllergyIntolerance(RequestUpdateAllergyIntolerance request);
	GenericResponseBean deleteAllergyIntolerance(String id);
	GenericResponseBean historyAllergyIntoleranceById(String id);
	
	GenericResponseBean searchAllergyIntoleranceById(String id);
	GenericResponseBean searchAllergyIntolerance(RequestSearchAllergyIntolerance request);
}
