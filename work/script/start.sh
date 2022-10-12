#!/bin/sh

nohup java -Xms5g -Xmx5g -XX:+UseG1GC -server -jar $1 >/dev/null 2>&1
 
