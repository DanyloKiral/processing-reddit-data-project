package generator_service

import java.time.LocalDateTime
import common.RedditComment
import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._

class DataGenerator (fileName: String) {
  implicit private val formats = org.json4s.DefaultFormats

  def processLines(processFunc: RedditComment => Unit): Unit =
    Source.fromFile(fileName).getLines
      .map(parse(_).extract[Map[String, Any]])
      .map(dict => RedditComment(dict("id").toString, dict("author").toString, dict("body").toString, LocalDateTime.now))
      .foreach(processFunc)
}
