package pe.gob.minsa.bean;

import java.io.Serializable;

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
public class RequestRegisterMedicationStatement implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "El campo idPaciente es requerido")
	@NotEmpty(message = "El campo idPaciente es requerido")
	@NotBlank(message = "El campo idPaciente es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo idPaciente no cumple con los estandares requeridos, es solo numeros")
	private String idPaciente;	
	@NotNull(message = "El campo codigoProdFarmaceutico es requerido")
	@NotEmpty(message = "El campo codigoProdFarmaceutico es requerido")
	@NotBlank(message = "El campo codigoProdFarmaceutico es requerido")	
	private String codigoProdFarmaceutico;
	@NotNull(message = "El campo descrProdFarmaceutico es requerido")
	@NotEmpty(message = "El campo descrProdFarmaceutico es requerido")
	@NotBlank(message = "El campo descrProdFarmaceutico es requerido")	
	private String descrProdFarmaceutico;
	@NotNull(message = "El campo fechaPrescripcion es requerido")
	@NotEmpty(message = "El campo fechaPrescripcion es requerido")
	@NotBlank(message = "El campo fechaPrescripcion es requerido")
	@Pattern(regexp = "^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(?:(?:(-)(?:0?[13578]|1[02])\\1(?:31))|(?:(-)(?:0?[13-9]|1[0-2])\\2(?:29|30)))$|^(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(-)0?2\\3(?:29)$|^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(-)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:0[1-9]|1\\d|2[0-8])$", message= "El campo fechaPrescripcion  no cumple con los estandares requeridos YYYY-MM-DD o la fecha es invalida")
	private String fechaPrescripcion;
	@NotNull(message = "El campo descrDosis es requerido")
	@NotEmpty(message = "El campo descrDosis es requerido")
	@NotBlank(message = "El campo descrDosis es requerido")	
	private String descrDosis;
	@NotNull(message = "El campo via es requerido")
	@NotEmpty(message = "El campo via es requerido")
	@NotBlank(message = "El campo via es requerido")	
	private String via;
	@NotNull(message = "El campo cantidad es requerido")
	@NotEmpty(message = "El campo cantidad es requerido")
	@NotBlank(message = "El campo cantidad es requerido")	
	@Pattern(regexp = "^(\\d*)$", message= "El campo cantidad no cumple con los estandares requeridos, es solo numeros")
	private String cantidad;

}
