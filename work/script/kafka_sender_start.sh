#!/bin/sh

nohup java -Xms4g -Xmx4g -XX:+UseG1GC -server -jar /home/dreamsearch/mobon-billing-sender-0.0.1-SNAPSHOT.jar >/dev/null 2>&1

