Сервис для управления пользователями (CRUD) на Spring Boot.  
Обеспечивает создание, чтение, обновление и удаление пользователей с валидацией данных и версионированием схемы базы данных.

## Стек технологий
- Java 17
- Spring Boot 3.4.5
- Spring Data JPA (Hibernate)
- PostgreSQL 15
- Liquibase (миграции схемы)
- MapStruct (маппинг DTO ↔ Entity)
- Lombok
- SpringDoc OpenAPI (Swagger UI)
- Docker / Docker Compose
- JUnit 5, MockMvc, Testcontainers

## Функциональность
- ✅ Создание пользователя (POST `/api/v1/users`)
- ✅ Получение пользователя по ID (GET `/api/v1/users/{id}`)
- ✅ Обновление пользователя (PUT `/api/v1/users/{id}`)
- ✅ Удаление пользователя (DELETE `/api/v1/users/{id}`)
- ✅ Получение всех пользователей (GET `/api/v1/users`)
- ✅ Валидация входных данных (имя, email, возраст)
- ✅ Глобальная обработка исключений
- ✅ Документация API через Swagger UI
- ✅ Миграции БД через Liquibase

## Запуск приложения

### 1. Через Docker Compose (рекомендуемый способ)
В корне проекта выполните:
```bash
docker-compose up -d
Приложение стартует на порту 8081, база данных – на порту 5432.
После запуска Swagger UI доступен по адресу:
👉 http://localhost:8081/swagger-ui.html

2. Локальный запуск без Docker
2.1 Подготовить PostgreSQL
Установите и запустите PostgreSQL 15 локально. Создайте базу данных и пользователя:

sql
CREATE DATABASE userdb;
CREATE USER user_service WITH PASSWORD 'secret123';
GRANT ALL PRIVILEGES ON DATABASE userdb TO user_service;
2.2 Настроить подключение
Проверьте параметры в application.properties или задайте переменные окружения:

text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/userdb
SPRING_DATASOURCE_USERNAME=user_service
SPRING_DATASOURCE_PASSWORD=secret123
2.3 Запустить приложение
bash
mvn spring-boot:run
API Endpoints
Метод	URL	Описание
POST	/api/v1/users	Создать пользователя
GET	/api/v1/users/{id}	Получить пользователя по ID
PUT	/api/v1/users/{id}	Обновить пользователя
DELETE	/api/v1/users/{id}	Удалить пользователя
GET	/api/v1/users	Получить всех пользователей
Пример запроса (создание)
json
POST /api/v1/users
{
  "name": "Иван Петров",
  "email": "ivan@example.com",
  "age": 30
}
Пример ответа
json
{
  "id": 1,
  "name": "Иван Петров",
  "email": "ivan@example.com",
  "age": 30,
  "createdAt": "2026-05-10T12:00:00",
  "updatedAt": "2026-05-10T12:00:00"
}
Документация API (Swagger)
После запуска приложения откройте в браузере:
👉 http://localhost:8081/swagger-ui.html

Тестирование
Запустить все тесты:

bash
mvn test
Юнит‑тесты сервиса и маппера

Интеграционные тесты контроллера с Testcontainers (PostgreSQL)

Тесты глобального обработчика исключений

Миграции базы данных (Liquibase)
Файлы миграций находятся в src/main/resources/db/changelog.
При запуске Liquibase автоматически применяет новые изменения.
Схема создаётся с помощью SQL-скрипта:
v1.0__initial_schema.sql

Переменные окружения
Переменная	Описание	Значение по умолчанию
SPRING_DATASOURCE_URL	JDBC URL PostgreSQL	jdbc:postgresql://localhost:5432/userdb
SPRING_DATASOURCE_USERNAME	Имя пользователя БД	user_service
SPRING_DATASOURCE_PASSWORD	Пароль БД	secret123
SPRING_JPA_HIBERNATE_DDL_AUTO	Режим генерации схемы Hibernate	none (используется Liquibase)
SERVER_PORT	Порт приложения	8081
Структура проекта
text
src/
├── main/
│   ├── java/com/example/
│   │   ├── controller/      # REST контроллеры
│   │   ├── service/         # Бизнес-логика
│   │   ├── repository/      # Spring Data JPA репозитории
│   │   ├── entity/          # JPA сущности
│   │   ├── dto/             # Request / Response DTO
│   │   ├── mapper/          # MapStruct мапперы
│   │   ├── exception/       # Кастомные исключения и обработчик ошибок
│   │   └── config/          # Дополнительная конфигурация (OpenAPI)
│   └── resources/
│       ├── db/changelog/    # Liquibase миграции
│       ├── application.properties
│       └── ...
└── test/                    # Модульные и интеграционные тесты
Автор
Литвиненко Дмитрий
