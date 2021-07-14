#!/bin/bash

# Script to test this FDAF application example using Apache TomEE.
# You must have TomEE installation inside directory where the FDAF
# application `development-source` directory resides and in which
# this script also resided.
# TomEE application directory name must contain `tomee` word, e.g:
# `apache-tomee-plume-8.0.6`

reset

function run() {
    (
        test_run=
        while [ true ]; do
            test_run=$(ps aux | awk '/tail .*tomee.*/' | grep 'catalina.out')
            if [ "$test_run" != "" ]; then
                break;
            fi
            sleep 1
        done
        while [ true ];
        do
            test_run=$(ps aux | awk '/tail .*tomee.*/' | grep 'catalina.out')
            if [ "$test_run" = "" ]; then
                break
            fi
            sleep 1
        done
        sleep 1
        ./*tomee*/bin/shutdown.sh
        exit
    ) &

    rm -rfv *tomee*/temp/*
    rm -rfv *tomee*/logs/*
    rm -rfv *tomee*/works/*
    
    reset
    
    ./*tomee*/bin/startup.sh
    tail -f *tomee*/logs/catalina.out
}

CWD=$(pwd)
BASE=$(basename $CWD)

if [ "$1" = "--compile-test-multi-tiers" ]; then 
    if ./compile-test.sh with-eclipselink --tomee-test-multi-tiers; then
        run    
    fi
elif [ "$1" = "--compile-test-single-tier" ]; then 
    if ./compile-test.sh with-eclipselink-in-single-war --tomee-test-single-tier; then
        run    
    fi
elif [ "$1" = "--multi-tiers" ]; then
    WAR="compilable-source/$BASE-with-eclipselink/build/$BASE-webapp.war"
    EAR="compilable-source/$BASE-with-eclipselink/build/$BASE.ear"
    if [ -f "$WAR" ] && [ -f "$EAR" ]; then
        rm -rfv ./*tomee*/webapps/$BASE*
        cp -afv $WAR ./*tomee*/webapps/.
        cp -afv $EAR ./*tomee*/webapps/.
        run
    else
        echo "One of $BASE WAR archive or EAR archive cannot be found."
        echo "Try with option: --compile-test-multi-tiers"
    fi
elif [ "$1" = "--single-tier" ]; then
    WAR="compilable-source/$BASE-with-eclipselink-in-single-war/build/$BASE-webapp.war"
    echo $WAR
    if [ -f "$WAR" ]; then
        rm -rfv ./*tomee*/webapps/$BASE*
        cp -afv $WAR ./*tomee*/webapps/.
        run
    else
        echo "Not found: $WAR"
        echo "Try with option: --compile-test-single-tier"
    fi
else
    run
fi
