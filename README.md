# oidc-example

証明書のmyserverの証明書を取り出して、myclientに信頼できる証明書として登録する。

```bash
# 鍵ストアを作るためサーバーを作成して起動
mvn -f myclient/pom.xml clean package liberty:create liberty:install-feature liberty:start liberty:stop
mvn -f myserver/pom.xml clean package liberty:create liberty:install-feature liberty:start liberty:stop

#ls myclient/target/liberty/wlp/usr/servers/defaultServer/resources/security/key.p12
#ls myserver/target/liberty/wlp/usr/servers/defaultServer/resources/security/key.p12

# 停止されているか確認
ps -ef | grep defaultServer

# myserverの秘密鍵と証明書を作成して置き換える
openssl genrsa 2048 > default.key
# 証明書を作成する. common nameに定義した内容をhostsに登録しておくこと
openssl req -new -key default.key -subj "/C=JP/ST=Tokyo/OU=example/CN=myserver" > default.csr
openssl x509 -days 3650 -sha256 -req -signkey default.key < default.csr > default.crt
# パスワードはserver.xmlに記載したPasswordにそろえる
openssl pkcs12 -export -in default.crt -inkey default.key -out key.p12 -name default

cp key.p12 myserver/target/liberty/wlp/usr/servers/defaultServer/resources/security/
keytool -importcert -trustcacerts -keystore myclient/target/liberty/wlp/usr/servers/defaultServer/resources/security/key.p12 -storepass Password -alias myserver -file default.crt -noprompt
rm -f default.*

# myclientのほうの鍵ストアの内容を参照する
keytool -list -v -keystore myclient/target/liberty/wlp/usr/servers/defaultServer/resources/security/key.p12 -storepass Password

# myserver, myclientを起動
mvn -f myserver/pom.xml liberty:start
mvn -f myclient/pom.xml liberty:start

# 停止
mvn -f myserver/pom.xml liberty:stop
mvn -f myclient/pom.xml liberty:stop
```

ブラウザからアクセス
http://localhost:9081/myclient/api/properties



