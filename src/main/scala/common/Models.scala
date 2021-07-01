package common

import scala.collection.mutable.ArrayBuffer

case class RedditComment(id: String, author: String, comment: String)


case class LanguageMessages(language: String, messagesNum: Long)
case class SentimentMessages(sentiment: String, messagesNum: Long)
case class KeywordsMessages(keyword: String, messagesNum: Long)
case class Statistics(
                       languageStatistics: ArrayBuffer[LanguageMessages] = ArrayBuffer(),
                       sentimentStatistics: ArrayBuffer[SentimentMessages] = ArrayBuffer(),
                       var top10Keywords: Seq[KeywordsMessages] = Seq()
                     )
