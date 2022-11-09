# quiz-api
Functional quiz api using http4s, circe and doobie

## Starting Postgres docker container
```
$ cd docker
$ docker compose up -d
```

## Running the application

## API Examples:

### Categories API
Get all categories
```
$ curl --request GET "http://localhost:8000/categories"
```
Get category by id
```
$ curl --request GET "http://localhost:8000/categories/$id"
```
Add new category
```
$ curl --header "Content-Type: application/json" --request POST --data '{"name": "Test Category"}' http://localhost:8000/categories
```
Delete category
```
$ curl --request DELETE "http://localhost:8000/categories/$id"
```
