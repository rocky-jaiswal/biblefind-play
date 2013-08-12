package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import models.Bible

object Api extends Controller {
  
  def books = Action {
    Ok(Json.toJson(Bible.books()))
  }
  
  def chapters(book: String) = Action {
    Ok(Json.toJson(Bible.chapters(book)))
  }
  
  def verses(book: String, chapter: Int) = Action {
    Ok(Json.toJson(Bible.verses(book, chapter)))
  }
  
  def verse(book: String, chapter: Int, verse: Int) = Action {
    Ok(Json.toJson(Bible.verse(book, chapter, verse)))
  }
  
}
