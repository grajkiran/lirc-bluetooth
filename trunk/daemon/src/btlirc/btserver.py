#!/usr/bin/python

from globals import log, app
import bluetooth
import globals
import threading
import utils

#/* ============================================================================= */
#/* FUNCTIONS: IMPLEMENTATIONS */
#/* ============================================================================= */

class BluetoothServer (threading.Thread, utils.NonBlockingThread):
  port = None
  queue = None
  mainapp = None
  
  def __init__(self, mainapp, port, queue):
    self.port = port
    self.mainapp = mainapp
    self.queue = queue
    threading.Thread.__init__ (self)
    
  def run(self):
    log("[BluetoothServer] Initializing Bluetooth Server")
    
    stype = bluetooth.RFCOMM
    port = bluetooth.PORT_ANY
    
    server_sock = bluetooth.BluetoothSocket(stype)
    server_sock.bind(("", port))
    server_sock.listen(5)
    
    try:
        bluetooth.advertise_service(server_sock, "Bluemote Service", globals.UUID)
    except bluetooth.BluetoothError, be:
        self.mainapp.die("[BluemoteServer] Can't advertise service. Are the drivers running? Is there a bluetooth device?: " + str(be))
        return

    server_sock.settimeout(2)
    
    while True: # main accept() loop
      if not self.active:
          break

      if app.verbose:
        log("[BluetoothServer] Waiting for bluetooth conections...")

      client_sock = None
      while client_sock == None:
        if not self.active:
          break
                
        try:
          client_sock, address = server_sock.accept()
        except:
          #if app.verbose == 2: print ".", 
          pass
        
      if client_sock == None:
        continue

      if app.verbose:
        log("[BluetoothServer] Accepted connection from " + str(address))
        
      t = BluetoothClientHandle(client_sock, self.queue)
      t.start()

    server_sock.close()
    
    if app.verbose:
      log("[BluetoothServer] Bye bye.")
    
    return True


class BluetoothClientHandle(threading.Thread):
  def __init__(self, socket, queue):
    self.socket = socket
    self.queue = queue
    threading.Thread.__init__ (self)
    
  def run (self):
    
    if app.verbose:
      log("[BluetoothClientHandle] Taking connection")
    while True:
      if app.verbose:
        log("[BluetoothClientHandle] waiting data...")
        
      buff = self.socket.recv(globals.MAX_MSG_SIZE)
      if not buff: break

      if app.verbose:
        log("[BluetoothClientHandle] got: " + buff)
      self.queue.addCommand(buff);
      #lircde_process_command
    
    if app.verbose:
      log("[BluetoothClientHandle] Connection finished")
      
    self.socket.close()
