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
Создаем БД:
```bash
docker exec -it some-postgres psql -U postgres -c "CREATE DATABASE 1st_task;"
```
Клонируем этот репозиторий (или скачиваем .zip)
```bash
git glone https://github.com/a1pha-dev/1st_task
```
Запускаем `Main.main()`
