package pe.gob.minsa.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@PropertySource(value = { "file:/opt/data/mcs-minsa/properties/mcs-fhir-resource.properties" })
public class DatosKafka {

	@Value("${mcs.url-kafka}")
	private String url;

	@Value("${mcs.topic-kafka-audit}")
	private String topicAudit;

}
