package pe.gob.minsa.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.gob.minsa.bean.Auditoria;
import pe.gob.minsa.bean.GenericResponseBean;
import pe.gob.minsa.kafka.DatosAuditoriaKafka;
import pe.gob.minsa.kafka.DatosKafka;
import pe.gob.minsa.kafka.TramaRequestAuditoriaKafka;
import pe.gob.minsa.service.KafkaProducerService;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DatosKafka datosKafka;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void createAuditoriaDatos(String jsonRequestStr, String getMethod, String getQuery,
            String jsonResponseStr, String username, String ipclient,
            String clientURL, String clientURI, String response_time){

        Auditoria auditoria = new Auditoria();
        GenericResponseBean responseData = new GenericResponseBean();        
        ObjectMapper om = new ObjectMapper();        
        JSONObject jsonObjectRequest = new JSONObject();

        String datosRequest = new String();

        if (getMethod.equals("POST")) {
            try {
                if (jsonRequestStr != null && !jsonRequestStr.equals("") && jsonRequestStr.length() > 0)                
                    jsonObjectRequest = new JSONObject(jsonRequestStr);
                
                datosRequest = jsonObjectRequest.toString();
            } catch (JSONException e) {            
            	e.printStackTrace();
                logger.error("Error en createAuditoriaDatos 1 ==> "+ e.getMessage());
            }
        } else if (getMethod.equals("GET")) {
            datosRequest = getQuery;
        } // add PUT/HEAD/DELETE/PATCH/etc

        try {
        	LocalDateTime dateTime = LocalDateTime.now();        	
        	responseData = om.readValue(jsonResponseStr, GenericResponseBean.class);
        	
            auditoria.setFecha_request(dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            auditoria.setApp("FHIR HL7 - PERU");
            auditoria.setNombre_servicio("mcs-fhir-resource");
            auditoria.setUsuario_service(username);
            auditoria.setUrl_consulta(clientURL);
            auditoria.setCod_rpta_servicio(responseData.getCodigo());
            auditoria.setMsg_servicio(null);
            auditoria.setIp_consumo(ipclient);
            auditoria.setTrama_request(datosRequest);
            auditoria.setResponse_time(response_time);
            auditoria.setHttp_method(getMethod);

            guardarAuditKafka(auditoria);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Error en createAuditoriaDatos 2 ==> "+ e.getMessage());
        }

    }

    @Override
    public void guardarAuditKafka(Auditoria auditoria) {
        TramaRequestAuditoriaKafka tramaRequestKafka = new TramaRequestAuditoriaKafka();
        DatosAuditoriaKafka datosAuditoriaKafka = new DatosAuditoriaKafka();
        datosAuditoriaKafka.setCodigo("0000");
        datosAuditoriaKafka.setAuditoria(auditoria);       
        tramaRequestKafka.setTopic(datosKafka.getTopicAudit());
        tramaRequestKafka.setMessage(datosAuditoriaKafka);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); 
        HttpEntity<TramaRequestAuditoriaKafka> entityRequestKafka = new HttpEntity<TramaRequestAuditoriaKafka>(tramaRequestKafka,
                headers);
        restTemplate.postForEntity(datosKafka.getUrl(), entityRequestKafka, String.class);


    }

}
