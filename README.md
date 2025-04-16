# OTP Service

Сервис для генерации и валидации одноразовых кодов (OTP) с поддержкой отправки по Email, Telegram, SMS и сохранением в файл. Предусмотрена ролевая модель (ADMIN, USER) и токенная авторизация через JWT.

---

## Возможности

### Пользователь (USER):
- Регистрация и логин
- Привязка Telegram-аккаунта
- Обновление номера телефона
- Генерация OTP-кода по выбранному каналу: Email, Telegram, SMS, FILE
- Валидация OTP-кода по операции

### Администратор (ADMIN):
- Просмотр и изменение текущей конфигурации OTP (длина кода, срок жизни)
- Получение списка всех пользователей (кроме админов)
- Удаление пользователей и всех их OTP-кодов

---

## Технологии
- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA + PostgreSQL
- Lombok
- JWT (jjwt)
- Telegram Bots API
- Twilio API (для SMS)
- Jakarta Mail (для Email)
- Maven

---

## Структура проекта
- `controller` — REST-контроллеры
- `service` — бизнес-логика
- `model` — сущности JPA
- `dto` — структуры входных/выходных данных
- `repository` — доступ к БД
- `security` — JWT и авторизация
- `config` — конфигурации (бот, фильтры, Spring Security)

## Запуск проекта

### 1. Клонирование репозитория
```bash
git clone https://github.com/leralee/otp-service.git
cd otp-service
```

### 2. Установка переменных окружения
Создайте файл `.env` в корне проекта:
```env
# JWT
JWT_SECRET=ваш_секретный_ключ
JWT_EXPIRATION_MINUTES=30

# Email
EMAIL_USERNAME=ваш_email@gmail.com
EMAIL_PASSWORD=пароль_приложения

# Telegram
TELEGRAM_BOT_TOKEN=ваш_токен_бота

# Twilio (для SMS)
TWILIO_ACCOUNT_SID=...
TWILIO_AUTH_TOKEN=...
TWILIO_PHONE_NUMBER=...
```

### 3. Настройка application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/otpdb
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update

# Email настройки
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Telegram
telegram.bot.username=otp_leralee_helper_bot
```

### 4. Запустите PostgreSQL и создайте базу данных:
```sql
CREATE DATABASE otp_db;
```

---

## Роли и авторизация
- Используется JWT токен в заголовке `Authorization: Bearer <token>`
- Доступ к `/admin/**` разрешен только ADMIN
- Все остальные эндпоинты требуют авторизации

---

## Примеры запросов

### Регистрация
```http
POST /auth/register
{
  "login": "user1",
  "email": "user1@example.com",
  "password": "12345678",
  "telegramChatId": null,
  "phoneNumber": "+77001234567"
}
```

### Генерация OTP
```http
POST /otp/generate
Authorization: Bearer <token>
{
  "operationId": "payment_123",
  "channel": "EMAIL" // или TELEGRAM, SMS, FILE
}
```

### Валидация OTP
```http
POST /otp/validate
Authorization: Bearer <token>
{
  "operationId": "payment_123",
  "code": "123456"
}
```

### Получение конфигурации OTP (только ADMIN)
```http
GET /admin/config
Authorization: Bearer <admin-token>
```

---

## Готовый администратор

При первом запуске приложения автоматически создаётся пользователь с правами администратора. Это позволяет сразу протестировать административный функционал.

**Учётные данные администратора по умолчанию:**

```
Логин: admin
Пароль: admin123
```
--

## Безопасность
- Хранение паролей — BCrypt
- Генерация JWT токенов с подписью
- Разделение доступа по ролям
- Поддержка только одного администратора

---

## Примечания
- Для получения Telegram `chat_id` необходимо начать диалог с ботом `otp_leralee_helper_bot` и отправить `/start`
- FILE OTP сохраняется в корне проекта, файл `otp_{operationId}.txt`




