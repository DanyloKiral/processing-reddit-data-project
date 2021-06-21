package generator_service

import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import common.Configs

object GeneratorMain extends App {
  implicit val mapper = new ObjectMapper().registerModule(DefaultScalaModule)
  mapper.registerModule(new JavaTimeModule());
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  val dataGenerator = new DataGenerator(Configs.InputFileName)
  val producer = new Producer(Configs.KafkaBrokerAddress)

  try {
    dataGenerator.processLines(data => producer.sendMessageToTopic(data.id, data, Configs.MessagesTopicName))
  } catch {
    case error: Throwable => println(error)
  } finally {
    producer.close
  }
}
