#!/usr/bin/python

from btserver import BluetoothServer
from commands import CommandQueue
from globals import log, app
from lircserver import LircServer
from time import sleep
import signal
import globals
from globals import logger
import logging

#/* ============================================================================= */
#/* FUNCTIONS: IMPLEMENTATIONS */
#/* ============================================================================= */

class MainAppServer:
  _exit = False
  _errors = False
  _errmsg = ""
  
  def init(self):
    self.initlog();
    logger.info("[MainAppServer] Welcome to Phonemote Server 0.1")

    lircServer = LircServer(self);
    queue = CommandQueue(self, lircServer)
    queue.start()
    lircServer.start()

    # start bluetooth server
    btServer = BluetoothServer(self, queue)
    btServer.start() 
    
    signal.signal(signal.SIGTERM, lambda *args: self.exit())

    while True:
        if self._exit: 
            break
        
        try:
            sleep(10)
        except KeyboardInterrupt:
            print "[MainAppServer] ** Got KeyboardInterrupt **"
            break
    
    log("[MainAppServer] Shutting down btlirc server.")
      
    #lircServer.join()
    self.killthread(btServer, "Bluetooth Server")
    self.killthread(queue, "Command Queue")
    self.killthread(lircServer, "LIRC Server")
   
    if self._errors:
      logger.info("[MainAppServer] ** Exit with error: " + self._errmsg)
      
    logger.info("[MainAppServer] Bye bye.")

  def exit(self):
    self._exit = True

  def die(self, msg):
    logger.error("*** ERROR *** " + msg)
    self._errmsg = msg
    self._errors = True
    self.exit()
    
  def killthread(self, t, name):
    log("[MainAppServer] Waiting for " + name + " to shutdown...") 
    
    t.setactive(False)
    
    log("[MainAppServer] ..." + name + " shutdown complete.")
    
  def initlog(self):
    if app.log != None:
      logger.setLevel(app.loglevel)
      
      fh = logging.FileHandler(app.log)
      fh.setLevel(app.loglevel)
      formatter = logging.Formatter("%(asctime)s - %(levelname)s - %(message)s")
      fh.setFormatter(formatter)
      logger.addHandler(fh)
