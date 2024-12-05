package pe.gob.minsa.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestSearchMedicationStatement implements Serializable {

	private static final long serialVersionUID = 1L;
	private String codeName;
	private int pageNumber;
	private int limit;

}
