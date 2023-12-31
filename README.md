# QA-Java-diplom-2
2-я часть дипломного проекта курса "Инженер по автоматизации тестирования на Java"

## Требования

- Java 11
- Maven 4.0.0

## Зависимости
- RestAssured: Библиотека Java для тестирования RESTful API
- Gson: Библиотека Java для сериализации и десериализации JSON
- Commons HttpClient: Библиотека для выполнения HTTP-запросов
- JUnit: Фреймворк для тестирования Java
- Allure JUnit4: Интеграция Allure для JUnit4
- Allure RestAssured: Интеграция Allure для RestAssured

## Настройка проекта

Для запуска тестов вам потребуется:
1. Установить Java Development Kit (JDK) версии 11 и Maven.
2. Склонировать репозиторий на локальную машину.
3. Открыть терминал в корне проекта.
4. Запустить тесты:
```mvn clean test```

## Генерация отчетов Allure
1. После запуска тестов выполните команду: <br>
```mvn allure:serve```
2. Дождитесь генерации отчета и автоматического открытия в браузере.


### Автор
Катречко Александр @kolo_ksay