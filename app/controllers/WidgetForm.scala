package controllers

object WidgetForm {
  import play.api.data.Forms._
  import play.api.data.Form
  import play.api.data.format.Formats._

  /**
   * A form processing DTO that maps to the form below.
   *
   * Using a class specifically for form binding reduces the chances
   * of a parameter tampering attack and makes code clearer.
   */
  case class Data(name: String, state: String, zip: String, account_type:String, card_number:String, expiration_date:String, cvv2:String, account_number:String, prepaid:String,amount:Double)

  /**
   * The form definition for the "create a widget" form.
   * It specifies the form fields and their types,
   * as well as how to convert from a Data to form data and vice versa.
   */
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "state" -> nonEmptyText,
      "zip" -> nonEmptyText,
      "account_type"-> nonEmptyText,
      "card_number"-> nonEmptyText,
      "expiration_date"-> nonEmptyText,
      "cvv2"-> nonEmptyText,
      "account_number"-> nonEmptyText,
      "prepaid"-> nonEmptyText,
      "amount"-> of(doubleFormat)

    )(Data.apply)(Data.unapply)
  )
}


