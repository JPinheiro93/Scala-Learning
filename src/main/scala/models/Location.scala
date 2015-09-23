package models

import spray.json.{ JsonFormat, DefaultJsonProtocol, JsObject }

case class Location(place_id : String, geometry : JsObject)
case class GoogleLocationApiResult[T](status: String, results: List[T])

object LocationJsonProtocol extends DefaultJsonProtocol {
  implicit val geometryFormat = jsonFormat2(Location)
  implicit def googleElevationApiResultFormat[T :JsonFormat] = jsonFormat2(GoogleLocationApiResult.apply[T])
}