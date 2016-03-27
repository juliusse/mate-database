#!/bin/bash

java -Xms512M -Xmx1024M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar "sbtwrapper/sbt-launch.jar" "$@"
