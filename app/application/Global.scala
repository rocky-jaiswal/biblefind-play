package application

import play.api._
import play.api.mvc._
import com.mongodb.casbah.Imports._

import scala.io.Source
import scala.util.matching.Regex

case class BookSection(book: String, chapter: Int, verse: Int, text: String)

object Global extends WithFilters(MyFilter) {
  
  val mongoDB = MongoClient("localhost", 27017)("main")
  val bibleColl = mongoDB("bible")
  
  def seed() = {
    val books = List("Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation")
    
    val reg = new Regex("\\d+:\\d+")
    var book = ""
    var chapter = 1
    var verse = 1
    var text = ""
    var bookSection = BookSection(book, chapter, verse, text)
    var allVerses: List[BookSection] = List()
    
    for(line <- Source.fromFile("seed/KJV12.txt").getLines()) {
      val cleanLine = line.trim
      if(cleanLine.length > 0) {
        if(books.contains(cleanLine)) {
          book = cleanLine
        } else {
          if(!reg.findFirstMatchIn(cleanLine).isEmpty) {
            chapter = reg.findFirstMatchIn(cleanLine).get.toString().split(":")(0).toInt
            verse   = reg.findFirstMatchIn(cleanLine).get.toString().split(":")(1).toInt
            text    = cleanLine.substring(8, cleanLine.length).trim
            bookSection = BookSection(book, chapter, verse, text)
            allVerses = bookSection :: allVerses
          } else {
            bookSection.text.concat(cleanLine)
          }
        }
      }
    }
    bibleColl.drop()
    allVerses.foreach(verse => bibleColl.insert(MongoDBObject("book" -> verse.book, "chapter" -> verse.chapter, "verse" -> verse.verse, "text" -> verse.text)))
  }

}

object MyFilter extends EssentialFilter {
  
  def apply(next: EssentialAction) = new EssentialAction {
    def apply(request: RequestHeader) = {
      next(request).map(result => 
        result.withHeaders("ACCESS_CONTROL_ALLOW_ORIGIN" -> "http://localhost:9000")
      )
    }
  }
  
}
