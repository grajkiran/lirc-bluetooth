#!/usr/bin/python

from btlirc import globals
from btlirc.mainapp import MainAppServer
import sys
import getopt
import time
from btlirc.daemon import Daemon
from btlirc.globals import app
import logging

def usage():
  print "LIRC Daemon Emulator"
  print "usage: %s [-o] [-L] [-v] [-P] start|stop|restart" % sys.argv[0]
  print "   -o  output socket filename (/dev/lircd)"
  print "   -L  log file to write"
  print "   -v  turn on debug messages"
  print "   -P  pid file for daemon"
  
  sys.exit(0)

class MyDaemon(Daemon):
  def run(self):
    mas = MainAppServer()
    mas.init()
    
if __name__ == "__main__":
  optlist, args = getopt.getopt(sys.argv[1:], 'ho:L:vP:')
  for (opt, value) in optlist:
    if opt == '-o':
      app.lirc_file = value
    elif opt == '-L':
      app.log = value
    elif opt == '-v':
      app.loglevel = logging.DEBUG
    elif opt == '-P':
      app.pid_file = value
    elif opt == '-h':
      usage()
    else:
      usage()

  daemon = MyDaemon(app.pid_file)
  daemon.start()
