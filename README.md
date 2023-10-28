
# Secured-rest
## Цель: 
Выполнить аутентификацию с помощью Keycloak (+ Spring security + webflux)

Использовал 

статью: https://developers.redhat.com/articles/2023/07/24/how-integrate-spring-boot-3-spring-security-and-keycloak#install_keycloak

Код: https://github.com/edwin/spring-3-keycloak/tree/master

голову и упрямство
### Локальный запуск:
Скачиваю и запускаю контейнер Keycloak:
```
docker run -p 8080:8080 \
-e KEYCLOAK_ADMIN=admin \
-e KEYCLOAK_ADMIN_PASSWORD=password \
keycloak/keycloak:17.0.0  start-dev
```

Создаю пользователя:

1. Открыть http://localhost:8080/ => Administration Console
   Имя пользователя: admin
   Пароль: password

2. создаю realm с именем external
3. создаю client с именем external-client.
```
   Client ID: external-client
   Enabled: On
   Client Protocol: openid-connect
   Access type: Confidential
   Standard flow enabled: On
   Direct access grants enabled: On
   Valid redirects URI: http://localhost:8081/*
```

После создания клиента, нужно во вкладке credentials скопировать secret и сохранить в конфиг приложения
```
spring.security.oauth2.client.registration.external.client-secret=ваш_secret
```

4. создаю user c
   Username: someusername
   Email: someemail@mail.com
   First Name: somefirstname
   Last Name: somelastname
   В созданом пользователе во вкладке credentials создать пароль
   В созданом пользователе во вкладке Role Mappings добавить все роли (думаю что нет нужды добавлять всех, но в этом нужно было разбираться)

Запускаю приложение.
Открываю в браузере:
```
http://localhost:8081/
```
Ввожу имя пользователя и пароль и вижу свои данные

TODO: починить http://localhost:8081/unauthenticated эта ручка не должна вести на аутентификацию, а ведет



