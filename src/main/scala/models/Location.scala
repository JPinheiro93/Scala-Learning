package models

import spray.json.{ JsonFormat, DefaultJsonProtocol, JsObject }

case class Location(geometry : JsObject)
case class GoogleLocationApiResult[T](status: String, results: List[T])

object LocationJsonProtocol extends DefaultJsonProtocol {
  implicit val locationFormat = jsonFormat1(Location)
  implicit def googleLocationApiResultFormat[T :JsonFormat] = jsonFormat2(GoogleLocationApiResult.apply[T])
}