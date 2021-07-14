#!/bin/bash

reset

CWD=$(pwd)
BASE=$(basename $CWD)
cd development-source/io.sourceforge.fdaf/fdaf

if mvn clean compile -P initialize-compilable; then
    cd $CWD
    cd compilable-source/"$BASE-$1"
    if mvn; then
        if [ "$1" != "thorntail" ]; then
            cd $CWD
            cd deployment-test
            mvn -Dcode_name="$BASE" -Dvariant="$1"
            cd $CWD
        else
            if [ -f "build/$BASE-$1.jar" ]; then
                cp -afv build/$BASE-$1.jar ../../$BASE-$1.jar
            fi
        fi
    else
        cd $CWD
        exit 1
    fi
else
    cd $CWD
    exit 1
fi

cd $CWD

date > last-update-ts

if [ "$2" = "--tomee-test-multi-tiers" ]; then
    rm -rfv *tomee*/webapps/fdaf*
    cp -afv compilable-source/$BASE-with-eclipselink/build/$BASE.ear *tomee*/webapps
    cp -afv compilable-source/$BASE-with-eclipselink/build/$BASE-webapp.war *tomee*/webapps
fi

if [ "$2" = "--tomee-test-single-tier" ]; then
    rm -rfv *tomee*/webapps/fdaf*
    cp -afv compilable-source/$BASE-with-eclipselink-in-single-war/build/$BASE-webapp.war *tomee*/webapps
fi

exit 0
