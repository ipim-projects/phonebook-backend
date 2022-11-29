# Backend-сервисы тестового задания № 1

#### Требования для установки

* JDK 1.8 
* Apache Maven 3.5.0
* Создать БД postgresql: CREATE DATABASE phonebook WITH OWNER = postgres  ENCODING = 'UTF8'

#### Быстрая настройка
HTTPS https://gitflic.ru/project/ipim/phonebook-backend.git

#### Глобальные настройки Git
git config --global user.name "user"
git config --global user.email "user@mail.com"

#### Создание нового репозитория
git clone https://gitflic.ru/project/ipim/phonebook-backend.git
cd phonebook-backend

#### Использовать существующую директорию
cd existing_folder
git init
git remote add origin https://gitflic.ru/project/ipim/phonebook-backend.git
git add .
git commit -m "Initial commit"
git push -u origin master

#### Запушить существующий репозиторий
cd existing_folder
git remote rename origin old-origin
git remote add origin https://gitflic.ru/project/ipim/phonebook-backend.git
git push -u origin --all
git push -u origin --tags


Запуск сборки:

```
#!shell
  mvn -U clean install
```


#### Запуск

```
#!shell

java -jar target\phonebook-backend-0.0.1-SNAPSHOT.jar
java -jar target\phonebook-backend-0.0.1-SNAPSHOT.jar  --spring.config.location=C:\dir\application.properties 

```

## Описание настроек application.properties проекта

 №  |  Имя настройки        | Значение по-умолчанию | Описание
----|-----------------------|-----------------------| ----------------------------------------------- 
1.  |  phonebook.defaultDir | null                  | Корневой каталог работы маршрута, по-умолчанию будет использован HOME для текущего пользователя
2.  |  phonebook.workDir    | work                  | Каталог входных файлов - для файлов с выгрузками
3.  |  phonebook.outputDir  | data                  | Каталог входных файлов
4.  |  phonebook.makeDirs   | true                  | Создавать каталоги при их отсутствии
5.  |  logging.file.name    | указать файл          | Имя лог-файла 
5.  |  debug                | true/false            | Требуется ли debug уровень логгирования

# Бэкэнд микросервисы на Spring Boot & Spring Cloud

#### Требования для установки

* JDK 1.8 
* Apache Maven 3.5.0


#### Быстрая настройка
HTTPS https://gitflic.ru/project/ipim/phonebook-backend.git

#### Глобальные настройки Git
git config --global user.name "user"
git config --global user.email "user@mail.com"

#### Создание нового репозитория
git clone https://gitflic.ru/project/ipim/phonebook-backend.git
cd phonebook-backend

#### Использовать существующую директорию
cd existing_folder
git init
git remote add origin https://gitflic.ru/project/ipim/phonebook-backend.git
git add .
git commit -m "Initial commit"
git push -u origin master

#### Запушить существующий репозиторий
cd existing_folder
git remote rename origin old-origin
git remote add origin https://gitflic.ru/project/ipim/phonebook-backend.git
git push -u origin --all
git push -u origin --tags


Запуск сборки:

```
#!shell
  mvn -U clean install
```


#### Запуск

```
#!shell

java -jar target/phonebook-backend-0.0.1-SNAPSHOT.jar

```

## Описание модулей проекта

  Модуль           | Описание
-------------------| ----------------------------------------------------------------
 phonebook-backend | Сервисы работы со справочниками Телефонная книга и Место работы
