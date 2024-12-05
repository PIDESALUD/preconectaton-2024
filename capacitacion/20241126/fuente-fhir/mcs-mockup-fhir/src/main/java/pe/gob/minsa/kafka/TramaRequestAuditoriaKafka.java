package pe.gob.minsa.kafka;

public class TramaRequestAuditoriaKafka {

	private String topic;
	private DatosAuditoriaKafka message;

	public TramaRequestAuditoriaKafka() {

	}

	public TramaRequestAuditoriaKafka(String topic, DatosAuditoriaKafka message) {

		this.topic = topic;
		this.message = message;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public DatosAuditoriaKafka getMessage() {
		return message;
	}

	public void setMessage(DatosAuditoriaKafka message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "TramaRequestAuditoriaKafka [topic=" + topic + ", message=" + message + "]";
	}

}
