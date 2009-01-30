#!/usr/bin/python

from globals import log, app, logger
import bluetooth
import globals
import threading
import utils
import utils
import socket


#/* ============================================================================= */
#/* FUNCTIONS: IMPLEMENTATIONS */
#/* ============================================================================= */

class BluetoothServer (threading.Thread, utils.NonBlockingThread):
  port = None
  queue = None
  mainapp = None
  
  def __init__(self, mainapp, queue):
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

      logger.info("[BluetoothServer] Waiting for bluetooth connections...")

      client_sock = None
      while client_sock == None:
        if not self.active:
          break
                
        try:
          client_sock, address = server_sock.accept()
        except:
          pass
        
      if client_sock == None:
        continue

      logger.info("[BluetoothServer] Accepted connection from " + str(address))
        
      t = BluetoothClientHandle(client_sock, self)
      t.start()

    server_sock.close()
    
    log("[BluetoothServer] Bye bye.")
    
    return True


class BluetoothClientHandle(threading.Thread, utils.NonBlockingThread):
  def __init__(self, socket, srv):
    self.socket = socket
    self.srv = srv
    self.queue = srv.queue
    threading.Thread.__init__ (self)
    
  def run (self):
    
    log("[BluetoothClientHandle] Taking connection")
    while True:
        
      if not self.srv.active:
        break
      
      log("[BluetoothClientHandle] waiting data...")
        
      try:
        buff = self.socket.recv(globals.MAX_MSG_SIZE)
        if not buff: break
      except bluetooth.BluetoothError, e:
        print "Error receiving: %s (%s)" % (e, self.socket)
        break

      log("[BluetoothClientHandle] got: " + buff)
      self.queue.addCommand(buff);
    
    logger.info("[BluetoothClientHandle] Connection finished")
      
    self.socket.close()
