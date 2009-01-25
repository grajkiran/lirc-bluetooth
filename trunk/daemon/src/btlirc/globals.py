#!/usr/bin/python

from pref import Preferences

mainapp = None

app = Preferences()
app.btserver_port = 0x101f
app.verbose = 1
app.lirc_file = "/dev/lircd"
app.lirc_port = 8765

MAX_MSG_SIZE = 250
UUID = "084a08da-e722-11dd-9bf8-000fb0c7d780"

def log(s, nl=True):
    if app.verbose:
        if nl:
            print s
        else:
            print s,
  
