> sample project 빌드하고 데몬띄우기

1. resource/table.txt
2. clean package -P local-sample
3. Biling-Consumer-02 : /home/dreamsearch/logs/log4j/logZip/<br>
    192.168.2.76 : dreamsearch / dreamsearch2017!

4. cat start.sh <br>
#!/bin/sh<br>
for j in $(ls *.jar -ltr | tail -1 | awk '{print $8}')<br>
do<br>
 nohup java -Xms8g -Xmx8g -XX:+UseG1GC -server -jar $j >/dev/null 2>&1<br>
done<br>

5. cat stop.sh <br>
#!/bin/sh<br>
PID="$(ps ax | grep java | grep -i $1 | grep -v grep | awk '{print $1}')"<br>
if [[ "" !=  "$PID" ]]; then<br>
  echo "killing $PID"<br>
  kill -s TERM $PID<br>
fi<br>

