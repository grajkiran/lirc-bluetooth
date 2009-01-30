#!/usr/bin/python

from pref import Preferences
import logging

app = Preferences()

MAX_MSG_SIZE = 250
UUID = "0b8d4d80-9bf6-11dd-b89f-0017319b4e54"

# create logger
logger = logging.getLogger()

def log(s):
  logger.debug(s)
