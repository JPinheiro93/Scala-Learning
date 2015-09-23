package locationservice

import akka.actor.{Props, Actor}
import spray.routing.HttpService

class RESTServiceActor extends Actor with RESTService {

  def actorRefFactory = context

  def receive = runRoute(LocationRoute)
}

trait RESTService extends HttpService {
  def LocationRoute =
    path("LocationService" / Segment) { address =>
      requestContext =>
        val elevationService = actorRefFactory.actorOf(Props(new LocationService(requestContext)))
        elevationService ! LocationService.Process(address)
    }
}
