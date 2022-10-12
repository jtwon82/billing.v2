#!/bin/sh

PID="$(ps ax | grep java | grep -i mobon-billing-consumer-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{print $1}')"
if [[ "" !=  "$PID" ]]; then
  echo "killing $PID"
  kill -s TERM $PID
fi

