package keyword_service

import java.time.Duration
import java.util
import java.util.Properties

import common.{Configs, RedditComment}
import generator_service.GeneratorMain.props
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.json4s._
import org.json4s.jackson.JsonMethods._

object KeywordMain extends App {
  implicit val formats = org.json4s.DefaultFormats
  private val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "keywords-service")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Configs.KafkaBrokerAddress)

  createTopic

  val topology = buildTopology()
  val streams: KafkaStreams = new KafkaStreams(topology, props)

  streams.cleanUp()
  streams.start()
  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

  private def buildTopology() = {
    val builder = new StreamsBuilder()
    val comments: KStream[String, String] = builder.stream[String, String](Configs.CommentsTopicName)

    comments
        .mapValues(parse(_).extract[RedditComment])
        .mapValues(c => c.comment.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase)
        .flatMapValues(_.split(' '))
        .filter((k, t) => t.size > 3)
        .to(Configs.KeywordsTopicName)

    builder.build()
  }

  private def createTopic(): Unit = {
    val client = AdminClient.create(props)
    val topics = new util.ArrayList[NewTopic]()
    topics.add(new NewTopic(
      Configs.KeywordsTopicName,
      Configs.KeywordsTopicPartitions,
      Configs.KeywordsTopicReplication))
    client.createTopics(topics)
    client.close()
  }
}
