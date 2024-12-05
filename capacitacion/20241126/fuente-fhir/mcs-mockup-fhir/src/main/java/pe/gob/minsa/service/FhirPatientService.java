package pe.gob.minsa.service;

import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.bean.RequestRegisterPatient;
import pe.gob.minsa.bean.RequestSearchPatient;
import pe.gob.minsa.bean.RequestUpdatePatient;

public interface FhirPatientService {

	GenericResponseBean searchPatientById(String id);
	GenericResponseBean searchPatient(RequestSearchPatient request);
	GenericResponseBean registerPatient(RequestRegisterPatient request);
	GenericResponseBean updatePatient(RequestUpdatePatient request);
	GenericResponseBean deletePatient(String id);
	GenericResponseBean historyPatientById(String id);
}
