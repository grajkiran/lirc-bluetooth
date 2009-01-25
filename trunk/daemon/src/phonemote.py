#!/usr/bin/python

from btlirc import globals
from btlirc.mainapp import MainAppServer
import sys
import getopt

def show_usage():
  print "LIRC Daemon Emulator"
  print "usage: bluelirc [-d]"
  print "   -d  daemonize"
  print "   -f  lircd file"
  sys.exit(0)

optlist, list = getopt.getopt(sys.argv[1:], 'hdf:')
daemonize = False
lirc_file = None

for opt in optlist:
  if opt[0] == '-d':
    daemonize = True
  elif opt[0] == '-f':
    lirc_file = opt[1]
  elif opt[0] == '-h':
    show_usage()
  else:
    show_usage()

mas = MainAppServer()
mas.init(daemonize, lirc_file)
