package language_service

import com.cybozu.labs.langdetect.{DetectorFactory, LangDetectException}

class LanguageDetector {
  val detectorFactory = new DetectorFactory()

  def init(profileDirectory: String): Unit = {
    detectorFactory.loadProfile(profileDirectory)
  }

  def detect(text: String): Option[String] = {
    try {
      val detector = detectorFactory.create
      detector.append(text)
      Some(detector.detect)
    }
    catch {
      case e: LangDetectException => {
        println("Cannot identify language!")
        None
      }
    }
  }
}
