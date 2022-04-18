#!/bin/bash

reset

CWD=$(pwd)
BASE=$(basename $CWD)
cd development-source/io.sourceforge.fdaf/fdaf

TOMEE_TEST_MULTI_PACKS=0
TOMEE_SINGLE_PACK=0
JAVADOC=0
PORT=

for arg in $@; do
    case $arg in
        --port=*|-p=*)
            PORT="${arg#*=}"
            ;;
        "--tomee-test-multi-packs")
            TOMEE_TEST_MULTI_PACKS=1
            ;;
        "--tomee-test-single-pack")
            TOMEE_SINGLE_PACK=1
            ;;
        "--javadoc")
            JAVADOC=1
            ;;
        *)
            exit
            ;;
    esac
done

PORT_SRC_DIR="compilable-source/$BASE-$PORT"

if mvn clean compile -P initialize-compilable; then
    cd $CWD
    if [ ! -d "$PORT_SRC_DIR" ]; then
        echo "Port '$PORT' doesn't exist."
        exit 1
    fi
    cd $PORT_SRC_DIR
    if mvn; then
        if [ "$PORT" != "thorntail" ]; then
            cd $CWD
            cd deployment-test
            mvn -Dcode_name="$BASE" -Dvariant="$PORT"
            cd $CWD
        else
            if [ -f "build/$BASE-$1.jar" ]; then
                cp -afv build/$BASE-$PORT.jar ../../$BASE-$PORT.jar
            fi
            cd $CWD
        fi
        if [ "$JAVADOC" = "1" ]; then
            cd $PORT_SRC_DIR
            if mvn javadoc:aggregate; then
                cd $CWD
                rm -rf API-documentations
                cp -af /tmp/io.sourceforge.fdaf/$BASE-$PORT/main/site/apidocs API-documentations
            else
                cd $CWD
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

if [ "$TOMEE_TEST_MULTI_PACKS" = "1" ]; then
    rm -rfv *tomee*/webapps/fdaf*
    cp -afv compilable-source/$BASE-with-eclipselink/build/$BASE.ear *tomee*/webapps
    cp -afv compilable-source/$BASE-with-eclipselink/build/$BASE-webapp.war *tomee*/webapps
fi

if [ "$TOMEE_SINGLE_PACK" = "1" ]; then
    rm -rfv *tomee*/webapps/fdaf*
    cp -afv compilable-source/$BASE-with-eclipselink-in-single-war/build/$BASE-webapp.war *tomee*/webapps
fi

exit 0
