@echo off
if exist out rmdir /s /q out
mkdir out
for /r %%f in (*.java) do javac -d out "%%f"
echo Compiled.
java -cp out app.Main
pause
