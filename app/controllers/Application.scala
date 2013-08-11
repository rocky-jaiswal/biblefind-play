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
  
  //Get all book in the Bible
  def books = Action {
    val books = Global.bibleColl.distinct("book")
    Ok(Json.toJson(books.toList.map(book => book.toString())))
  }
  
  //Get the number of chapters in a book
  //db.bible.find({book: "Genesis"}).sort({"chapter": -1}).limit(1)
  def chapters(book: String) = Action {
    val query = MongoDBObject("book" -> book)
    val queryChapter = MongoDBObject("chapter" -> -1)
    val result = Global.bibleColl.find(query).sort(queryChapter).limit(1)
    Ok(Json.toJson(Map("size" -> result.map(r => r("chapter")).next().asInstanceOf[Int])))
  }
  
  //Get the number of verses in a chapter of a book
  //db.bible.find({book: "Genesis", chapter: 5}).sort({"chapter": -1}).limit(1)
  def verses(book: String, chapter: Int) = Action {
    val query = MongoDBObject("book" -> book) ++ MongoDBObject("chapter" -> chapter)
    val queryChapter = MongoDBObject("chapter" -> -1)
    val result = Global.bibleColl.find(query).sort(queryChapter).limit(1)
    Ok(Json.toJson(Map("size" -> result.map(r => r("verse")).next().asInstanceOf[Int])))
  
  }
  
  //Get the verse
  def verse(book: String, chapter: Int, verse: Int) = Action {
    val query = MongoDBObject("book" -> book) ++ MongoDBObject("chapter" -> chapter) ++ MongoDBObject("verse" -> verse)
    val result = Global.bibleColl.findOne(query)
    Ok(Json.toJson(Map("verse" -> result.get("text").toString())))
  }
  
}
