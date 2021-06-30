package sentiment_service

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.trees.Tree
import sentiment_service.Sentiment.Sentiment

object SentimentAnalyzer {

  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
  val pipeline: StanfordCoreNLP = new StanfordCoreNLP(props)

  def mainSentiment(input: String): Sentiment = Option(input) match {
    case Some(text) if !text.isEmpty => extractSentiment(text)
    case _ => throw new IllegalArgumentException("input can't be null or empty")
  }

  private def extractSentiment(text: String): Sentiment = {
    val (_, sentiment) = extractSentiments(text)
      .maxBy { case (sentence, _) => sentence.length }
    sentiment
  }

  def extractSentiments(text: String): List[(String, Sentiment)] = {
    val annotation: Annotation = pipeline.process(text)
    val sentences = annotation.asInstanceOf[Array[CoreAnnotations.SentencesAnnotation]]
    sentences
      .map(sentence => (sentence, sentence.asInstanceOf[Tree]))
      .map { case (sentence, tree) => (sentence.toString,Sentiment.toSentiment(RNNCoreAnnotations.getPredictedClass(tree))) }
      .toList
  }

}