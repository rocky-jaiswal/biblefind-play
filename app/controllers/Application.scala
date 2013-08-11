package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import com.mongodb.casbah.Imports._
import application.Global

object Application extends Controller {
  
  def index = Action {
    Ok(Json.toJson(Map("hello" -> "world")))
  }
  
  def books = Action {
    val coll = Global.bibleColl
    val books = coll.distinct("book")
    Ok(Json.toJson(books.toList.map(book => book.toString())))
  }
  
}
