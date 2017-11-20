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


case class Transaction(trans_id:String,authorization_code:String,account_token:String,id:Long,primoris_fee:String,installment_total:String,callID:String,settlement_id:String,payment_detail_id:String,payment_date:String,amount:String,payment_token:String,payment_token_status:String,payment_type:String,
	account_status:String,payment_name:String,amount_total:String,installment_made:String,confirmation_number:String,installment_plan:String,add_date:String,retry_id:String)


case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
    lazy val prev = Option(page - 1).filter(_ >= 0)
    lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}


class WidgetRepository @Inject() (DB: Database) {
    
   
	
    def insert(widget:Widget) = {
    DB.withConnection { implicit connection =>
    	println("Value of name and amount is " + widget.name + "And" + widget.amount)
        SQL("insert into Card_Details values({id},{name},{state},{zip},{account_type},{card_number},{expiration_date},{cvv2},{account_number},{prepaid},{amount})").on('id -> widget.id,
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


    def insertTransaction(transaction:Transaction) = {
    DB.withConnection { implicit connection =>
    	println("Value of trans id and id is " + transaction.trans_id + "And" + transaction.id)
        SQL("insert into transaction values({trans_id},{authorization_code},{account_token},{id},{primoris_fee},{installment_total},{callID},{settlement_id},{payment_detail_id},{payment_date},{amount},{payment_token},{payment_type},{account_status},{payment_name},{amount_total},{installment_made},{confirmation_number},{installment_plan},{add_date},{retry_id})").on('trans_id -> transaction.trans_id,
        			'authorization_code -> transaction.authorization_code,
        			'account_token -> transaction.account_token,
                    'id -> transaction.id,
                    'primoris_fee -> transaction.primoris_fee,
                    'installment_total -> transaction.installment_total,
                    'callID -> transaction.callID,
                    'settlement_id -> transaction.settlement_id,
                    'payment_detail_id -> transaction.payment_detail_id,
                    'payment_date -> transaction.payment_date,
                    'amount -> transaction.amount,
                    'payment_token -> transaction.payment_token,
                    'payment_token_status -> transaction.payment_token_status,
                    'payment_type -> transaction.payment_type,
                    'account_status -> transaction.account_status,
                    'payment_name -> transaction.payment_name,
                    'amount_total -> transaction.amount_total,
                    'installment_made -> transaction.installment_made,
                    'confirmation_number -> transaction.confirmation_number,
                    'installment_plan -> transaction.installment_plan,
                    'add_date -> transaction.add_date,
                    'retry_id -> transaction.retry_id
                ).executeInsert()
    	
    	
    	}
    
    }



     
    
}