# java-developer-test-asheremet
Для запуска приложения требуется:
- внести api.key и secret.key в файл build.gradle следующим образом :
  applicationDefaultJvmArgs = ["-Dapikey=... -Dsecret.key=..."]
- собрать проект коммандой 'gradle build'
- запустить коммандой 'gradle run'.

Аналогично эти же опции можно настроить в IDE:
- Edit configurations
- Add VM options
- Прописать -Dapi.key=... -Dsecret.key=... во вкладке с настройками JVM.

Так же можно запустить через Java Options запуская приложение следующим образом:
- _JAVA_OPTIONS="-Dapi.key=... -Dsecret.key=..." ./gradlew run

Просмотр метрик :
- Через браузер по ссылке http://localhost:8080/metrics
- В терминале коммандой curl localhost:8080/metrics