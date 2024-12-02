package pe.gob.minsa.kafka;

import pe.gob.minsa.bean.Auditoria;

public class DatosAuditoriaKafka {

	private String codigo;
	private Auditoria auditoria;

	public DatosAuditoriaKafka() {

	}

	public DatosAuditoriaKafka(String codigo, Auditoria auditoria) {

		this.codigo = codigo;
		this.auditoria = auditoria;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Auditoria getAuditoria() {
		return auditoria;
	}

	public void setAuditoria(Auditoria auditoria) {
		this.auditoria = auditoria;
	}

	@Override
	public String toString() {
		return "DatosAuditoriaKafka [codigo=" + codigo + ", auditoria=" + auditoria + "]";
	}

}
