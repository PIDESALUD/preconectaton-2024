package pe.gob.minsa.service;

import pe.gob.minsa.bean.Auditoria;

public interface KafkaProducerService {

    void createAuditoriaDatos(String objetoRequest, String getMethod, String getQuery, String codigorespuesta,
            String username, String ipclient,
            String clientURL, String clientURI, String response_time);

    void guardarAuditKafka(Auditoria auditoria);

}
