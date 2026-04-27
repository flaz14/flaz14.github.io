#!/usr/bin/env bash


cd ../my-tracer
mvn clean install
cd ../demo

compile_classpath='../my-tracer/loader/target/loader.jar:.'
runtime_classpath="/usr/local/soft/jdk1.8.0_111/lib/tools.jar:$compile_classpath"

echo "============"
javac -version
java  -version
echo "============"

javac -cp "$compile_classpath" CallViaInterface.java && \
java  -cp "$runtime_classpath" CallViaInterface