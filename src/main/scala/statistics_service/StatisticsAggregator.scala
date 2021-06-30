package statistics_service

import java.time.Duration
import java.util.Properties

import common.{Configs, LanguageMessages, RedditComment, SentimentMessages, Statistics}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable, Materialized}
import org.json4s.jackson.JsonMethods.parse
import sentiment_service.SentimentMain.createTopic
import org.json4s.jackson.JsonMethods.parse
import java.time.Duration
import java.util
import java.util.Properties

import language_service.LanguageMain.props
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.json4s._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.state.QueryableStoreTypes


class StatisticsAggregator {
  implicit val formats = org.json4s.DefaultFormats
  private val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "statistics-service")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Configs.KafkaBrokerAddress)

  private val topology = buildTopology()
  private val streams: KafkaStreams = new KafkaStreams(topology, props)

  streams.cleanUp()
  streams.start()
  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

  private def buildTopology() = {
    val builder = new StreamsBuilder()
    val keywords: KStream[String, String] = builder.stream[String, String](Configs.KeywordsTopicName)
    val languages: KStream[String, String] = builder.stream[String, String](Configs.LanguagesTopicName)
    val sentiments: KStream[String, String] = builder.stream[String, String](Configs.SentimentsTopicName)

    keywords
      .groupBy((key, value) => value)
      .count()(Materialized.as("keywords-count"))

    languages
      .groupBy((key, value) => value)
      .count()(Materialized.as("languages-count"))

    sentiments
      .groupBy((key, value) => value)
      .count()(Materialized.as("sentiments-count"))

    builder.build()
  }

  def collectStatistics() = {
    val result = Statistics()

    val keywordsCountIterator = streams.store("keywords-count", QueryableStoreTypes.keyValueStore).all
    val languagesCountIterator = streams.store("languages-count", QueryableStoreTypes.keyValueStore).all
    val sentimentsCountIterator = streams.store("sentiments-count", QueryableStoreTypes.keyValueStore).all

    while (languagesCountIterator.hasNext) {
      val next = languagesCountIterator.next
      result.languageStatistics.addOne(LanguageMessages(next.key, next.value))
    }

    while (sentimentsCountIterator.hasNext) {
      val next = sentimentsCountIterator.next
      result.sentimentStatistics.addOne(SentimentMessages(next.key, next.value))
    }

    result
  }
}
