package com.nic.billingstats;

import com.nic.billingstats.model.BillingStatsEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

//@Configuration
//@EnableKafkaStreams
public class KafkaStreamConfig {

	//@Value("${spring.cloud.stream.kafka.binder.brokers}")
	//String kafkaBootStrapServers;

	//@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public KafkaStreamsConfiguration kafkaStreamConfig() {
		Map props = new HashMap<String, Object>();

		/*props.put(StreamsConfig.APPLICATION_ID_CONFIG, "billingstats");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootStrapServers);
		//props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		//props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		//props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<>(BillingStatsEvent.class).getClass());
		//props.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
		//props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, BillingStatsEvent.class);
		props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "3000");
		props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
*/
		return new KafkaStreamsConfiguration(props);
	}

}
