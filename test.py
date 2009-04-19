#!/usr/bin/python

import xmlrpclib
import pprint

s = xmlrpclib.Server("http://localhost:8080/xmlrpc")

pprint.pprint(s.echo.echo("HI"))
pprint.pprint(s.ping.ping())
pprint.pprint(s.registration.anonymous())
pprint.pprint(s.system.listMethods())

pprint.pprint(s.system.methodSignature('echo.echo'))
pprint.pprint(s.system.methodSignature('ping.ping'))
