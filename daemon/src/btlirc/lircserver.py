#!/usr/bin/python

from globals import log, app, mainapp
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
  
  def __init__(self, mainapp, lirc_file):
    self.lirc_file = lirc_file
    self.mainapp = mainapp
    self.clients = []
    
    if platform.system() == 'Linux':
        self.use_unix_socket = True
        
    threading.Thread.__init__ (self)
  
  def run(self):
    
    if self.use_unix_socket:
        socket_family = socket.AF_UNIX
        
        if self.lirc_file is not None:
            port = self.lirc_file
        else:
            port = app.lirc_file
    else:
        socket_family = socket.AF_INET
        port = (socket.gethostname(), app.lirc_port)

    s = socket.socket(socket_family, socket.SOCK_STREAM)
    
    if app.verbose:
        log("[LircServer] Using port " + str(port))

    if self.use_unix_socket:
        if os.access(app.lirc_file, os.F_OK):
            try:
              os.remove(app.lirc_file)
              print "ok"
            except OSError:
              self.mainapp.die("[LircServer] Can't delete file: " + app.lirc_file)
              return

    if app.verbose:
      log("[LircServer] LIRC Server Listening on " + str(port))

    try:
        s.bind(port)
    except Exception, e:
        self.mainapp.die("[LircServer] Can't bind socket: " + str(e[1]))
        return
        
    s.listen(15)
    s.settimeout(2)
    #s.setblocking(0)
    #if not os.access(app.lirc_file,  os.W_OK):
    
    while True:
        if not self.active:
            break
            
        if app.verbose:
            log("[LircServer] Waiting for connections...")
        
        conn = None
        while conn == None:
            if not self.active:
                break
                
            try:
                conn, addr = s.accept()
            except socket.timeout, err:
                if app.verbose == 2: print ".",
        
        if conn == None:
            continue
            
        if app.verbose:
            log("[LircServer] Connection received..." + str(addr) + " - " + str(conn)) ;

        self.handleClient(conn)
    
    s.close()
  
  def handleClient(self, socket):
    mutex.acquire()
    
    if app.verbose:
      log("[LircServer] Adding client")
    
    self.clients.append(LircClient(socket))

    mutex.release()
  

      
  def sendCommand(self, cmd):
    mutex.acquire()

    #Byte[] bytes = Encoding.ASCII.GetBytes(cmd+'\n');

    if len(self.clients) > 0:
      if app.verbose:
        log("[LircServer] Enviando comando " + cmd + " a " + str(len(self.clients)) + " clientes")
      
      liveclients = []
      
      for c in self.clients:
        if c.sendCommand(cmd + "\n"):
          liveclients.append(c)
      
      self.clients = liveclients
        
    else:
      if app.verbose:
        log("[LircServer] No clients connected, dropping command!")
        
    mutex.release()



class LircClient:
  sock = None
  #connected = False

  def __init__(self, sock):
    self.sock = sock
    #self.connected = sock.Connected;

  def isConnected(self):
    return not self.sock is None

  def sendCommand(self, cmd):
    result = False
    #this.connected = sock.Connected;
    if self.isConnected():
      try:
        self.sock.send(cmd) #,cmd.Length,SocketFlags.None);
        result = True
      except socket.error:
        pass

    return result

