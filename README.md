## Задание №1
### Быстрый запуск:
Поднимаем контейнер с PostgresSQL
```bash
docker run \
    --name postgresdb \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=secret \
    -e POSTGRES_DB=1st_task \
    -p 5432:5432 \
    -d postgres:16
```
Запуск приложения:
```bash
./gradlew run
```