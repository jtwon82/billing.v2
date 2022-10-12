#!/bin/sh

PID="$(ps ax | grep java | grep -i mobon-billing-HHtoDD | grep -v grep | awk '{print $1}')"
if [[ "" !=  "$PID" ]]; then
  echo "killing $PID"
  kill -s TERM $PID
fi

