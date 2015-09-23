package locationservice

import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.http._
import spray.testkit.Specs2RouteTest

class LocationServiceSpec extends Specification with Specs2RouteTest with LocationService {
  def actorRefFactory = system

  //TODO: refazer testes com entrada e saida JSON
  "LocationService" should {

    /*"return a form for GET requests to the root path" in {
      Get() ~> LocationRoute ~> check {
        responseAs[MediaType] must (containing("Location") != null)
        responseAs[String] must contain("Google Maps API")
      }
    }*/

    /*"leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> LocationRoute ~> check {
        status === NotFound
      }
    }*/

    /*"return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(LocationRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }*/
  }
}
