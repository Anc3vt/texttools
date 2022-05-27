#!/bin/bash
mvn clean install -Plicense && target/*dep* build/license.jar

