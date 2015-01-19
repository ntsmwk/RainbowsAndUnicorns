#!/bin/bash

tests=$(find src/at/jku/cp/rau/tests -name "Test*.java")
junit="/usr/share/java/junit4.jar"
jc="javac -target 1.7 -source 1.7 -cp .:src:$junit"
j="java -cp .:src:$junit org.junit.runner.JUnitCore"

for test in $tests
do
    echo $test
    testclassname=$(echo $test | sed -e "s/src\///" -e "s/\.java//" -e "s/\//./g")

    $jc $test
    $j $testclassname
done


#java -cp .:/usr/share/java/junit4.jar org.junit.runner.JUnitCore
#
#cd your_test_directory_here
#find . -name "\*Test.java" \
#    | sed -e "s/\.java//" -e "s/\//./g" \
#    | xargs java org.junit.runner.JUnitCore
