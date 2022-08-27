default.crt:
	openssl genrsa 2048 > default.key
	openssl req -new -key default.key -subj "/C=JP/ST=Tokyo/OU=example/CN=myserver" > default.csr
	openssl x509 -days 3650 -sha256 -req -signkey default.key < default.csr > default.crt

key.p12: default.crt
	openssl pkcs12 -export -in default.crt -inkey default.key -out key.p12 -name default -password pass:Password

myserver/target/liberty/wlp/usr/servers/defaultServer/resources/security/key.p12: key.p12
	mvn -f myserver/pom.xml clean package liberty:create liberty:install-feature
	mkdir -p myserver/target/liberty/wlp/usr/servers/defaultServer/resources/security
	cp key.p12 myserver/target/liberty/wlp/usr/servers/defaultServer/resources/security

myclient/target/liberty/wlp/usr/servers/defaultServer/resources/security/key.p12: default.crt
	mvn -f myclient/pom.xml clean package liberty:create liberty:install-feature liberty:start liberty:stop
	keytool -importcert -trustcacerts -keystore myclient/target/liberty/wlp/usr/servers/defaultServer/resources/security/key.p12 -storepass Password -alias myserver -file default.crt -noprompt

setup: myserver/target/liberty/wlp/usr/servers/defaultServer/resources/security/key.p12 myclient/target/liberty/wlp/usr/servers/defaultServer/resources/security/key.p12

start:
	mvn -f myserver/pom.xml liberty:start
	mvn -f myclient/pom.xml liberty:start

stop:
	mvn -f myserver/pom.xml liberty:stop
	mvn -f myclient/pom.xml liberty:stop

clean: stop
	mvn -f myserver/pom.xml clean
	mvn -f myclient/pom.xml clean
	rm -f default.*
	rm -f key.p12

