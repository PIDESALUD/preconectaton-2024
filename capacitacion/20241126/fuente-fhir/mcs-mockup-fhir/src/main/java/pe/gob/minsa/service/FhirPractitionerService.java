package pe.gob.minsa.service;

import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.bean.RequestRegisterProfesional;
import pe.gob.minsa.bean.RequestSearchProfesional;
import pe.gob.minsa.bean.RequestUpdateProfesional;

public interface FhirPractitionerService {

	GenericResponseBean registerProfesional(RequestRegisterProfesional request);
	GenericResponseBean updateProfesional(RequestUpdateProfesional request);
	GenericResponseBean deleteProfesional(String id);
	GenericResponseBean historyProfesionalById(String id);
	
	GenericResponseBean searchProfesionalById(String id);
	GenericResponseBean searchProfesional(RequestSearchProfesional request);
}
