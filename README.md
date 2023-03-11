# URL Shortener

This is a sample application that explores implementation of url shortening similar to [bit.ly](https://bitly.com/).

## Requirements
- Docker
- Java 17
- Angular 15
- Postgres
## Using docker
```shell
docker-compose up
```
Application can be accessed via ``http://localhost:10000/``
## How to run without docker
### Running Backend
```shell
# environment variables required to connect to database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=url-shortener
DB_USERNAME=postgres
DB_PASSWORD=password
```
At the root of the project run below command.
```shell
# starting application
./gradlew clean bootRun
```
### Running web application
Navigate to the ``frontend`` subfolder.
```shell
# install dependency
npm install
#executing application
npm run start
```
Application can be accessed via ``http://localhost:4200``