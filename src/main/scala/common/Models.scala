package common

import java.time.LocalDateTime

case class RedditComment(id: String, author: String, comment: String, processingStartedAt: LocalDateTime)
