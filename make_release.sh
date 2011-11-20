#!/bin/bash

if [ "x$1" == "x" ]; then
    echo "Specify release version number:"
    echo "   $0 <version>"
    exit 1;
fi;

VERSION="$1"

mkdir -p releases/$VERSION
rm -f releases/$VERSION/*
cp -v *.deb releases/$VERSION
cp -v midlet/deployed/LircBT.ja? releases/$VERSION

echo "Done"
# test
