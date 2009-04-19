package arhen.webapp

import scala.collection.jcl.Conversions.convertList
import javax.servlet.ServletConfig
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import redstone.xmlrpc.{XmlRpcServlet, XmlRpcCustomSerializer, XmlRpcSerializer}

import arhen.xmlrpc._


class XmlRpcInvocationHandler (var name: String, var objekt: Object)

/**
 * XML-RPC Introspection extensions, as outlined at
 * http://scripts.incutio.com/xmlrpc/introspection.html
 **/
class System (handlers: List[XmlRpcInvocationHandler]) {
  def listMethods() = {
    var methods :List[String] = Nil
    
    handlers.foreach {
      handler => handler.objekt.getClass.getDeclaredMethods.foreach {
        method => methods = methods ::: List(handler.name + "." + method.getName)
      }
    }

    methods.filter(!_.endsWith("$tag"))
  }

  private def classToXmlRpcType(klass :Any) = {
    "string"
  }

  def methodSignature(qualifiedMethod: String) = {
    var signatures :List[List[String]] = Nil
    val parts = List.fromString(qualifiedMethod, '.')
    val methodName = parts.last
    val handlerName = parts.dropRight(1).reduceLeft { (b, a) => b + "." + a }

    val handler = handlers.filter(_.name == handlerName).head

    handler.objekt.getClass.getDeclaredMethods.filter(_.getName != "$tag")
      .map {method => List(classToXmlRpcType(method.getReturnType))
        .++(method.getParameterTypes.map(classToXmlRpcType(_)))
      }
  }

  def methodHelp(qualifiedMethod: String) = "methodHelp is not Implemented"
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

  var system :System = _

  override def init(servletConfig: ServletConfig) {
     super.init(servletConfig) 

     var serializer = getXmlRpcServer().getSerializer()
     serializer.addCustomSerializer(new ScalaListSerializer)
     
     val invocationHandlers = List(
       new XmlRpcInvocationHandler("echo", new Echo()),
       new XmlRpcInvocationHandler("ping", new Ping()),
       new XmlRpcInvocationHandler("registration", new Registration())
     )

     for (handler <- invocationHandlers) {
       getXmlRpcServer().addInvocationHandler(handler.name, handler.objekt)
     }

     this.system = new System(invocationHandlers)
     getXmlRpcServer().addInvocationHandler("system", this.system)
  }

  override def doGet(servletRequest: HttpServletRequest,
    servletResponse: HttpServletResponse) {
    var writer = servletResponse.getWriter()
     
    writer.write("<html><head><title>xmlrpc methods</title></head>")
    writer.write("<body>")
    
    writer.write("<ul>")
    for (method <- this.system.listMethods())
      writer.write("<li>" + method + "\n")
    writer.write("</ul>")

    writer.write("</body>")
    writer.write("</html>")
    writer.write("\n")
  }
}
