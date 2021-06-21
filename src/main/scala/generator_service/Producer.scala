package generator_service

import java.util.Properties
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer._

class Producer (kafkaBrokerAddress: String) (implicit mapper: ObjectMapper) {
  private val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokerAddress)
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  private val producer = new KafkaProducer[String, String](props)

  def sendMessageToTopic(key: String, message: Any, topic: String): Unit = {
    val serializedMessage = mapper.writeValueAsString(message)
    val record = new ProducerRecord[String, String](topic, key, serializedMessage)
    producer.send(record).get
  }

  def close(): Unit =
    producer.close
}
