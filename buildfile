require 'buildr/scala'
require 'buildr/jetty'
require 'readline'


VERSION_NUMBER = "0.0.1"

GROUP = "arhen"
COPYRIGHT = "James Bowes"


# Required Maven 2 Repositories
repositories.remote << "http://www.ibiblio.org/maven2/"
# for redstone
repositories.remote << "http://guille.beuno.com.ar/maven-repo/"


# Dependencies
SCALA   = 'org.scala-lang:scala-library:jar:2.7.3'
SERVLET = 'servletapi:servlet-api:jar:2.4'
XMLRPC  = 'redstone:xmlrpc:jar:1.1'


desc "The Arhen project"
define "arhen" do

  project.version = VERSION_NUMBER
  project.group = GROUP
  manifest["Implementation-Vendor"] = COPYRIGHT

  compile.with SCALA, SERVLET, XMLRPC

  task("jetty"=>[package(:war), jetty.use]) do |task|
    jetty.deploy("http://localhost:8080", task.prerequisites.first)
    Readline::readline('[Type ENTER to stop Jetty]')
  end
end
