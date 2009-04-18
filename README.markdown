Arhen
=====

Arhen is a toy [xmlrpc](http://www.xmlrpc.com) server framework/application
written in [Scala](http://www.scala-lang.org). It doesn't do anything useful,
and probably never will.

Prerequisites
-------------

To build and run Arhen you'll need:
* [Buildr](http://apache.buildr.org)
* [Scala](http://www.scala-lang.org)
* [Ruby](http://www.ruby-lang.org) (for buildr)
* Your favourite JVM
* [Python](http://www.python.org) (optional, for the test script)

Here's how I set things up on [Fedora](http://www.fedora-project.org) (rawhide,
soon to be F11):

### Java and Friends

Setting up Java and Scala is easy enough:

    yum install java-1.6.0-openjdk-devel scala

### Ruby

Install rubygems, as well as ruby-devel and gcc (for compiling the ruby to
java bridge used by buildr):

    yum install rubygems ruby-devel gcc

I like to set up ruby gems so that gems get installed in my home directory, as
described [here](http://jbowes.wordpress.com/2008/05/13/installing-ruby-gems-in-your-home-directory/).

### Buildr

The only trick for buildr is making sure you pick the right `JAVA_HOME` when
you run gem. I used this:

    JAVA_HOME=/usr/lib/jvm/java gem install buildr

Running
-------

Buildr will want a `JAVA_HOME` and `SCALA_HOME` set, so you mayaswell stick
them in your .bash_profile:

    echo "export JAVA_HOME=/usr/lib/jvm/java" >> ~/.bash_profile
    echo "export SCALA_HOME=/usr/share/scala" >> ~/.bash_profile
    source ~/.bash_profile

To launch jetty:

    buildr arhen:jetty

You can then use the provided `test.py` to run some xmlrpc calls against Arhen.
Browsing to http://localhost:8080/xmlrpc will give you a list of the available
xmlrpc methods.
