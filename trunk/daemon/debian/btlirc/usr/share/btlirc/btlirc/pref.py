#!/usr/bin/python

import logging

class Preferences:
  lirc_port = 8765
  lirc_file = "/dev/lircd"
  pid_file = "/var/run/btlirc.pid"
  log = "/var/log/btlirc.log"
  loglevel = logging.INFO
