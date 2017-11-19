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
      Redirect(routes.WidgetController.sendData(name = data.name, state = data.state, zip = data.zip, account_type = data.account_type, card_number = data.card_number,   
        expiration_date = data.expiration_date, cvv2 = data.cvv2, account_number = data.account_number, prepaid = data.prepaid, amount = data.amount) )
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }



  def sendData(name :String, state:String, zip :String, account_type:String, card_number:String,expiration_date:String,cvv2:String,account_number:String, prepaid:String, amount:Double) = Action { implicit ctx =>

     
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
                       var m = (json \ "data" \ "amount").as[String]
                       println("Value of json is " + json)
                       println("Value of amount is " + m)
                      //  val elements = (json \\ "data").children
                      //  for (acct <- elements) {
                      //     val m = acct.extract[Widget]
                      //     println(s"Account: ${m.name}, ${m.amount}")
                          
                      // }
                      // var n = (response.json \ "payment_token_status").as[String]
                      // println("Value of m" + n)    
                      // println("Value of n" + n)
                       println("In status match")
                       


                       //var m = Json.parse(response.bodyAsSource)
                      // println("2nd call val is "+m ) 
                       
                       //this.stat = (m \ "Status").as[String]
                       //this.tr =(m \ "Transaction_id").as[String]
                       //val co = (m \ "Code").as[String]
                     //  this.amt = (m \ "Amount").as[String]
                     //  println("Parsed values: Status ->"+ stat + " Transaction_id ->" + tr + " Code ->"+co)
                       //println("Parsed value: Status -> "+ stat)
                       
                   
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