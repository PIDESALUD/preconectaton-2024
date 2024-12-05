package pe.gob.minsa.bean;

import java.io.Serializable;

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
public class RequestUpdateOrganization implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "El campo idOrganizacion es requerido")
	@NotEmpty(message = "El campo idOrganizacion es requerido")
	@NotBlank(message = "El campo idOrganizacion es requerido")
	@Pattern(regexp = "^(\\d*)$", message= "El campo idOrganizacion no cumple con los estandares requeridos, es solo numeros")
	private String idOrganizacion;	
	//@Pattern(regexp = "^(true|false)$", message= "El campo activo no cumple con los estandares requeridos, es solo [true|false]")
	private String activo;	
	@NotNull(message = "El campo codigoRenipress es requerido")
	@NotEmpty(message = "El campo codigoRenipress es requerido")
	@NotBlank(message = "El campo codigoRenipress es requerido")
	@Length(min = 8, max = 8, message = "El campo codigoRenipress debe tener una longitud maxima es de 8 digitos")
	private String codigoRenipress;
	@NotNull(message = "El campo nombreEstablecimiento es requerido")
	@NotEmpty(message = "El campo nombreEstablecimiento es requerido")
	@NotBlank(message = "El campo nombreEstablecimiento es requerido")
	private String nombreEstablecimiento;
	@NotNull(message = "El campo ubigeo es requerido")
	@NotEmpty(message = "El campo ubigeo es requerido")
	@NotBlank(message = "El campo ubigeo es requerido")
	@Pattern(regexp = "^(\\d*)$", message = "El campo ubigeo no cumple con los estandares requeridos, es solo numeros")
	@Length(min = 1, max = 6, message = "El campo ubigeo tiene una longitud maxima es de 6 digitos")
	private String ubigeo;
	@NotNull(message = "El campo direccion es requerido")
	@NotEmpty(message = "El campo direccion es requerido")
	@NotBlank(message = "El campo direccion es requerido")
	private String direccion;
	@NotNull(message = "El campo descrDisa es requerido")
	@NotEmpty(message = "El campo descrDisa es requerido")
	@NotBlank(message = "El campo descrDisa es requerido")
	private String descrDisa;

}
