#!/bin/bash
set -e
rm -rf out
mkdir -p out
javac -d out $(find src -name "*.java")
echo "Compiled."
java -cp out app.Main
