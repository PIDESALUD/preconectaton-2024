package pe.gob.minsa.service;

import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.bean.RequestRegisterCondition;
import pe.gob.minsa.bean.RequestSearchCondition;
import pe.gob.minsa.bean.RequestUpdateCondition;

public interface FhirConditionService {

	
	GenericResponseBean registerCondition(RequestRegisterCondition request);
	GenericResponseBean updateCondition(RequestUpdateCondition request);
	GenericResponseBean deleteCondition(String id);
	GenericResponseBean historyConditionById(String id);
	
	GenericResponseBean searchConditionById(String id);
	GenericResponseBean searchCondition(RequestSearchCondition request);
}
