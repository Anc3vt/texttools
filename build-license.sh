#!/bin/bash
mvn clean install -Plicense && cp target/*dep* build/license.jar

