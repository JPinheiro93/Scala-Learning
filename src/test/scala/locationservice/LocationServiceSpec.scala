package locationservice

import spray.http.StatusCodes._
import org.scalatest.FreeSpec
import org.scalatest.Matchers
import spray.testkit.ScalatestRouteTest

class LocationServiceSpec extends FreeSpec with RESTService with ScalatestRouteTest with Matchers {
  def actorRefFactory = system

  "The Location Service" - {
    "when calling GET LocationService with address: 'Eendrachtlaan 315, Utrecht'" - {
      "should return '{\"location\":{\"lat\":52.0618174,\"lng\":5.1085974}}'" in {
          Get("/LocationService/Eendrachtlaan+315,+Utrecht") ~> LocationRoute ~> check {
          status should equal(OK)
          entity.toString should equal("{\"location\":{\"lat\":52.0618174,\"lng\":5.1085974}}")
        }
      }
    }
  }


  /*"RESTService" should {

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
  }*/
}
