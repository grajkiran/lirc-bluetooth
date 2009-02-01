# file: rfcomm-server.py
# auth: Albert Huang <albert@csail.mit.edu>
# desc: simple demonstration of a server application that uses RFCOMM sockets
#
# $Id: rfcomm-server.py 518 2007-08-10 07:20:07Z albert $

#from bluetooth import *

from ctypes import *
import time
import os
import sys
import Xlib
from time import sleep


Xtst = CDLL("libXtst.so.6")
Xlib1 = CDLL("libX11.so.6")
dpy = Xtst.XOpenDisplay(None)


def SendInput( txt ):
    for c in txt:
        #sym = Xlib.XStringToKeysym(c)
        code = Xlib1.XKeysymToKeycode(dpy, 0x1008FF11)
        Xtst.XTestFakeKeyEvent(dpy, code, True, 0)
        Xtst.XTestFakeKeyEvent(dpy, code, False, 0)
        Xlib1.XFlush(dpy)
        sleep(1)

def SendKeyPress(key):
    sym = Xlib.XStringToKeysym(str(key))
    code = Xlib.XKeysymToKeycode(dpy, sym)
    Xtst.XTestFakeKeyEvent(dpy, code, True, 0)
    Xlib.XFlush(dpy)

def SendKeyRelease(key):
    sym = Xlib.XStringToKeysym(str(key))
    code = Xlib.XKeysymToKeycode(dpy, sym)
    Xtst.XTestFakeKeyEvent(dpy, code, False, 0)
    Xlib.XFlush(dpy)

SendInput("aaaassssssssssssssssssssss")

#endInput("aa")

#server_sock = BluetoothSocket(RFCOMM)
#server_sock.bind(("", PORT_ANY))
#server_sock.listen(1)
#
#port = server_sock.getsockname()[1]
#
#uuid = "084a08da-e722-11dd-9bf8-000fb0c7d780"
#
#advertise_service(server_sock, "SampleServer",
#                   service_id=uuid,
#                   service_classes=[ uuid, SERIAL_PORT_CLASS ],
#                   profiles=[ SERIAL_PORT_PROFILE ],
##                   protocols = [ OBEX_UUID ] 
#                    )
#                   
#print "Waiting for connection on RFCOMM channel %d" % port
#
#client_sock, client_info = server_sock.accept()
#print "Accepted connection from ", client_info
#
#try:
#    while True:
#        data = client_sock.recv(1024)
#        if len(data) == 0: break
#        print "received [%s]" % data
#except IOError:
#    pass
#
#print "disconnected"
#
#client_sock.close()
#server_sock.close()
#print "all done"
