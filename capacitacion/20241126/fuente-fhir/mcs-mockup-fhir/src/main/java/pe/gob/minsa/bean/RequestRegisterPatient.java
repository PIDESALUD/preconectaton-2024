package pe.gob.minsa.bean;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestRegisterPatient implements Serializable {

	private static final long serialVersionUID = 1L;	
	@NotEmpty(message = "El campo nombresPaciente es requerido")
	private List<@NotBlank(message = "El campo nombresPaciente es requerido") String> nombresPaciente;
	@NotNull(message = "El campo apellidosPaciente es requerido")
	@NotEmpty(message = "El campo apellidosPaciente es requerido")
	@NotBlank(message = "El campo apellidosPaciente es requerido")
	private String apellidosPaciente;
	@NotNull(message = "El campo tipoDocumentoPaciente es requerido")
	@NotEmpty(message = "El campo tipoDocumentoPaciente es requerido")
	@NotBlank(message = "El campo tipoDocumentoPaciente es requerido")
	@Pattern(regexp = "^([1]|[2]|[3]|[4]){1}$", message= "El campo tipoDocumentoPaciente no cumple con los estandares requeridos, es solo numeros [1-4]")
	@Length(min = 1, max = 1, message = "El campo tipoDocumentoPaciente tiene una longitud maxima es de 1 caracter")
	private String tipoDocumentoPaciente;
	@NotNull(message = "El campo dniPaciente es requerido")
	@NotEmpty(message = "El campo dniPaciente es requerido")
	@NotBlank(message = "El campo dniPaciente es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo dniPaciente no cumple con los estandares requeridos, es solo numeros")
	@Length(min = 1, max = 25, message = "El campo dniPaciente tiene una longitud maxima es de 25 caracteres")
	private String dniPaciente;
	@NotNull(message = "El campo digitoVerificacionPaciente es requerido")
	@NotEmpty(message = "El campo digitoVerificacionPaciente es requerido")
	@NotBlank(message = "El campo digitoVerificacionPaciente es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo digitoVerificacionPaciente no cumple con los estandares requeridos, es solo numeros")
	@Length(min = 1, max = 1, message = "El campo digitoVerificacionPaciente tiene una longitud maxima es de 1 caracteres")
	private String digitoVerificacionPaciente;
	@NotNull(message = "El campo nroCelularPaciente es requerido")
	@NotEmpty(message = "El campo nroCelularPaciente es requerido")
	@NotBlank(message = "El campo nroCelularPaciente es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo nroCelularPaciente no cumple con los estandares requeridos, es solo numeros")
	private String nroCelularPaciente;
	@NotNull(message = "El campo fechaNacimientoPaciente es requerido")
	@NotEmpty(message = "El campo fechaNacimientoPaciente es requerido")
	@NotBlank(message = "El campo fechaNacimientoPaciente es requerido")
	@Pattern(regexp = "^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(?:(?:(-)(?:0?[13578]|1[02])\\1(?:31))|(?:(-)(?:0?[13-9]|1[0-2])\\2(?:29|30)))$|^(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(-)0?2\\3(?:29)$|^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(-)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:0[1-9]|1\\d|2[0-8])$", message= "El campo fechaNacimientoPaciente  no cumple con los estandares requeridos YYYY-MM-DD o la fecha es invalida")	
	private String fechaNacimientoPaciente;
	@NotNull(message = "El campo generoPaciente es requerido")
	@NotEmpty(message = "El campo generoPaciente es requerido")
	@NotBlank(message = "El campo generoPaciente es requerido")
	@Pattern(regexp = "^([M,F]|[m,f]){1}$", message = "El campo generoPaciente debe contener el siguiente valor (M - F)")
	private String generoPaciente;
	@NotNull(message = "El campo direccionPaciente es requerido")
	@NotEmpty(message = "El campo direccionPaciente es requerido")
	@NotBlank(message = "El campo direccionPaciente es requerido")
	private String direccionPaciente;
	@NotNull(message = "El campo distritoPaciente es requerido")
	@NotEmpty(message = "El campo distritoPaciente es requerido")
	@NotBlank(message = "El campo distritoPaciente es requerido")
	private String distritoPaciente;
	@NotNull(message = "El campo provinciaPaciente es requerido")
	@NotEmpty(message = "El campo provinciaPaciente es requerido")
	@NotBlank(message = "El campo provinciaPaciente es requerido")
	private String provinciaPaciente;
	@NotNull(message = "El campo departamentoPaciente es requerido")
	@NotEmpty(message = "El campo departamentoPaciente es requerido")
	@NotBlank(message = "El campo departamentoPaciente es requerido")
	private String departamentoPaciente;	
	@NotEmpty(message = "El campo nombresContactoFamiliar es requerido")
	private List<@NotBlank(message = "El campo nombresContactoFamiliar es requerido") String> nombresContactoFamiliar;
	@NotNull(message = "El campo apellidosContactoFamiliar es requerido")
	@NotEmpty(message = "El campo apellidosContactoFamiliar es requerido")
	@NotBlank(message = "El campo apellidosContactoFamiliar es requerido")
	private String apellidosContactoFamiliar;		
	@NotNull(message = "El campo nroContactoFamiliar es requerido")
	@NotEmpty(message = "El campo nroContactoFamiliar es requerido")
	@NotBlank(message = "El campo nroContactoFamiliar es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo nroContactoFamiliar no cumple con los estandares requeridos, es solo numeros")
	private String nroContactoFamiliar;		
	@NotNull(message = "El campo nombrePais es requerido")
	@NotEmpty(message = "El campo nombrePais es requerido")
	@NotBlank(message = "El campo nombrePais es requerido")
	private String nombrePais;
	

}
