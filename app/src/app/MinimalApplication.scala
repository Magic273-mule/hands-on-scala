
package app
import scalatags.Text.all._
object MinimalApplication extends cask.MainRoutes {
  var messages = Vector(("Damian", "Test"), ("Charles", "test1"))
  val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"

  @cask.get("/")
  def hello(errorOpt: Option[String] = None,
            userName: Option[String] = None,
            msg: Option[String] = None) = doctype("html")(
    html(
      head(link(rel := "stylesheet", href := bootstrap)),
      body(
        div(cls := "container")(
          h1("Chat!"),
          div(id := "messageList")(messageList()),
          for (error <- errorOpt) yield i(color.red)(error),
          form(action := "/", method := "post")(
            input(
              `type` := "text",
              name := "name",
              placeholder := "nombre",
              userName.map(value := _)
            ),
            input(
              `type` := "text",
              name := "msg",
              placeholder := "mensaje",
              msg.map(value := _)
            ),
            input(`type` := "submit")
          )
        )
      )
    )
  )

  def messageList() = frag(for((name, msg)<-messages)yield p(b(name)," ", msg))

  @cask.postJson("/")
  def postChatMsg(name: String, msg: String) = {
    if(name=="") ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
    else if(msg=="") ujson.Obj("success" -> false, "err" -> "msg cannot be empty")
    else {
      messages = messages :+ (name -> msg)
      ujson.Obj("success" -> true, "txt" -> messageList().render, "err" -> "")git
    }
  }

  initialize()
}
