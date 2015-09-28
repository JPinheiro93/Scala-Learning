package locationservice

import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.testkit.Specs2RouteTest

class LocationServiceSpec extends Specification with Specs2RouteTest with RESTService {
  def actorRefFactory = system

  "RESTService" should {

    "return JSON content with location, with lat and lng inside it" in {
      Get("/LocationService") ~> LocationRoute ~> check {
        responseAs[String] must
          contain("{\"location\":{\"lat\":")
        responseAs[String] must
          contain("\"lng\":")
        responseAs[String] must
          contain("}")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> LocationRoute ~> check {
        status === NotFound
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(LocationRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}
