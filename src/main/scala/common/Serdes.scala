package common

import java.time.LocalDateTime

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

//case object LocalDateSerializer
//  extends CustomSerializer[LocalDateTime](
//    _ =>
//      ({
//        case JString(s) => LocalDateTime.parse(s)
//      }, Map())
//  )
