package language_service

import java.time.Duration
import java.util
import java.util.Properties

import common.{Configs, RedditComment}
import keyword_service.KeywordMain.props
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.json4s._
import org.json4s.jackson.JsonMethods._

object LanguageMain extends App {
  implicit val formats = org.json4s.DefaultFormats
  private val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "language-service")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Configs.KafkaBrokerAddress)

  createTopic

  val languageDetector = new LanguageDetector()
  languageDetector.init(Configs.LanguageDetectorProfilesPath)

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
      .filter((k, t) => t.size > 0)
      .mapValues(t => languageDetector.detect(t))
      .filter((k, l) => l.nonEmpty)
      .mapValues(_.get)
      .to(Configs.LanguagesTopicName)

    builder.build()
  }

  private def createTopic(): Unit = {
    val client = AdminClient.create(props)
    val topics = new util.ArrayList[NewTopic]()
    topics.add(new NewTopic(
      Configs.LanguagesTopicName,
      Configs.LanguagesTopicPartitions,
      Configs.LanguagesTopicReplication))
    client.createTopics(topics)
    client.close()
  }
}
