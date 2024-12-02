package pe.gob.minsa.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auditoria implements Serializable {

    private static final long serialVersionUID = 1L;

    // Cliente
    private String trama_request;

    // Sso
    private String usuario_service;
    private String ip_consumo;

    // Servicio
    private String app;
    private String nombre_servicio;
    private String fecha_request;
    private String url_consulta;
    private String cod_rpta_servicio;
    private String msg_servicio;
    private String response_time;
    private String http_method;

   

}
