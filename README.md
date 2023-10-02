# <span style="color: white">**JAVA-post-service**
____
![JAVA-post-service](post-service.jpg)

### <span style="color: white">Данное приложение предназначено для фиксации отправления, отслеживания перемещения между почтовами пунктами и выдачей посылок


<span style="color: white">Приложение использует слудющие технологии:
- Java 
- Spring Boot (web, validation, data-jpa)
- Maven
- используемая БД - PostgreSQL
- Lombok
- JUnit и Mock тестирование

<span style="color: white">___Ниже приведены эндпоинты и кратное описаних их функционала:___
- регистрация посылки. <span style="color: green">(POST "/e-posts/new/{typePostDelivery}")
- отправление посылки. <span style="color: green">(PUT "/e-posts/register/departure?where")
- прибытие посылки. <span style="color: green">(PUT "/e-posts/register/arrival")
- отметка о готовности посылки к получению. <span style="color: green">(PUT "/e-posts/register/receiving"")
- отметка о вручении посылки. <span style="color: green">(PUT "/e-posts/register/receive"")
- получение истории перемещения посылки. <span style="color: green">(PUT "/e-posts/tracking/{postId}"")

<span style="color: white">В разработке принимали участие https://github.com/AlexanderKolnakov и https://github.com/dzrgln:













