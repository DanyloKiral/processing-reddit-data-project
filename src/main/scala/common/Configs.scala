package common

import scala.util.Properties

object Configs {
  val InputFileName = Properties.envOrElse("KFTI_INPUT_FILE_NAME", "data/RC_2010-12")
  val KafkaBrokerAddress = Properties.envOrElse("KFTI_KAFKA_BROKER_ADDRESS", "localhost:9092")

  val CommentsTopicName = Properties.envOrElse("KFTI_COMMENTS_TOPIC_NAME", "reddit-comments")
  val CommentsTopicPartitions = Properties.envOrElse("KFTI_COMMENTS_TOPIC_PARTITIONS", "1").toInt
  val CommentsTopicReplication = Properties.envOrElse("KFTI_COMMENTS_TOPIC_REPLICATION", "1").toShort

  val KeywordsTopicName = Properties.envOrElse("KFTI_KEYWORDS_TOPIC_NAME", "comments-keywords")
  val KeywordsTopicPartitions = Properties.envOrElse("KFTI_KEYWORDS_TOPIC_PARTITIONS", "1").toInt
  val KeywordsTopicReplication = Properties.envOrElse("KFTI_KEYWORDS_TOPIC_REPLICATION", "1").toShort

  val SentimentsTopicName = Properties.envOrElse("KFTI_SENTIMENTS_TOPIC_NAME", "comments-sentiments")
  val SentimentsTopicPartitions = Properties.envOrElse("KFTI_SENTIMENTS_TOPIC_PARTITIONS", "1").toInt
  val SentimentsTopicReplication = Properties.envOrElse("KFTI_SENTIMENTS_TOPIC_REPLICATION", "1").toShort

  val LanguagesTopicName = Properties.envOrElse("KFTI_LANGUAGES_TOPIC_NAME", "comments-languages")
  val LanguagesTopicPartitions = Properties.envOrElse("KFTI_LANGUAGES_TOPIC_PARTITIONS", "1").toInt
  val LanguagesTopicReplication = Properties.envOrElse("KFTI_LANGUAGES_TOPIC_REPLICATION", "1").toShort

  val StatisticsHost = Properties.envOrElse("KFTI_STATISTICS_HOST", "0.0.0.0")
  val StatisticsPort = Properties.envOrElse("KFTI_STATISTICS_PORT", "1234").toInt
}
