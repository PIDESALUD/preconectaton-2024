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
public class RequestUpdateProfesional implements Serializable {

	private static final long serialVersionUID = 1L;	
		
	@NotNull(message = "El campo idProfesional es requerido")
	@NotEmpty(message = "El campo idProfesional es requerido")
	@NotBlank(message = "El campo idProfesional es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo idProfesional no cumple con los estandares requeridos, es solo numeros")
	private String idProfesional;	
	private String activo;	
//	@NotEmpty(message = "El campo nombresProfesional es requerido")
	private List<@NotBlank(message = "El campo nombresPaciente es requerido") String> nombresProfesional;
//	@NotNull(message = "El campo apellidosProfesional es requerido")
//	@NotEmpty(message = "El campo apellidosProfesional es requerido")
//	@NotBlank(message = "El campo apellidosProfesional es requerido")
	private String apellidosProfesional;
//	@NotNull(message = "El campo tipoDocumentoProfesional es requerido")
//	@NotEmpty(message = "El campo tipoDocumentoProfesional es requerido")
//	@NotBlank(message = "El campo tipoDocumentoProfesional es requerido")
//	@Pattern(regexp = "^([1]|[2]|[3]|[4]){1}$", message= "El campo tipoDocumentoProfesional no cumple con los estandares requeridos, es solo numeros [1-4]")
//	@Length(min = 1, max = 1, message = "El campo tipoDocumentoProfesional tiene una longitud maxima es de 1 caracter")
	private String tipoDocumentoProfesional;
//	@NotNull(message = "El campo dniProfesional es requerido")
//	@NotEmpty(message = "El campo dniProfesional es requerido")
//	@NotBlank(message = "El campo dniProfesional es requerido")
//	@Pattern(regexp = "^(\\d*)$", message= "El campo dniProfesional no cumple con los estandares requeridos, es solo numeros")
//	@Length(min = 1, max = 25, message = "El campo dniProfesional tiene una longitud maxima es de 25 caracteres")
	private String dniProfesional;
//	@NotNull(message = "El campo digitoVerificacionProfesional es requerido")
//	@NotEmpty(message = "El campo digitoVerificacionProfesional es requerido")
//	@NotBlank(message = "El campo digitoVerificacionProfesional es requerido")
//	@Pattern(regexp = "^(\\d*)$", message= "El campo digitoVerificacionProfesional no cumple con los estandares requeridos, es solo numeros")
//	@Length(min = 1, max = 1, message = "El campo digitoVerificacionProfesional tiene una longitud maxima es de 1 caracteres")
	private String digitoVerificacionProfesional;		
//	@NotEmpty(message = "El campo nombresContactoFamiliar es requerido")
	private List<@NotBlank(message = "El campo nombresContactoFamiliar es requerido") String> nombresContactoFamiliar;
//	@NotNull(message = "El campo apellidosContactoFamiliar es requerido")
//	@NotEmpty(message = "El campo apellidosContactoFamiliar es requerido")
//	@NotBlank(message = "El campo apellidosContactoFamiliar es requerido")
	private String apellidosContactoFamiliar;		
//	@NotNull(message = "El campo nroColegiatura es requerido")
//	@NotEmpty(message = "El campo nroColegiatura es requerido")
//	@NotBlank(message = "El campo nroColegiatura es requerido")
//	@Pattern(regexp = "^(\\d*)$", message= "El campo nroColegiatura no cumple con los estandares requeridos, es solo numeros")
	private String nroColegiatura;
//	@NotNull(message = "El campo codigoColegio es requerido")
//	@NotEmpty(message = "El campo codigoColegio es requerido")
//	@NotBlank(message = "El campo codigoColegio es requerido")	
	private String codigoColegio;
//	@NotNull(message = "El campo nombreColegio es requerido")
//	@NotEmpty(message = "El campo nombreColegio es requerido")
//	@NotBlank(message = "El campo nombreColegio es requerido")	
	private String nombreColegio;
	@NotNull(message = "El campo nombrePais es requerido")
	@NotEmpty(message = "El campo nombrePais es requerido")
	@NotBlank(message = "El campo nombrePais es requerido")
	private String nombrePais;

}
