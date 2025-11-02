# Spring Boot JDBC Messages Application

REST API приложение для управления сообщениями с использованием Spring Boot и JDBC. Поддерживает работу с несколькими базами данных (PostgreSQL, MySQL, H2).

## 📋 Содержание

- [Технологии](#технологии)
- [Требования](#требования)
- [Установка и настройка](#установка-и-настройка)
- [Запуск приложения](#запуск-приложения)
- [API Endpoints](#api-endpoints)
- [Работа с базами данных](#работа-с-базами-данных)
- [Тестирование](#тестирование)
- [Структура проекта](#структура-проекта)

## 🛠 Технологии

- **Java 17**
- **Spring Boot 3.2.4**
- **Spring JDBC** (JdbcTemplate)
- **Gradle 8.5**
- **H2 Database** (по умолчанию, in-memory)
- **PostgreSQL** (опционально)
- **MySQL** (опционально)

## 📦 Требования

- **JDK 17** или выше
- **Gradle 8.5** или выше (можно использовать встроенный wrapper)
- **PostgreSQL 12+** (опционально, для профиля postgresql)
- **MySQL 8+** (опционально, для профиля mysql)

## 🔧 Установка и настройка

### 1. Клонирование репозитория

```bash
git clone https://github.com/TashaNovik/Java_mitp_hw_5_JDBC.git
cd Java_mitp_hw_5_JDBC
```

### 2. Быстрый старт с H2 (база данных по умолчанию)

Запустите:
```bash
# Windows
.\gradlew.bat bootRun

# macOS/Linux
./gradlew bootRun
```

Приложение будет доступно по адресу: **http://localhost:8080**

**H2 Console** (для просмотра данных): **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### 3. Настройка PostgreSQL (опционально)

#### Установка PostgreSQL:

**Windows:**
```bash
# Скачайте и установите с официального сайта
# https://www.postgresql.org/download/windows/
```

**macOS (Homebrew):**
```bash
brew install postgresql@16
brew services start postgresql@16
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
```

#### Создание базы данных:

```bash
# Войти в PostgreSQL
psql -U postgres

# Создать базу данных
CREATE DATABASE messages_db;

# Установить пароль для пользователя postgres (если требуется)
ALTER USER postgres WITH PASSWORD 'password';

# Выйти
\q
```

#### Альтернатива: Docker

```bash
docker run -d --name postgres-messages -p 5432:5432 -e POSTGRES_DB=messages_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -v postgres-data:/var/lib/postgresql/data postgres:15
```

### 4. Проверка настроек подключения для PostgreSQL

Файл `src/main/resources/application-postgresql.properties` содержит настройки подключения:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/messages_db
spring.datasource.username=postgres
spring.datasource.password=password
```

Если ваши настройки отличаются, измените их в этом файле.

## 🚀 Запуск приложения

### Запуск с H2 (по умолчанию) - рекомендуется для начала

```bash
# Windows
.\gradlew.bat bootRun

# macOS/Linux
./gradlew bootRun
```

**H2 Console доступна по адресу:** http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### Запуск с PostgreSQL

```bash
# Windows
.\gradlew.bat bootRun --args="--spring.profiles.active=postgresql"

# macOS/Linux
./gradlew bootRun --args='--spring.profiles.active=postgresql'
```

### Запуск с MySQL

```bash
# Windows
.\gradlew.bat bootRun --args="--spring.profiles.active=mysql"

# macOS/Linux
./gradlew bootRun --args='--spring.profiles.active=mysql'
```

### Что ожидать при запуске

После успешного запуска вы увидите в консоли:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.4)

...
2025-11-02 ... : Started DemoApplication in 2.345 seconds
```

Приложение будет доступно по адресу: **http://localhost:8080**

## 🌐 API Endpoints

Базовый путь: `/api`

### 1. Получить список сообщений

**GET** `/api/messages`

Возвращает последние 30 сообщений, отсортированные по времени создания (новые первыми).

**Пример запроса:**
```bash
curl http://localhost:8080/api/messages
```

**Пример ответа:**
```json
[
  {
    "id": 2,
    "content": "Hello from PostgreSQL!",
    "author": "Admin",
    "createdAt": "2025-11-02T12:30:45"
  },
  {
    "id": 1,
    "content": "Welcome to Messages API",
    "author": "System",
    "createdAt": "2025-11-02T12:30:44"
  }
]
```

### 2. Получить сообщение по ID

**GET** `/api/messages/{id}`

**Пример запроса:**
```bash
curl http://localhost:8080/api/messages/1
```

**Пример ответа:**
```json
{
  "id": 1,
  "content": "Welcome to Messages API",
  "author": "System",
  "createdAt": "2025-11-02T12:30:44"
}
```

**Ошибка (404):**
```json
{
  "timestamp": "2025-11-02T12:35:00",
  "status": 404,
  "error": "Not Found",
  "message": "Message not found with id: 999",
  "path": "/api/messages/999"
}
```

### 3. Создать новое сообщение

**POST** `/api/messages`

**Пример запроса:**
```bash
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{
    "content": "My new message",
    "author": "TestUser"
  }'
```

**Пример ответа (201 Created):**
```json
{
  "id": 3,
  "content": "My new message",
  "author": "TestUser",
  "createdAt": "2025-11-02T12:40:15"
}
```

Если `author` не указан, используется значение "Anonymous".

### 4. Пагинация сообщений

**GET** `/api/messages?page={page}&size={size}`

**Параметры:**
- `page` - номер страницы (начинается с 0)
- `size` - количество сообщений на странице

**Пример запроса:**
```bash
curl "http://localhost:8080/api/messages?page=0&size=10"
```

### 5. Поиск сообщений по автору

**GET** `/api/messages/search?author={username}`

**Пример запроса:**
```bash
curl "http://localhost:8080/api/messages/search?author=Admin"
```

### 6. Массовое создание сообщений

**POST** `/api/messages/batch`

**Пример запроса:**
```bash
curl -X POST http://localhost:8080/api/messages/batch \
  -H "Content-Type: application/json" \
  -d '[
    {"content": "Message 1", "author": "User1"},
    {"content": "Message 2", "author": "User2"}
  ]'
```

**Пример ответа:**
```json
{
  "inserted": 2
}
```

## 💾 Работа с базами данных

### H2 (In-Memory) - По умолчанию

**Запуск:**
```bash
./gradlew bootRun
```

**H2 Console:** http://localhost:8080/h2-console

**Настройки подключения:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

**Конфигурация:** `application.properties`

**Схема:** `schema.sql`

**Преимущества:**
- Не требует установки внешней БД
- Идеально для разработки и тестирования
- Автоматическая инициализация данных при запуске

### PostgreSQL

**Настройка:**

1. Установите PostgreSQL или запустите Docker:
```bash
docker run -d --name postgres-messages -p 5432:5432 -e POSTGRES_DB=messages_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -v postgres-data:/var/lib/postgresql/data postgres:15
```

2. Запустите с профилем:
```bash
./gradlew bootRun --args='--spring.profiles.active=postgresql'
```

**Конфигурация:** `application-postgresql.properties`

**Схема:** `schema-postgresql.sql`

**Подключение через psql:**
```bash
psql -U postgres -h localhost -p 5432 -d messages_db

# Просмотр таблиц
\dt

# Просмотр данных
SELECT * FROM messages;
SELECT * FROM users;
```

### MySQL

**Настройка:**

1. Установите MySQL
2. Создайте базу данных:
```sql
CREATE DATABASE messages_db;
```

3. Запустите с профилем:
```bash
./gradlew bootRun --args='--spring.profiles.active=mysql'
```

**Конфигурация:** `application-mysql.properties`

**Схема:** `schema-mysql.sql`

### H2 (In-Memory) - По умолчанию

**Запуск:**
```bash
./gradlew bootRun
```

**H2 Console:** http://localhost:8080/h2-console

**Настройки подключения:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

**Конфигурация:** `application.properties`

**Схема:** `schema.sql`

**Преимущества:**
- Не требует установки внешней БД
- Идеально для разработки и тестирования
- Автоматическая инициализация данных при запуске

### PostgreSQL

**Настройка:**

**Схема:** `schema.sql`

## 🧪 Тестирование

### Запуск всех тестов

```bash
# Windows
.\gradlew.bat test

# macOS/Linux
./gradlew test
```

### Запуск с отчетом

```bash
./gradlew test --info
```

Отчет будет доступен в `build/reports/tests/test/index.html`

### Запуск конкретного теста

```bash
./gradlew test --tests "DemoApplicationTests.testGetMessages"
```

### Сборка без тестов

```bash
./gradlew build -x test
```

## 📁 Структура проекта

```
Java_mitp_hw_5_JDBC/
├── src/
│   ├── main/
│   │   ├── java/com/example/hellospring/
│   │   │   ├── DemoApplication.java           # Главный класс приложения
│   │   │   ├── controller/
│   │   │   │   └── MessageController.java     # REST контроллер
│   │   │   ├── exception/
│   │   │   │   └── MessageNotFoundException.java
│   │   │   ├── model/
│   │   │   │   ├── Message.java               # Модель сообщения
│   │   │   │   └── User.java                  # Модель пользователя
│   │   │   ├── repository/
│   │   │   │   ├── JdbcMessageRepository.java # Репозиторий сообщений
│   │   │   │   └── JdbcUserRepository.java    # Репозиторий пользователей
│   │   │   └── service/
│   │   │       └── MessageService.java        # Бизнес-логика
│   │   └── resources/
│   │       ├── application.properties         # Основная конфигурация (PostgreSQL)
│   │       ├── application-h2.properties      # Профиль H2
│   │       ├── application-mysql.properties   # Профиль MySQL
│   │       ├── application-postgresql.properties # Профиль PostgreSQL
│   │       ├── schema.sql                     # Схема для H2
│   │       ├── schema-mysql.sql               # Схема для MySQL
│   │       └── schema-postgresql.sql          # Схема для PostgreSQL
│   └── test/
│       └── java/com/example/hellospring/
│           └── DemoApplicationTests.java      # Тесты
├── build.gradle                               # Конфигурация Gradle
├── gradlew                                    # Gradle wrapper (Unix)
├── gradlew.bat                                # Gradle wrapper (Windows)
├── DATABASE_MIGRATION.md                      # Инструкции по миграции БД
├── ЗАДАНИЕ.md                                 # Описание задания
└── README.md                                  # Этот файл
```

## 🔍 Проверка работоспособности

### 1. Проверка запуска приложения

```bash
./gradlew bootRun
```

Ожидаемый результат: приложение запускается без ошибок, в консоли видно:
```
Started DemoApplication in X.XXX seconds
```

### 2. Проверка инициализации БД

```bash
curl http://localhost:8080/api/messages
```

Ожидаемый результат: список с тестовыми сообщениями.

### 3. Проверка создания сообщения

```bash
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{"content": "Test message", "author": "Tester"}'
```

Ожидаемый результат: JSON с созданным сообщением (статус 201).

### 4. Проверка обработки ошибок

```bash
curl http://localhost:8080/api/messages/99999
```

Ожидаемый результат: JSON с ошибкой 404 Not Found.

## ⚠️ Возможные проблемы и решения

### Ошибка подключения к PostgreSQL

```
Connection to localhost:5432 refused
```

**Решение:**
1. Убедитесь, что PostgreSQL запущен: `pg_ctl status` или `sudo systemctl status postgresql`
2. Проверьте порт: `netstat -an | grep 5432`
3. Используйте H2: `./gradlew bootRun --args='--spring.profiles.active=h2'`

### База данных не существует

```
FATAL: database "messages_db" does not exist
```

**Решение:**
```bash
psql -U postgres -c "CREATE DATABASE messages_db;"
```

### Ошибка аутентификации

```
FATAL: password authentication failed for user "postgres"
```

**Решение:**
1. Измените пароль в `application.properties`
2. Или сбросьте пароль PostgreSQL:
```bash
psql -U postgres
ALTER USER postgres WITH PASSWORD 'password';
```

### Port 8080 уже используется

```
Web server failed to start. Port 8080 was already in use.
```

**Решение:**
1. Измените порт в `application.properties`: `server.port=8081`
2. Или остановите процесс на порту 8080

## 📚 Дополнительные материалы

- **Полная документация по миграции БД:** см. `DATABASE_MIGRATION.md`
- **Описание задания:** см. `ЗАДАНИЕ.md`
- **Spring Boot Documentation:** https://spring.io/projects/spring-boot
- **PostgreSQL Documentation:** https://www.postgresql.org/docs/

## 👤 Автор

**TashaNovik**
- GitHub: [@TashaNovik](https://github.com/TashaNovik)

## 📄 Лицензия

Этот проект создан в образовательных целях для курса Java в МФТИ.


