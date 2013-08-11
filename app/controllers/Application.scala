package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import com.mongodb.casbah.Imports._
import application.Global

object Application extends Controller {
  
  def index = Action {
    val db = Global.mongoDB
    val coll = db("testData")
    val response = coll.findOne( MongoDBObject("hello" -> "world") )
    response match {
      case Some(res) =>
        Ok(Json.toJson(Map("hello" -> "world")))
      case None =>
        Ok(Json.toJson(Map("not found" -> true)))
    }
  }
  
}
