# oidc-example

証明書のmyserverの証明書を取り出して、myclientに信頼できる証明書として登録する。

## セットアップ

```bash
# 鍵ストアを作るためサーバーを作成して起動
mvn -f myclient/pom.xml clean package liberty:create liberty:install-feature liberty:start liberty:stop
mvn -f myserver/pom.xml clean package liberty:create liberty:install-feature liberty:start liberty:stop

# 停止されているか確認
ps -ef | grep defaultServer

# myserverの秘密鍵と証明書を作成して置き換える
openssl genrsa 2048 > default.key
# 証明書を作成する. common nameに定義した内容をhostsに登録しておくこと
openssl req -new -key default.key -subj "/C=JP/ST=Tokyo/OU=example/CN=myserver" > default.csr
openssl x509 -days 3650 -sha256 -req -signkey default.key < default.csr > default.crt
# パスワードはserver.xmlに記載したPasswordにそろえる
openssl pkcs12 -export -in default.crt -inkey default.key -out key.p12 -name default -password pass:Password

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



## TODO 

各エンドポイントをどうたたいているか可視化する
https://openliberty.io/docs/latest/reference/config/openidConnectClient.html
https://www.ibm.com/docs/en/sva/9.0.2?topic=SSPREK_9.0.2/com.ibm.isam.doc/config/concept/OAuthEndpoints.html
https://www.scottbrady91.com/openid-connect/openid-connect-endpoints
https://connect2id.com/products/server/docs/api
https://developer.okta.com/docs/reference/api/oidc/#endpoints


### 認可エンドポイント

```
authorizationEndpointUrl="https://localhost:9443/oidc/endpoint/OP/authorize"
```
https://www.ibm.com/docs/ja/was-liberty/nd?topic=liberty-invoking-authorization-endpoint-openid-connect
https://server.example.com:443/oidc/endpoint/<provider_name>/authorize

https://localhost:9443/oidc/endpoint/OP/authorize



### トークン・エンドポイント

```
tokenEndpointUrl="https://localhost:9443/oidc/endpoint/OP/token"
```
https://www.ibm.com/docs/ja/was-liberty/nd?topic=liberty-invoking-token-endpoint-openid-connect
https://server.example.com:443/oidc/endpoint/<provider_name>/token

### ログアウト・エンドポイント

logoutRedirectURL
https://www.ibm.com/docs/ja/was-liberty/nd?topic=liberty-invoking-logout-endpoint-openid-connect
https://server.example.com:443/oidc/endpoint/<provider_name>/logout

https://localhost:9443/oidc/endpoint/OP/logout


### セッション管理エンドポイント

https://www.ibm.com/docs/ja/was-liberty/nd?topic=liberty-invoking-session-management-endpoint-openid-connect
https://server.example.com:443/oidc/endpoint/<provider_name>/check_session_iframe

### カバレッジ・マップ・エンドポイント

https://www.ibm.com/docs/ja/was-liberty/nd?topic=liberty-invoking-coverage-map-service
https://server.example.com:443/oidc/endpoint/<provider_name>/coverage_map

### UserInfoエンドポイント

https://docs.verify.ibm.com/verify/reference/handleuserinfopost-4
https://www.ibm.com/docs/ja/was-liberty/nd?topic=liberty-invoking-userinfo-endpoint-openid-connect
https://server.example.com:443/oidc/endpoint/<provider_name>/userinfo

https://localhost:9443/oidc/endpoint/OP/userinfo


### イントロスペクション・エンドポイント

https://www.ibm.com/docs/ja/was-liberty/nd?topic=liberty-invoking-introspection-endpoint-openid-connect
https://server.example.com:443/oidc/endpoint/<provider_name>/introspect


### Discoveryエンドポイント

discoveryEndpointUrl

### JWKエンドポイント

jwkEndpointUrl

### トークン取り消しエンドポイント

https://qiita.com/TakahikoKawasaki/items/185d34814eb9f7ac7ef3#5-%E3%83%88%E3%83%BC%E3%82%AF%E3%83%B3%E5%8F%96%E3%82%8A%E6%B6%88%E3%81%97-rfc-7009


## Keycloakを利用する

こちらをコピーして利用する。
https://github.com/keycloak/keycloak-containers/blob/main/docker-compose-examples/keycloak-postgres.yml

```bash
docker-compose -f keycloak-postgres.yml up
```
