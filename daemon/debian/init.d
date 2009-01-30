#! /bin/sh
#### BEGIN INIT INFO
# Provides:          btlirc
# Required-Start:    bluetooth
# Required-Stop:     bluetooth
# Default-Start:     2 3 4 5
# Default-Stop:      S 0 1 6
# Short-Description: LIRC-alike daemon for Bluetooth remote control
# Description:       Debian init script for LIRC-alike daemon for
#                    Bluetooth remote control
### END INIT INFO
#
# Author:   Svilen Ivanov <svilen.ivanov@gmail.com>
#

PATH=/bin:/usr/bin:/sbin:/usr/sbin
DAEMON=/usr/share/btlirc/phonemote.py
LABEL="btlirc remote control daemon"
NAME="btlirc"

test -x $DAEMON || exit 0

. /lib/lsb/init-functions

if [ -f /etc/btlirc/btlirc.conf ] ; then
	. /etc/btlirc/btlirc.conf
fi

ARGS=""
if [ "x$LOG_FILE" != "x" ] ; then
   ARGS="$ARGS -L '$LOG_FILE'"
fi

if [ "x$SOCKET" != "x" ] ; then
   ARGS="$ARGS -o '$SOCKET'"
fi

if [ "x$DEBUG" != "x" ] ; then
   ARGS="$ARGS -v"
fi

PIDFILE=/var/run/btlirc.pid

case "$1" in
    start)
   log_daemon_msg "Starting $LABEL" "$NAME"
   start_daemon -p $PIDFILE $DAEMON $ARGS
   log_end_msg $?
    ;;
  stop)
   log_daemon_msg "Stopping $LABEL" "$NAME"
   killproc -p $PIDFILE
   sleep 3
   log_end_msg $?
    ;;
  force-reload|restart)
    $0 stop
    $0 start
    ;;
  *)
    echo "Usage: /etc/init.d/$NAME {start|stop|restart|force-reload}"
    exit 1
    ;;
esac

exit 0
