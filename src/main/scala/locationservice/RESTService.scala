package locationservice

import akka.actor.{Props, Actor}
import spray.json.{JsString, JsObject}
import spray.routing.HttpService

class RESTServiceActor extends Actor with RESTService {

  def actorRefFactory = context

  def receive = runRoute(LocationRoute)
}

trait RESTService extends HttpService {

  def LocationRoute =
    path("LocationService" / Segment) {
      strAddress => {
        requestContext =>
          val locationService = actorRefFactory.actorOf(Props(new LocationService(requestContext)))
          /*val headers = requestContext.request.headers
          val strAddress = headers.map(h => h.name -> h.value).toMap.get("address").get*/
          val address = new JsString(strAddress)
          locationService ! LocationService.Process(new JsObject(Map("address" -> address)))
      }
    } ~
    path("LocationService") {
      requestContext =>
        val locationService = actorRefFactory.actorOf(Props(new LocationService(requestContext)))
        val headers = requestContext.request.headers
        val strAddress = headers.map(h => h.name -> h.value).toMap.get("address").get
        val address = new JsString(strAddress)
        locationService ! LocationService.Process(new JsObject(Map("address" -> address)))
    }
}
