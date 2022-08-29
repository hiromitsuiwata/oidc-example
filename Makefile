setup:
	mvn -f myclient/pom.xml clean package liberty:create liberty:install-feature

start:
	# mvn -f myserver/pom.xml liberty:start
	mvn -f myclient/pom.xml liberty:start

stop:
	#3mvn -f myserver/pom.xml liberty:stop
	mvn -f myclient/pom.xml liberty:stop

clean: stop
	# mvn -f myserver/pom.xml clean
	mvn -f myclient/pom.xml clean
	rm -f default.*
	rm -f key.p12

