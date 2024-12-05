package pe.gob.minsa.bean;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestUpdatePatient implements Serializable {

	private static final long serialVersionUID = 1L;	
	@NotNull(message = "El campo idPaciente es requerido")
	@NotEmpty(message = "El campo idPaciente es requerido")
	@NotBlank(message = "El campo idPaciente es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo idPaciente no cumple con los estandares requeridos, es solo numeros")
	private String idPaciente;	
	//@Pattern(regexp = "^(true|false)$", message= "El campo activo no cumple con los estandares requeridos, es solo [true|false]")
	private String activo;	
	private List<String> nombresPaciente;	
	private String apellidosPaciente;	
	//@Pattern(regexp = "^([1]|[2]|[3]|[4]){1}$", message= "El campo tipoDocumentoPaciente no cumple con los estandares requeridos, es solo numeros [1-4]")
	//@Length(min = 1, max = 1, message = "El campo tipoDocumentoPaciente tiene una longitud maxima de 1 caracter")
	private String tipoDocumentoPaciente;	
	//@Pattern(regexp = "^(\\d*)$", message= "El campo dniPaciente no cumple con los estandares requeridos, es solo numeros")
	//@Length(min = 1, max = 25, message = "El campo dniPaciente tiene una longitud maxima es de 25 caracteres")
	private String dniPaciente;	
	//@Pattern(regexp = "^(\\d*)$", message= "El campo digitoVerificacionPaciente no cumple con los estandares requeridos, es solo numeros")
	//@Length(min = 1, max = 1, message = "El campo digitoVerificacionPaciente tiene una longitud maxima es de 1 caracteres")
	private String digitoVerificacionPaciente;	
	//@Pattern(regexp = "^(\\d*)$", message= "El campo nroCelularPaciente no cumple con los estandares requeridos, es solo numeros")
	private String nroCelularPaciente;	
	//@Pattern(regexp = "^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(?:(?:(-)(?:0?[13578]|1[02])\\1(?:31))|(?:(-)(?:0?[13-9]|1[0-2])\\2(?:29|30)))$|^(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(-)0?2\\3(?:29)$|^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(-)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:0[1-9]|1\\d|2[0-8])$", message= "El campo fechaNacimientoPaciente  no cumple con los estandares requeridos YYYY-MM-DD o la fecha es invalida")	
	private String fechaNacimientoPaciente;	
	//@Pattern(regexp = "^([M,F]|[m,f]){1}$", message = "El campo generoPaciente debe contener el siguiente valor (M - F)")
	private String generoPaciente;	
	private String direccionPaciente;	
	private String distritoPaciente;	
	private String provinciaPaciente;	
	private String departamentoPaciente;
	private List<String> nombresContactoFamiliar;	
	private String apellidosContactoFamiliar;
	private String nroContactoFamiliar;
	@NotNull(message = "El campo nombrePais es requerido")
	@NotEmpty(message = "El campo nombrePais es requerido")
	@NotBlank(message = "El campo nombrePais es requerido")
	private String nombrePais;
	

}
