@echo off

if "%1"=="h" goto begin

start mshta vbscript:createobject("wscript.shell").run("""%~nx0"" h",0)(window.close)&&exit

:begin

java -server -Xms128M -Xmx1G -jar D:\.m2\repository\hu\jinfeng\sync-file\1.0-SNAPSHOT\sync-file-1.0-SNAPSHOT.jar server adblocation=D:\syncfile
pause