package pe.gob.minsa.service;

import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.bean.RequestRegisterMedicationStatement;
import pe.gob.minsa.bean.RequestSearchMedicationStatement;
import pe.gob.minsa.bean.RequestUpdateMedicationStatement;

public interface FhirMedicationStatementService {

	
	GenericResponseBean registerMedicationStatement(RequestRegisterMedicationStatement request);
	GenericResponseBean updateMedicationStatement(RequestUpdateMedicationStatement request);
	GenericResponseBean deleteMedicationStatement(String id);
	GenericResponseBean historyMedicationStatementById(String id);
	
	GenericResponseBean searchMedicationStatementById(String id);
	GenericResponseBean searchMedicationStatement(RequestSearchMedicationStatement request);
}
