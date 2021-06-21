package common

import scala.util.Properties

object Configs {
  val InputFileName = Properties.envOrElse("KFTI_INPUT_FILE_NAME", "data/RC_2010-12")
  val MessagesTopicName = Properties.envOrElse("KFTI_MESSAGES_TOPIC_NAME", "reddit-messages")
  val KafkaBrokerAddress = Properties.envOrElse("KFTI_KAFKA_BROKER_ADDRESS", "localhost:9092")

  val StatisticsHost = Properties.envOrElse("KFTI_STATISTICS_HOST", "localhost")
  val StatisticsPort = Properties.envOrElse("KFTI_STATISTICS_PORT", "1234").toInt
}
