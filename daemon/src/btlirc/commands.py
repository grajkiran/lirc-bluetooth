#!/usr/bin/python

import threading
from globals import log,app
from time import sleep
import platform
import utils

mutex = threading.Lock()

class CommandQueue( threading.Thread,  utils.NonBlockingThread ):
  commands = None
  lircServer = None
  
  def __init__(self, mainapp,  lircServer):
    self.commands = []
    self.lircServer = lircServer
    threading.Thread.__init__ ( self )
  
  def addCommand(self, cmd):
    mutex.acquire()
    
    log("[CommandQueue] Adding command: "+cmd)
    self.commands.append(cmd)
    
    mutex.release()
  
  def run(self):
    
    while True:
      if not self.isactive():
          break
      
      mutex.acquire()
      
      if len(self.commands) > 0:
        for cmd in self.commands:
            self.lircServer.sendCommand(Command(cmd).toString())
        self.commands = []
      
      mutex.release()
        
      sleep(0.1)

class Command:
  keyCode = "0x9"
  command = ""
  repeat = 0
  control = "BLUELIRC"
  
  def __init__(self, command=''):
    self.command = command
    
    if self.command[-1] == '\n': self.command = self.command[:-1]
    if self.command[-1] == '\0': self.command = self.command[:-1]
     
    if platform.system() == 'Linux':
        self.keyCode = "0x9"
    else:
        self.keyCode = "0000000000eab154"

  def toString(self):
    return "0x9 0x0 %s BLUELIRC" % self.command
 
