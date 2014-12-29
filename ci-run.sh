set -e
set -x

#export display to run firefox headless
#export DISPLAY=:99

# remove files form last build
rm -rf jenkins/builds/*
rm -rf jenkins/jacoco/*
mkdir -p jenkins/builds
mkdir -p jenkins/jacoco

# build
dpkg-buildpackage -uc -us -b

#move debian package
mv ../mate-db_*.deb jenkins/builds/

# move mate-db files
mv application/target/universal/*.zip jenkins/builds/
#mv application/target/scala-2.*/jacoco/jacoco.exec jenkins/jacoco/frontend.exec

# remove MVC-classes that should be ignored
rm -rf application/target/*/classes/system
rm -rf application/target/*/classes/Routes*.class
rm -rf application/target/*/classes/info/seltenheim/mate/views
rm -rf application/target/*/classes/info/seltenheim/mate/controllers/javascript
rm -rf application/target/*/classes/info/seltenheim/mate/controllers/ref
rm -rf application/target/*/classes/info/seltenheim/mate/controllers/routes*
rm -rf application/target/*/classes/info/seltenheim/mate/controllers/Reverse*.class
rm -rf application/target/*/classes/info/seltenheim/mate/controllers/system/javascript
rm -rf application/target/*/classes/info/seltenheim/mate/controllers/system/ref
rm -rf application/target/*/classes/info/seltenheim/mate/controllers/system/routes*
rm -rf application/target/*/classes/info/seltenheim/mate/controllers/system/Reverse*.class