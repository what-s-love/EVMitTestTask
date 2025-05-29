# EVMit Telegram Mini App

Spring Boot-приложение с интеграцией Telegram Mini App, аутентификацией через Telegram WebApp, сборкой с frontend Tailwind CSS и возможностью деплоя в Google Cloud Run.

---

## 🚀 Фунцкионал

* Аутентификация пользователя через Telegram WebApp
* Валидация подписи Telegram Mini App
* Отображение данных Telegram-пользователя
* Сохранение пользователей и сессий в PostgreSQL
* Frontend-стилизация с Tailwind CSS

---

## 🛠️ Локальный запуск

### 🔧 1. Установка зависимостей

#### 📆 Node.js (для Tailwind)

```bash
cd frontend
npm install
```

#### 🧵 Сборка Tailwind CSS

```bash
npm run build
```

> Файл `tailwind.css` будет собран в `src/main/resources/static/css/` и автоматически доступен Spring Boot.

---

### 🐘 2. Конфигурация

Приложение ожидает переменные окружения следующего вида, которые будут переданы в файл `application.yml`:

```.env
SERVER_PORT=8080
DB_URL=jdbc:postgresql://localhost:**порт_БД**/**имя_БД**
DB_USERNAME=postgres
DB_PASSWORD=**пароль_БД**
DB_DRIVER_CLASS_NAME=org.postgresql.Driver
TELEGRAM_BOT_TOKEN=1234567890:AABB1aBCdEfJKlMNOPQRstuVw2xWoSsEucM
```

---

### 🏃 3. Запуск приложения

```bash
./mvnw spring-boot:run
```

В браузере: http\://localhost:8080

---

## 🐳 Docker сборка

### 🐘 1. Конфигурация

Приложение ожидает переменные окружения следующего вида, которые будут переданы в файл `application.yml`:

```.env
SERVER_PORT=8080
DB_URL=jdbc:postgresql://localhost:**порт_БД**/**имя_БД**
DB_USERNAME=postgres
DB_PASSWORD=**пароль_БД**
DB_DRIVER_CLASS_NAME=org.postgresql.Driver
TELEGRAM_BOT_TOKEN=1234567890:AABB1aBCdEfJKlMNOPQRstuVw2xWoSsEucM
```

---

### 🏃 3. Запуск приложения

Сборка:

```bash
docker build -t evmit-bot .
```

Локальный запуск:

```bash
docker run -p 8080:8080 evmit-bot
```

---

## 🐳 Docker-compose сборка

### 🐘 1. Конфигурация

Приложение ожидает переменные окружения следующего вида, которые будут переданы в файл `application.yml`:

```.env
SERVER_PORT=8080
APP_PORTS=8080:8080
DB_IMAGE=postgres:16.3
DB_PORTS=5050:5432
DB_URL=jdbc:postgresql://**имя_контейнера_БД**:5432/**имя_БД**
DB_USERNAME=postgres
DB_PASSWORD=**пароль_БД**
DB_DRIVER_CLASS_NAME=org.postgresql.Driver
TELEGRAM_BOT_TOKEN=1234567890:AABB1aBCdEfJKlMNOPQRstuVw2xWoSsEucM
```

---

### 🏃 3. Запуск приложения

Локальный запуск:

```bash
docker compose up -d   
```

---

## ☁️ Деплой в Google Cloud Run

1. Подключите репозиторий к Cloud Build
2. Убедитесь, что `Dockerfile` в корне
3. Push в `main` запускает:

   * Сборку Tailwind CSS
   * Maven-сборку Spring Boot
   * Деплой в Cloud Run

---

## 🔐 Аутентификация через Telegram

1. Telegram передаёт `initData` при запуске mini app
2. На сервере выполняется:

   * Парсинг initData
   * Проверка подписи HMAC-SHA256
   * Валидация auth\_date
   * Сохранение пользователя и сессии

---

## 🧱 Структура проекта

```
┌ frontend/                     # Tailwind CSS и input.css
├ src/main/resources/static/    # tailwind.css генерируется сюда
├ src/main/resources/templates/ # Thymeleaf-шаблоны
├ src/main/java/...             # Spring Boot backend
├ Dockerfile                    # мультистейдж: frontend → backend → runtime
└ README.md

```

---
