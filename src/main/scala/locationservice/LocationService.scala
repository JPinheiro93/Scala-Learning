package locationservice

import akka.actor.Actor
import akka.event.Logging
import models._
import spray.httpx.SprayJsonSupport._
import spray.json.JsObject
import spray.routing.RequestContext

import scala.util.{Failure, Success}
import spray.client.pipelining._

object LocationService {
  case class Process(address : JsObject)
}

class LocationService(requestContext: RequestContext) extends Actor {

  import LocationService._

  def actorRefFactory = context

  implicit val system = context.system
  import system.dispatcher

  val log = Logging(system, getClass)

  def receive = {
    case Process(address) =>
      process(address)
      context.stop(self)
  }

  def process(address: JsObject) = {

    import LocationJsonProtocol._

    val addressStrParam = address.fields("address").toString()
    val addressParam = addressStrParam.substring(1, addressStrParam.length - 1).replaceAll(" ", "+")
    val pipeline = sendReceive ~> unmarshal[GoogleLocationApiResult[Location]]
    val responseFuture = pipeline{
      Get(s"""https://maps.googleapis.com/maps/api/geocode/json?address=$addressParam""")
    }

    responseFuture onComplete {
      case Success(GoogleLocationApiResult(_, Location(location) :: _)) =>
        val locationJson = location.fields("location").asJsObject
        val result = s"""{"location":$locationJson}"""
        requestContext.complete(result)

      case Failure(error) =>
        requestContext.complete(error)
    }
  }
}
