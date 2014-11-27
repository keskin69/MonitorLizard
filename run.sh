export JAVA_HOME=/usr/java7_64
cd /home/areca/keskin/monitor
$JAVA_HOME/bin/java -jar repgen.jar monitor.cfg $1 $2 >> ./log/monitor.log 2>> ./log/monitor.err
