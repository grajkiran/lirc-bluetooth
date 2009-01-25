#!/usr/bin/python

from btserver import BluetoothServer
from commands import CommandQueue
from globals import log, app
from lircserver import LircServer
from time import sleep
import globals

#/* ============================================================================= */
#/* FUNCTIONS: IMPLEMENTATIONS */
#/* ============================================================================= */

class MainAppServer:
  _exit = False
  _errors = False
  _errmsg = ""
  
  def init(self, daemonize, lirc_file):
    log("[MainAppServer] Welcome to Phonemote Server 0.1")

    lircServer = LircServer(self, lirc_file);
    queue = CommandQueue(self, lircServer)
    queue.start()
    lircServer.start()

    # start bluetooth server
    btServer = BluetoothServer(self, app.btserver_port, queue)
    btServer.start() 

    while True:
        if self._exit: 
            break
        
        try:
            sleep(.5)
        except KeyboardInterrupt:
            print "[MainAppServer] ** Got KeyboardInterrupt **"
            break
    
    if app.verbose:
      log("[MainAppServer] Shutting down phonemote server.")
      
    #lircServer.join()
    self.killthread(btServer, "Bluetooth Server")
    self.killthread(queue, "Command Queue")
    self.killthread(lircServer, "LIRC Server")
   
    if self._errors:
      log("[MainAppServer] ** Exit with error: " + self._errmsg)
      
    if app.verbose:
        log("[MainAppServer] Bye bye.")

    #return TRUE;

  def exit(self):
    self._exit = True

  def die(self, msg):
    print "*** ERROR *** " + msg
    self._errmsg = msg
    self._errors = True
    self.exit()
    
  def killthread(self, t, name):
    if app.verbose:
      log("[MainAppServer] Waiting for " + name + " to shutdown...") 
    
    t.setactive(False)
    #t.join()
    
    if app.verbose:
      log("[MainAppServer] ..." + name + " shutdown complete.")

