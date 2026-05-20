# Анализ зависимостей Maven

## Инструкция

Выполните команду в директории `part1/p1_1`:

```bash
mvn dependency:tree
```

Скопируйте вывод команды ниже:

```
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------< com.movies:movie-app >------------------------
[INFO] Building Movie Database Application 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- dependency:3.7.0:tree (default-cli) @ movie-app ---
[INFO] com.movies:movie-app:jar:1.0-SNAPSHOT
[INFO] +- com.h2database:h2:jar:2.2.224:compile
[INFO] \- org.hibernate.orm:hibernate-core:jar:6.4.0.Final:compile
[INFO]    +- jakarta.persistence:jakarta.persistence-api:jar:3.1.0:compile
[INFO]    +- jakarta.transaction:jakarta.transaction-api:jar:2.0.1:compile
[INFO]    +- org.jboss.logging:jboss-logging:jar:3.5.0.Final:runtime
[INFO]    +- org.hibernate.common:hibernate-commons-annotations:jar:6.0.6.Final:runtime
[INFO]    +- io.smallrye:jandex:jar:3.1.2:runtime
[INFO]    +- com.fasterxml:classmate:jar:1.5.1:runtime
[INFO]    +- net.bytebuddy:byte-buddy:jar:1.14.7:runtime
[INFO]    +- jakarta.xml.bind:jakarta.xml.bind-api:jar:4.0.0:runtime
[INFO]    |  \- jakarta.activation:jakarta.activation-api:jar:2.1.0:runtime
[INFO]    +- org.glassfish.jaxb:jaxb-runtime:jar:4.0.2:runtime
[INFO]    |  \- org.glassfish.jaxb:jaxb-core:jar:4.0.2:runtime
[INFO]    |     +- org.eclipse.angus:angus-activation:jar:2.0.0:runtime
[INFO]    |     +- org.glassfish.jaxb:txw2:jar:4.0.2:runtime
[INFO]    |     \- com.sun.istack:istack-commons-runtime:jar:4.1.1:runtime
[INFO]    +- jakarta.inject:jakarta.inject-api:jar:2.0.1:runtime
[INFO]    \- org.antlr:antlr4-runtime:jar:4.13.0:runtime
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.250 s
[INFO] Finished at: 2026-05-21T00:34:56+03:00
[INFO] ------------------------------------------------------------------------
```

---

## Вопрос 1: Прямые зависимости

**Вопрос:** Сколько прямых (direct) зависимостей имеет ваш проект?

**Ваш ответ:** 2: H2 Database и Hibernate Core

**Объяснение:** Прямые зависимости - это те, которые вы явно добавили в секцию `<dependencies>` в pom.xml.

---

## Вопрос 2: Транзитивные зависимости Hibernate

**Вопрос:** Сколько транзитивных зависимостей добавляет Hibernate Core?

**Ваш ответ:** 16

**Подсказка:** Посчитайте строки под `org.hibernate.orm:hibernate-core:jar:6.4.0.Final` в дереве зависимостей.

---

## Вопрос 3: Транзитивные зависимости

**Вопрос:** Перечислите 3 транзитивных зависимости, которые подтягивает Hibernate.

1. jakarta.persistence-api
2. antlr4-runtime
3. jboss-logging
