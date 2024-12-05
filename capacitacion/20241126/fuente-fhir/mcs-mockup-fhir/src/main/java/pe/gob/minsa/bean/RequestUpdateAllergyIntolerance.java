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
public class RequestUpdateAllergyIntolerance implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Pattern(regexp = "^(A|I|N)$", message= "El campo tipoAlergia no cumple con los estandares requeridos, es solo [A|I|N]")
	private String tipoAlergia;
	private List<
	@Pattern(regexp = "^(C|M|B|E|N)$", message= "El campo categoriaAlergia no cumple con los estandares requeridos, es solo [C|M|B|E|N]")
	String> categoriaAlergia;
	@Pattern(regexp = "^(B|A|I)$", message= "El campo gradoCriticidad no cumple con los estandares requeridos, es solo [B|A|I]")
	private String gradoCriticidad;	
	private String codigoAlergia;	
	private String descrAlergia;	
	private String confirmaAlergia;
	@NotNull(message = "El campo idAllergy es requerido")
	@NotEmpty(message = "El campo idAllergy es requerido")
	@NotBlank(message = "El campo idAllergy es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo idAllergy no cumple con los estandares requeridos, es solo numeros")
	private String idAllergy;	
	@Pattern(regexp = "^(true|false)$", message= "El campo activo no cumple con los estandares requeridos, es solo [true|false]")
	private String activo;	
	@NotNull(message = "El campo idPaciente es requerido")
	@NotEmpty(message = "El campo idPaciente es requerido")
	@NotBlank(message = "El campo idPaciente es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo idPaciente no cumple con los estandares requeridos, es solo numeros")
	private String idPaciente;
}
