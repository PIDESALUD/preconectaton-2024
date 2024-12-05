package pe.gob.minsa.service;

import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.bean.RequestRegisterOrganization;
import pe.gob.minsa.bean.RequestSearchOrganization;
import pe.gob.minsa.bean.RequestUpdateOrganization;

public interface FhirOrganizationService {

	
	GenericResponseBean registerOrganization(RequestRegisterOrganization request);
	GenericResponseBean updateOrganization(RequestUpdateOrganization request);
	GenericResponseBean deleteOrganization(String id);
	GenericResponseBean historyOrganizationById(String id);
	
	GenericResponseBean searchOrganizationById(String id);
	GenericResponseBean searchOrganization(RequestSearchOrganization request);
}
