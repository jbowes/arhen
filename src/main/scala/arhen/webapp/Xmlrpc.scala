package arhen.webapp

import scala.collection.jcl.Conversions.convertList
import javax.servlet.ServletConfig
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import redstone.xmlrpc.{XmlRpcServlet, XmlRpcCustomSerializer, XmlRpcSerializer}

import arhen.xmlrpc._
import Reflection.ScalaReflection._

class XmlRpcInvocationHandler (var name: String, var objekt: Object)

class Server (handlers: List[XmlRpcInvocationHandler]) {
  def listMethods() = {
    var methods :List[String] = Nil
    
    handlers.foreach {
      handler => handler.objekt.methods.foreach {
        method => methods = methods ::: List(handler.name + "." + method)
      }
    }

    methods
  }
}

class ScalaListSerializer extends XmlRpcCustomSerializer {
  def getSupportedClass() = classOf[List[Object]]
  def serialize(value: Object, output: java.io.Writer,
    builtInSerializer: XmlRpcSerializer) {
    val list :List[Object] = value.asInstanceOf[List[Object]]

    builtInSerializer.serialize(java.util.Arrays.asList(list.toArray:_*),
      output)
  }
}

class Xmlrpc extends XmlRpcServlet {

  var server :Server = _

  override def init(servletConfig: ServletConfig) {
     super.init(servletConfig) 

     var serializer = getXmlRpcServer().getSerializer()
     serializer.addCustomSerializer(new ScalaListSerializer)
     
     val invocationHandlers = List(
       new XmlRpcInvocationHandler("ping", new Ping()),
       new XmlRpcInvocationHandler("registration", new Registration())
     )

     for (handler <- invocationHandlers) {
       getXmlRpcServer().addInvocationHandler(handler.name, handler.objekt)
     }

     this.server = new Server(invocationHandlers)
     getXmlRpcServer().addInvocationHandler("server", this.server)
  }

  override def doGet(servletRequest: HttpServletRequest,
    servletResponse: HttpServletResponse) {
    var writer = servletResponse.getWriter()
     
    writer.write("<html><head><title>xmlrpc methods</title></head>")
    writer.write("<body>")
    
    writer.write("<ul>")
    for (method <- this.server.listMethods())
      writer.write("<li>" + method + "\n")
    writer.write("</ul>")

    writer.write("</body>")
    writer.write("</html>")
    writer.write("\n")
  }
}
