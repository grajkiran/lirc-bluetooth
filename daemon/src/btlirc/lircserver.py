#!/usr/bin/python

from globals import log, app, logger
from time import sleep
import os
import platform
import socket
import sys
import threading
import utils

mutex = threading.Lock()

class LircServer(threading.Thread, utils.NonBlockingThread):
  use_unix_socket = False
  clients = []
  lirc_file = None
  
  def __init__(self, mainapp):
    self.lirc_file = app.lirc_file
    self.mainapp = mainapp
    self.clients = []
    
    if platform.system() == 'Linux':
        self.use_unix_socket = True
        
    threading.Thread.__init__ (self)
  
  def run(self):
    port = None
    if self.use_unix_socket:
        socket_family = socket.AF_UNIX
        
        if self.lirc_file is not None:
            port = self.lirc_file
    else:
        socket_family = socket.AF_INET
        port = (socket.gethostname(), app.lirc_port)

    s = socket.socket(socket_family, socket.SOCK_STREAM)
    
    log("[LircServer] Using port " + str(port))

    if self.use_unix_socket:
        if os.access(port, os.F_OK):
            try:
              os.remove(port)
            except OSError:
              self.mainapp.die("[LircServer] Can't delete file: " + app.lirc_file)
              return

    logger.info("[LircServer] LIRC Server Listening on " + str(port))

    try:
        s.bind(port)
        if self.use_unix_socket:
            os.chmod(port, 0666)
    except Exception, e:
        self.mainapp.die("[LircServer] Can't bind socket: " + str(e[1]))
        return
        
    s.listen(15)
    s.settimeout(2)
    
    while True:
        if not self.active:
            break
            
        log("[LircServer] Waiting for connections...")
        
        conn = None
        while conn == None:
            if not self.active:
                break
                
            try:
              conn, addr = s.accept()
            except socket.timeout, err:
              pass
        
        if conn == None:
            continue
            
        logger.info("[LircServer] Connection received..." + str(addr) + " - " + str(conn)) ;

        self.handleClient(conn)
    
    s.close()
  
  def handleClient(self, socket):
    mutex.acquire()
    
    log("[LircServer] Adding client")
    
    self.clients.append(LircClient(socket))

    mutex.release()
      
  def sendCommand(self, cmd):
    mutex.acquire()

    if len(self.clients) > 0:

      log("[LircServer] Received command " + cmd + " a " + str(len(self.clients)) + " clients")
      
      liveclients = []
      
      for c in self.clients:
        if c.sendCommand(cmd + "\n"):
          liveclients.append(c)
      
      self.clients = liveclients
        
    else:
      log("[LircServer] No clients connected, dropping command!")
        
    mutex.release()

class LircClient:
  sock = None

  def __init__(self, sock):
    self.sock = sock

  def isConnected(self):
    return not self.sock is None

  def sendCommand(self, cmd):
    result = False
    if self.isConnected():
      try:
        self.sock.send(cmd) 
        result = True
      except socket.error:
        pass

    return result
