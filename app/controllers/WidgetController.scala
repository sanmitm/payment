package controllers

import javax.inject.Inject
import models._
import play.api.data._
import play.api.i18n._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent.Future
import play.api.libs.ws.ahc.AhcWSClient
import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import play.api.libs.ws._
import play.api.http.HttpEntity
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.ahc._
import play.api.libs.json._
import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import java.util.Calendar
import scala.util.Random
import java.util.UUID
import math._

/**
 * The classic WidgetController using MessagesAbstractController.
 *
 * Instead of MessagesAbstractController, you can use the I18nSupport trait,
 * which provides implicits that create a Messages instances from
 * a request using implicit conversion.
 *
 * See https://www.playframework.com/documentation/2.6.x/ScalaForms#passing-messagesprovider-to-form-helpers
 * for details.
 */
class WidgetController @Inject()(widgetService: WidgetRepository,cc: MessagesControllerComponents)(ws: WSClient) extends MessagesAbstractController(cc) {
  import WidgetForm._

  private val widgets = scala.collection.mutable.ArrayBuffer(
    Widget(0,"Widget 1","Maha", "411023","card","129038109238","0119","232","654654654","yes",2000),
  )

  // The URL to the widget.  You can call this directly from the template, but it
  // can be more convenient to leave the template commpletely stateless i.e. all
  // of the "WidgetController" references are inside the .scala file.
  private val postUrl = routes.WidgetController.createWidget()

  def index = Action {
    Ok(views.html.index())
  }

  def listWidgets = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.listWidgets(widgets, form, postUrl))
  }

  // This will be the action that handles our form post
  def createWidget = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[Data] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.listWidgets(widgets, formWithErrors, postUrl))
    }
    
    
    val successFunction = { data: Data =>
      // This is the good case, where the form was successfully parsed as a Data.
      
     // widgets.append(widget)
     val now = Calendar.getInstance().getTime()
     var s = data.name+data.account_number+now.toString;
     var id = UUID.nameUUIDFromBytes(s.getBytes()).getMostSignificantBits();
     val widget1 = Widget(id,data.name,data.state,data.zip, data.account_type,data.card_number, data.expiration_date, data.cvv2, data.account_number,data.prepaid, data.amount)
      widgetService.insert(widget1)
      Redirect(routes.WidgetController.sendData(id,name = data.name, state = data.state, zip = data.zip, account_type = data.account_type, card_number = data.card_number,   
        expiration_date = data.expiration_date, cvv2 = data.cvv2, account_number = data.account_number, prepaid = data.prepaid, amount = data.amount) )
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }



  def sendData(id:Long,name :String, state:String, zip :String, account_type:String, card_number:String,expiration_date:String,cvv2:String,account_number:String, prepaid:String, amount:Double) = Action { implicit ctx =>

     
      var url1 = "https://tyrion.primorisservices.com/payments/direct"
       import play.api.libs.json.Json
      var obj =  Json.obj(
                                 //  "cardNo" -> this.cardno1,
                                   "key" -> "H6ZJBESFRi0tXcO4gPgYq0N7",
                                   "private_key" -> "rav6auQvi6aOyaKI1ldXJwlt",
                                   "name" -> name,
                                   "state" -> state,
                                   "zip" -> zip,
                                   "account_type" -> account_type,
                                   "card_number" -> card_number,
                               "expiration_date" -> expiration_date,
                               "cvv2" -> cvv2,
                               "account_number" -> account_number,
                               "prepaid" -> prepaid,
                               "amount" -> amount
                               
                               )
      val name1 = name.toString
      val state1 = state.toString
      val zip1 = zip.toString
      val account_type1 = account_type.toString
      val card_number1 = card_number.toString
      val expiration_date1 = expiration_date.toString
      val cvv21 = cvv2.toString
      val account_number1 = account_number.toString
      val prepaid1 = prepaid.toString
      val amount1 = amount.toString


      println("In send to Bank")
      val jsonString: String = Json.stringify(obj)
             val result: Future[Result] = ws.url(url1)
                 .withHttpHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded") 
                 .withRequestTimeout(10000.millis) 
                 .post(
                    Map(
                      "key" -> Seq("H6ZJBESFRi0tXcO4gPgYq0N7"),
                      "private_key" -> Seq("rav6auQvi6aOyaKI1ldXJwlt"),
                      "name" -> Seq(name1),
                      "zip" -> Seq(zip1), 
                      "account_type" -> Seq(account_type1), 
                      "card_number" -> Seq(card_number1), 
                      "expiration_date" -> Seq(expiration_date1), 
                      "cvv2" -> Seq(cvv21), 
                      "account_number"-> Seq(account_number1), 
                      "prepaid"->Seq(prepaid1),
                      "amount"->Seq(amount1)
                    )
                  )  
                  .map { response => 
                   response.status match { 
                        case s if s < 300 =>{ 
                       
                       println("Merchant resp val is .. " +response.body)
                       val json = Json.parse(response.body)
                       
                       var resp_account_token = (json \ "data" \ "account_token").as[String]
                       var resp_primoris_fee = (json \ "data" \ "primoris_fee").as[String]
                       var resp_installment_total = (json \ "data" \ "installment_total").as[String]
                       var resp_callID = (json \ "data" \ "callID").as[String]
                       var resp_settlement_id = (json \ "data" \ "settlement_id").as[String]
                       var resp_payment_detail_id = (json \ "data" \ "payment_detail_id").as[String]
                       var resp_payment_date = (json \ "data" \ "payment_date").as[String]
                       var resp_amount = (json \ "data" \ "amount").as[String]
                       var resp_payment_token = (json \ "data" \ "payment_token").as[String]
                       var resp_payment_token_status = (json \ "data" \ "payment_token_status").as[String]
                       var resp_payment_type = (json \ "data" \ "payment_type").as[String]
                       var resp_account_status = (json \ "data" \ "account_status").as[String]
                       var resp_payment_name = (json \ "data" \ "payment_name").as[String]
                       var resp_amount_total = (json \ "data" \ "amount_total").as[String]
                       var resp_installment_made = (json \ "data" \ "installment_made").as[String]
                       var resp_confirmation_number = (json \ "data" \ "confirmation_number").as[String]
                       var resp_installment_plan = (json \ "data" \ "installment_plan").as[String]
                       var resp_add_date = (json \ "data" \ "add_date").as[String]
                       var resp_retry_id = (json \ "data" \ "retry_id").as[String]
                       var resp_authorization_code = (json \ "data" \ "authorization_code").as[String]
                       var resp_trans_id = (json \ "data" \ "trans_id").as[String]

                       println("Value of json is " + json)
                       println("Value of account token is " + resp_account_token)
                       println("Value of primoris fee is " + resp_primoris_fee)
                       println("Value of resp_installment_total is " + resp_installment_total)
                       println("Value of resp_callID is " + resp_callID)
                       println("Value of resp_settlement_id is " + resp_settlement_id)
                       println("Value of resp_payment_detail_id is " + resp_payment_detail_id)
                       println("Value of resp_payment_date is " + resp_payment_date)
                       println("Value of resp_amount is " + resp_amount)

                       println("Value of resp_payment_token is " + resp_payment_token)
                       println("Value of resp_payment_token_status is " + resp_payment_token_status)
                       println("Value of resp_payment_type is " + resp_payment_type)
                       println("Value of resp_account_status is " + resp_account_status)
                       println("Value of resp_payment_name is " + resp_payment_name)
                       println("Value of resp_amount_total is " + resp_amount_total)
                       println("Value of resp_installment_made is " + resp_installment_made)
                       println("Value of resp_confirmation_number is " + resp_confirmation_number)
                       println("Value of resp_installment_plan is " + resp_installment_plan)
                       println("Value of resp_add_date is " + resp_add_date)
                       println("Value of resp_retry_id is " + resp_retry_id)
                       println("Value of resp_authorization_code is " + resp_authorization_code)
                       println("Value of resp_trans_id is " + resp_trans_id)

                      val transaction = Transaction(resp_trans_id,resp_authorization_code,resp_account_token,id,resp_primoris_fee,resp_installment_total,resp_callID,resp_settlement_id,resp_payment_detail_id,resp_payment_date ,resp_amount,resp_payment_token,resp_payment_token_status,resp_payment_type,resp_account_status,resp_payment_name ,resp_amount_total ,resp_installment_made,resp_confirmation_number,resp_installment_plan,resp_add_date,resp_retry_id)
                      widgetService.insertTransaction(transaction)
                     
                      println("In status match")
                       
                   
                     Ok(response.body) 
                    //Redirect(routes.Application.callMerchant) 
                      
                                            }
                     case _ => 
                      
                      
                        println("In Error")
                        println(response.body)
                       Ok("Status: " + response.status + " / Error: " + response.body) 
                                      

                                        }

                        } 
                        

            
          Ok("Success")
}


}