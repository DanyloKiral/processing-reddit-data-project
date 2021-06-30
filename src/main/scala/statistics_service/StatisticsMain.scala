package statistics_service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{concat, get, handle, handleSync, path, withoutRequestTimeout}
import akka.stream.scaladsl.Source
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import common.Configs

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.stream.scaladsl.Source
import common.Configs
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object StatisticsMain extends App {
  implicit val system = ActorSystem(Behaviors.empty, "kafka-throughput-investigation")
  implicit val executionContext = ExecutionContext.global
  implicit val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().newServerAt(Configs.StatisticsHost, Configs.StatisticsPort).connectionSource()

  println(f"Server is starting on port ${Configs.StatisticsPort}")
  serverSource.runForeach { connection =>
    println("Accepted new connection from " + connection.remoteAddress)
    connection.handleWith(
      get {
        concat(
          path("health") {
            handleSync(_ => HttpResponse(StatusCodes.OK))
          },
          path("statistics") {
            withoutRequestTimeout {
              handleSync(_ => HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`,
                mapper.writeValueAsString(statisticsAggregator.collectStatistics))))
            }
          }
        )
      }
    )
  } onComplete {
    case Success(_) => {
      println("Success. Closing connection")
    }
    case Failure(exception) => {
      println(f"Failure. ${exception}")
      println("Closing connection")
    }
  }

  val statisticsAggregator = new StatisticsAggregator()
}
