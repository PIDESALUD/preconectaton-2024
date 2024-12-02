package pe.gob.minsa.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GenericResponseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codigo;
	private Object data;

	public static GenericResponseBean error(String msg) {
		return new GenericResponseBean("6000", msg);
	}

}
