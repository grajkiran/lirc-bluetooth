#!/usr/bin/python

from pref import Preferences
import logging

app = Preferences()

MAX_MSG_SIZE = 250
UUID = "084a08da-e722-11dd-9bf8-000fb0c7d780"

# create logger
logger = logging.getLogger()

def log(s):
  logger.debug(s)
