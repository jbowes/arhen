#!/usr/bin/python

import xmlrpclib
import pprint

s = xmlrpclib.Server("http://localhost:8080/xmlrpc")

pprint.pprint(s.ping.ping())
pprint.pprint(s.registration.anonymous())
pprint.pprint(s.server.listMethods())
