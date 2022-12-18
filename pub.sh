#!/bin/bash

rm -r pub

./gradlew publish

scp -r pub/* ubuntu@wandhoven.ddns.net:/media/B/html/maven/
