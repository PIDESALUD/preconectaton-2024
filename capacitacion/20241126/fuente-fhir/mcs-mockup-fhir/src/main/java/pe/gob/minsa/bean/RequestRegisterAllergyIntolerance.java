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
public class RequestRegisterAllergyIntolerance implements Serializable{

	private static final long serialVersionUID = 1L;
	@NotNull(message = "El campo tipoAlergia es requerido")
	@NotEmpty(message = "El campo tipoAlergia es requerido")
	@NotBlank(message = "El campo tipoAlergia es requerido")
	@Pattern(regexp = "^(A|I|N)$", message= "El campo tipoAlergia no cumple con los estandares requeridos, es solo [A|I|N]")
	private String tipoAlergia;
	private List<
	@NotEmpty(message = "El campo nombresPaciente es requerido")
	@NotBlank(message = "El campo categoriaAlergia es requerido") 
	@Pattern(regexp = "^(C|M|B|E|N)$", message= "El campo categoriaAlergia no cumple con los estandares requeridos, es solo [C|M|B|E|N]")
	String> categoriaAlergia;
	@NotNull(message = "El campo gradoCriticidad es requerido")
	@NotEmpty(message = "El campo gradoCriticidad es requerido")
	@NotBlank(message = "El campo gradoCriticidad es requerido")
	@Pattern(regexp = "^(B|A|I)$", message= "El campo gradoCriticidad no cumple con los estandares requeridos, es solo [B|A|I]")
	private String gradoCriticidad;
	@NotNull(message = "El campo codigoAlergia es requerido")
	@NotEmpty(message = "El campo codigoAlergia es requerido")
	@NotBlank(message = "El campo codigoAlergia es requerido")
	private String codigoAlergia;
	@NotNull(message = "El campo descrAlergia es requerido")
	@NotEmpty(message = "El campo descrAlergia es requerido")
	@NotBlank(message = "El campo descrAlergia es requerido")
	private String descrAlergia;
	@NotNull(message = "El campo confirmaAlergia es requerido")
	@NotEmpty(message = "El campo confirmaAlergia es requerido")
	@NotBlank(message = "El campo confirmaAlergia es requerido")
	private String confirmaAlergia;
	@NotNull(message = "El campo idPaciente es requerido")
	@NotEmpty(message = "El campo idPaciente es requerido")
	@NotBlank(message = "El campo idPaciente es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo idPaciente no cumple con los estandares requeridos, es solo numeros")
	private String idPaciente;	
	
}
