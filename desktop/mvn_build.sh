#!/bin/bash

mvn -version
cd ../core
mvn clean install
cd ../desktop
mvn clean install

read -p "Press any key to resume ..." 