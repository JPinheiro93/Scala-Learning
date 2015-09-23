package locationservice

import akka.actor.Actor
import akka.event.Logging
import models._
import spray.httpx.SprayJsonSupport._
import spray.routing.RequestContext

import scala.util.{Failure, Success}
import spray.client.pipelining._

object LocationService {
  case class Process(address : String)
}

class LocationService(requestContext: RequestContext) extends Actor {

  import LocationService._

  def actorRefFactory = context

  implicit val system = context.system
  import system.dispatcher

  val log = Logging(system, getClass)

  def receive = {
    case Process(address) => {
      process(address)
      context.stop(self)
    }
  }

  def process(address: String) = {

    import LocationJsonProtocol._

    val pipeline = sendReceive ~> unmarshal[GoogleLocationApiResult[Location]]
    val responseFuture = pipeline{
      Get(s"https://maps.googleapis.com/maps/api/geocode/json?address=$address")
    }

    import spray.json.{ JsonFormat, DefaultJsonProtocol, JsObject }

    responseFuture onComplete {
      case Success(GoogleLocationApiResult(_, Location(_, geometry) :: _)) =>
        log.info("The location is: {}", geometry)
        requestContext.complete(geometry.asInstanceOf[JsObject])

      case Failure(error) =>
        requestContext.complete(error)
    }
  }
}

/*
trait LocationService extends HttpService {
/*
  def GetLocationRoute =
    path("LocationService") {
      get {
        respondWithMediaType(`text/html`) {
          // XML is Marshalled to `text/xml` by default, so we simply override here
          complete {
            ToResponseMarshallable.isMarshallable(
              <html>
                <body>
                  <h1>Search your
                    <i>Location</i>
                    on
                    <i>Google Maps API</i>
                    !</h1>
                  <form action="LocationServicePost" method="GET" enctype="multipart/form-data">
                    <input type="text" name="Address"></input>
                    <input type="submit" text="Search"></input>
                  </form>
                </body>
              </html>)
          }
        }
      }
    }*/

  def LocationRoute =
    path("LocationService" / Segment) { address =>
      requestContext =>
        respondWithMediaType(MediaTypes.`application/json`) {
          val result = scala.io.Source.fromURL("https://maps.googleapis.com/maps/api/geocode/json?address=" + address)
          val lat = result.length
          val lng = 456
          //TODO: submit request and get answer (result) in JSON
          complete{
            s"""{ "location" : { "lat": "$lat", "lng" : "$lng" } }"""
          }
        }
    }
}*/
