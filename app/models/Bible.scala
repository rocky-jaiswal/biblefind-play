package models

import com.mongodb.casbah.Imports._
import application.Global

object Bible {

  //Get all book in the Bible
  def books() = {
    val books = Global.bibleColl.distinct("book")
    books.toList.map(book => book.toString())
  }
  
  //Get the number of chapters in a book
  //db.bible.find({book: "Genesis"}).sort({"chapter": -1}).limit(1)
  def chapters(book: String) = {
    val query = MongoDBObject("book" -> book)
    val queryChapter = MongoDBObject("chapter" -> -1)
    val result = Global.bibleColl.findOne(query)
    Map("size" -> result.map(r => r("chapter")).getOrElse(-1).asInstanceOf[Int])
  }
  
  //Get the number of verses in a chapter of a book
  //db.bible.find({book: "Genesis", chapter: 5}).sort({"chapter": -1}).limit(1)
  def verses(book: String, chapter: Int) = {
    val query = MongoDBObject("book" -> book) ++ MongoDBObject("chapter" -> chapter)
    val queryChapter = MongoDBObject("chapter" -> -1)
    val result = Global.bibleColl.findOne(query)
    Map("size" -> result.map(r => r("verse")).getOrElse(-1).asInstanceOf[Int])
  
  }
  
  //Get the verse
  def verse(book: String, chapter: Int, verse: Int) = {
    val query = MongoDBObject("book" -> book) ++ MongoDBObject("chapter" -> chapter) ++ MongoDBObject("verse" -> verse)
    val result = Global.bibleColl.findOne(query)
    Map("verse" -> result.map(r => r("text")).getOrElse("not found").toString())
  }  
  
}
