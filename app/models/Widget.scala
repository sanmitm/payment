package models
import java.util.Date
import scala.concurrent.Future
import play.api.mvc._
import anorm._
import anorm.SqlParser._
import javax.inject.Inject
// Required for the Play db functionality
import play.api.db._
import play.api.Play.current
/**
 * Presentation object used for displaying data in a template.
 *
 * Note that it's a good practice to keep the presentation DTO,
 * which are used for reads, distinct from the form processing DTO,
 * which are used for writes.
 */
case class Widget(id:Long,name: String, state: String, zip: String, account_type:String, card_number:String, expiration_date:String, cvv2:String, account_number:String, prepaid:String,amount:Double)



case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
    lazy val prev = Option(page - 1).filter(_ >= 0)
    lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}


class WidgetRepository @Inject() (DB: Database) {
    
   
	
    def insert(widget:Widget) = {
    DB.withConnection { implicit connection =>
    	println("Value of name and amount is " + widget.name + "And" + widget.amount)
        SQL("insert into Computer values({id},{name},{state},{zip},{account_type},{card_number},{expiration_date},{cvv2},{account_number},{prepaid},{amount})").on('id -> widget.id,
        			'name -> widget.name,
        			'state -> widget.state,
                    'zip -> widget.zip,
                    'account_type -> widget.account_type,
                    'card_number -> widget.card_number,
                    'expiration_date -> widget.expiration_date,
                    'cvv2 -> widget.cvv2,
                    'account_number -> widget.account_number,
                    'prepaid -> widget.prepaid,
                    'amount -> widget.amount
                ).executeInsert()
    	
    	
    	}
    
    }
     
    
}