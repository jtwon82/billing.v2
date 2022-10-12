#!/bin/sh

nohup java -Xms10g -Xmx10g -XX:+UseG1GC -server -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Djava.rmi.server.hostname=192.168.2.77 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar /home/dreamsearch/clickview/mobon-billing-consumer-0.0.1-SNAPSHOT.jar >/dev/null 2>&1
 
