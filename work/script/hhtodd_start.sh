#!/bin/sh

nohup java -Xms4g -Xmx4g -XX:+UseG1GC -server -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8999 -Djava.rmi.server.hostname=192.168.2.79 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar /home/dreamsearch/mobon-billing-HHtoDD-0.0.1-SNAPSHOT.jar >/dev/null 2>&1

