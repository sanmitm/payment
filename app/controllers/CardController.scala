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
import play.api.libs.json.Json
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global




class CardController @Inject()(cardService: CardRepository,cc: MessagesControllerComponents)(ws: WSClient) extends MessagesAbstractController(cc) {
  
  import CardForm._



  
  private val postUrl = routes.CardController.createCardDetails()
  private val apiUrl = "https://tyrion.primorisservices.com/payments/direct"
  private val publicKey = "H6ZJBESFRi0tXcO4gPgYq0N7"
  private val privateKey = "rav6auQvi6aOyaKI1ldXJwlt" 


  def index = Action {
    Ok(views.html.index())
  }


  def responseErrorForm(status:Int,error:String) = Action {
    
    Ok(views.html.errorForm(status,error))
  }

  def errorForm(error:String) = Action {
    
    Ok(views.html.error(error))
  }


  def successForm(trans_id:String,status:Int,body:String,total_amount:String) = Action {
    
    Ok(views.html.successForm(trans_id,status,body,total_amount))
  }

  def listCardForm = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.listCardForm(form, postUrl))
  }

  // This will be the action that handles the form post method
  def createCardDetails = Action { implicit request: MessagesRequest[AnyContent] =>
    
    val errorFunction = { formWithErrors: Form[Data] =>
     
      BadRequest(views.html.listCardForm(formWithErrors, postUrl))
    }
    
    
    val successFunction = { data: Data =>
    
     val time = Calendar.getInstance().getTime()
     var uniqueString = data.name+data.account_number+time.toString;
     var U_UID = UUID.nameUUIDFromBytes(uniqueString.getBytes()).getMostSignificantBits();
     var id = Math.abs(U_UID)

      Redirect(routes.CardController.sendDataToApi(id,name = data.name, state = data.state, zip = data.zip, account_type = data.account_type, card_number = data.card_number,   
        expiration_date = data.expiration_date, cvv2 = data.cvv2, account_number = data.account_number, prepaid = data.prepaid, amount = data.amount))
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

 
  // This will be the action that sends the form data to primorisservices api
  def sendDataToApi(id:Long,name :String, state:String, zip :String, account_type:String, card_number:String,expiration_date:String,cvv2:String,account_number:String, prepaid:String, amount:Double) = Action { implicit request =>

     
      
       
      var transactionId = ""
      var transactionStatus = 0
      var postError = ""
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
      var totalAmount = ""

      
       
             val result: Future[Result] = ws.url(apiUrl)
                 .withHttpHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded") 
                 .withRequestTimeout(10000.millis) 
                 .post(
                    Map(
                      "key" -> Seq(publicKey),
                      "private_key" -> Seq(privateKey),
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
                  .flatMap { response => 
                   response.status match { 
                        case s if s < 300 =>{ 
                       
                       println("API response is " +response.body)
                      
                       val json = Json.parse(response.body)
                       postError = (json \ "data" \\ "error").mkString(" ")
                       

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
                       totalAmount = resp_amount_total
                       transactionId = resp_trans_id
                       transactionStatus = s
                       
                      val cardDetails = Card(id,name,state,zip,account_type,card_number takeRight 4,expiration_date,cvv2,account_number,prepaid,amount) 
                      cardService.insert(cardDetails)  
                      val transaction = Transaction(resp_trans_id,resp_authorization_code,resp_account_token,id,resp_primoris_fee,resp_installment_total,resp_callID,resp_settlement_id,resp_payment_detail_id,resp_payment_date ,resp_amount,resp_payment_token,resp_payment_token_status,resp_payment_type,resp_account_status,resp_payment_name ,resp_amount_total ,resp_installment_made,resp_confirmation_number,resp_installment_plan,resp_add_date,resp_retry_id)
                      cardService.insertTransaction(transaction)
                      Future.successful(Ok("Response is "+response.body)) 
                        
                                            }

                     case _ => 
                      Future.successful(Redirect(routes.CardController.responseErrorForm(transactionStatus,postError) )) 
                                 
                                        }
                           
                          } 

            Thread.sleep(6000)
            Redirect(routes.CardController.result(transactionId,transactionStatus,postError,totalAmount) )
  }


  // This will be the action that displays the appropriate result to the user
  def result(trans_id:String,stat:Int,error1:String,total_amount:String) = Action {
    
     //if error is not dete
     if(!error1.isEmpty())
     {
        var error = error1.replaceAll("\\{\\}\"\\[\\]\""," ")
        error = error.replaceAll("\"","")
       Redirect(routes.CardController.errorForm(error)) 
     } 
     else
     {
        Redirect(routes.CardController.successForm(trans_id,stat,"Transaction successful",total_amount))
     }


  }


 
 
}