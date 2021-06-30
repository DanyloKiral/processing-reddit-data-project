package generator_service

import java.util
import java.util.Properties

import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import common.Configs
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.streams.StreamsConfig

object GeneratorMain extends App {
  implicit val mapper = new ObjectMapper().registerModule(DefaultScalaModule)
  mapper.registerModule(new JavaTimeModule());
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  private val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "generator-service")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Configs.KafkaBrokerAddress)

  createTopic

  val dataGenerator = new DataGenerator(Configs.InputFileName)
  val producer = new Producer(Configs.KafkaBrokerAddress)

  try {
    dataGenerator.processLines(data => producer.sendMessageToTopic(data.id, data, Configs.CommentsTopicName))
  } catch {
    case error: Throwable => println(error)
  } finally {
    producer.close
  }

  private def createTopic(): Unit = {
    val client = AdminClient.create(props)
    val topics = new util.ArrayList[NewTopic]()
    topics.add(new NewTopic(
      Configs.CommentsTopicName,
      Configs.CommentsTopicPartitions,
      Configs.CommentsTopicReplication))
    client.createTopics(topics)
    client.close()
  }
}
