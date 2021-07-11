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

if [ "$1" = "--compile-test" ]; then 
    if ./compile-test.sh with-eclipselink --tomee-test; then
        run    
    fi
else
    run
fi
