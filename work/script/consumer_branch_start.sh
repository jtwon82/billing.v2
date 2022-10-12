#!/bin/sh

nohup java -Xms2g -Xmx2g -XX:+UseG1GC -server -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Djava.rmi.server.hostname=192.168.2.78 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar /home/dreamsearch/branch/mobon-billing-consumer-branch-0.0.1-SNAPSHOT.jar >/dev/null 2>&1

