#!/bin/sh

JAR_FILE=/d/.m2/repository/hu/jinfeng/sync-file/1.0-SNAPSHOT/sync-file-1.0-SNAPSHOT.jar

#JDK所在路径
JAVA_HOME="/d/java"
#JAVA_HOME="/usr/local/java"

#jvm虚拟机启动参数
JAVA_OPTS="-server -Xms128M -Xmx1G -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"
#JAVA_OPTS="-server -Xms1G -Xmx1G -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"
#初始化psid变量（全局）
psid=0
checkpid() {
    javaps=`$JAVA_HOME/bin/jps -l | grep $JAR_FILE`
    if [ -n "$javaps" ]; then
        psid=`echo $javaps | awk '{print $1}'`
    else
        psid=0
    fi
}

start() {
    checkpid
    if [ $psid -ne 0 ]; then
        echo "================================"
        echo "warn: $JAR_FILE already started! (pid=$psid)"
        echo "================================"
        else
        echo -n "Starting ..."
        nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar $JAR_FILE --spring.profiles.active=prod >/dev/null 2>&1 &
        
        checkpid
        if [ $psid -ne 0 ]; then
            echo "(pid=$psid) [OK]"
        else
            echo "[Failed]"
        fi
    fi
}

stop() {
    checkpid
    if [ $psid -ne 0 ]; then
        echo -n "Stopping $JAR_FILE ...(pid=$psid) "
        kill -9 $psid
    if [ $? -eq 0 ]; then
        echo "[OK]"
    else
        echo "[Failed]"
    fi
    checkpid
    if [ $psid -ne 0 ]; then
        stop
    fi
    else
        echo "================================"
        echo "warn: $JAR_FILE is not running"
        echo "================================"
    fi
}

status() {
    checkpid
    if [ $psid -ne 0 ]; then
    echo "$JAR_FILE is running! (pid=$psid)"
    else
    echo "$JAR_FILE is not running"
    fi
}

info() {
    echo "System Information:"
    echo "****************************"
    echo `head -n 1 /etc/issue`
    echo `uname -a`
    echo
    echo "JAVA_HOME=$JAVA_HOME"
    echo `$JAVA_HOME/bin/java -version`
    echo
    echo "JAR_FILE=$JAR_FILE"
    echo "****************************"
}

case "$1" in
'start')
start
;;
'stop')
stop
;;
'restart')
stop
start
;;
'status')
status
;;
'info')
info
;;
*)
echo "Usage: $0 {start|stop|restart|status|info}"
exit 1
esac;

