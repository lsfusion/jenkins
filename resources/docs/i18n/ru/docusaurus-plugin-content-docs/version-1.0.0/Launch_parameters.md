---
title: 'Параметры запуска: Обзор'
sidebar_label: Обзор
---

-   Сервер приложений (Server)
    -   [Java](#appjava)
    -   [lsFusion](#applsfusion)
-   Веб-сервер (Client)
    -   [Java](#webjava)
    -   [lsFusion](#weblsfusion)

## Сервер приложений (Server)

### Java {#appjava}

Java параметры запуска сервера приложений задаются в его команде запуска (например для [ручной](Execution_manual_.md#command-broken) или [автоматической](Execution_auto_.md#settings-broken) установки) :

|<br /><br/>|Название|Тип|Описание|По умолчанию|
|---|---|---|---|---|
|Системные (начинаются на X)|[Стандартные](https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html)|<br /><br/>|<div class="content-wrapper"><br/><p>Стандартные Java параметры. Прежде всего важно обратить внимание на:</p><br/><ul><br/><li>cp - classpath, пути в которых java ищет class файлы и другие ресурсы (в том числе lsFusion модули). По умолчанию равен . - текущая папка (при автоматической установке отличается).</li><br/><li>Xmx - максимальный размер памяти. Значение по умолчанию определяется в зависимости от конфигурации компьютера, на котором запускается сервер приложений. Для сложных логик рекомендуется устанавливать не менее 4ГБ. </li><br/></ul><br/></div>|<br /><br/>|
|-XX:CMSInitiatingOccupancyFraction|int|Вообще это стандартный параметр, отвечающий за порог, после которого включается CMS сборщик мусора. В то же время платформа использует этот параметр для таргетирования объема используемой памяти при помощи LRU кэшей (устанавливая более агрессивные параметры их очистки, если эта цель превышена, и менее агрессивные - в обратном случае). Для высоконагруженных серверов рекомендуется устанавливать в диапазоне от 40 до 60.|70|
|Пользовательские (начинаются на D)|<div class="content-wrapper"><br/><p>-Dlsfusion.server.lightstart</p><br/></div>|boolean|<p>Режим "облегченного" запуска (как правило используется при разработке). В этом режиме сервер не выполняет операции синхронизации метаданных, создания форм настройки [политики безопасности](Security_policy.md) и т.п., соответственно уменьшается время запуска и объем потребляемой памяти при запуске.</p><br/><p>В [IDE](IDE.md) регулируется галочкой в [lsFusion server конфигурации](IDE.md#configuration) (по умолчанию включена).</p>|false|
|<div class="content-wrapper"><br/><p>-Dlsfusion.server.devmode</p><br/></div>|boolean|<p>Режим разработки. В этом режиме:</p><br/><ul><br/><li>Не запускаются системные задачи (чтобы не мешать отладчику)</li><br/><li>Включается возможность редактирования [дизайна отчетов](Report_design.md) в [интерактивном печатном](In_a_print_view_PRINT_.md#interactive) представлении</li><br/><li>Включается анонимный доступ к API и UI ([системные параметры](Working_parameters.md) enableAPI, enableUI). Кроме того в этом режиме анонимный доступ идет под админом, а не анонимным пользователем</li><br/><li>Клиент автоматически переподключается при потере связи</li><br/><li>Выключается кэш чтения отчетов из ресурсов</li><br/></ul><br/><p>В [IDE](IDE.md) автоматически включается при запуске [lsFusion server конфигурации](IDE.md#configuration).</p>|false|
|-Dlsfusion.server.testmode|boolean|<p>Включает некоторые экспериментальные возможности</p><br/><p>Автоматически включается, если включены assertion'ы (опция -ea)</p>|false|

### lsFusion {#applsfusion}

lsFusion параметры запуска сервера приложений могут задаваться одним из следующих способов (в порядке их приоритетов, снизу более приоритетные) :

-   В ресурсах в xml-файле lsfusion.xml в местах использования этих параметров, после : (актуально для форков платформы)
-   В lsfusion.properties (обычно являются частью проекта, а значит действует по умолчанию для всех инсталляций)
-   В conf/settings.properties (для конкретных инсталляций)
-   В [Java параметрах запуска](#appjava) (начиная с D, например -Dlogics.topModule=FFF)

|Название|Тип|Описание|<p>По умолчанию</p>|
|---|---|---|---|
|<div class="content-wrapper"><br/><p>db.server, db.name, db.user, db.password, db.connectTimeout</p><br/></div>|string, string, string, string, int|<p>Параметры подключения к серверу БД (базы данных):</p><br/><ul><br/><li>db.server - адрес сервера БД (плюс при необходимости порт через :, например localhost:6532)</li><br/><li>db.name - имя БД</li><br/><li>db.user - имя пользователя для подключения к серверу БД</li><br/><li>db.user - пароль пользователя для подключения к серверу БД</li><br/><li>db.connectTimeout - таймаут подключения к СУБД</li><br/></ul>|localhost, lsfusion, postgres, , 1000|
|<div class="content-wrapper"><br/><p>rmi.port, rmi.exportName, http.port</p><br/></div>|int, string, int|<p>Параметры доступа к серверу приложений:</p><br/><ul><br/><li>rmi.port - порт сервера приложений (экспортируемых им RMI регистра / объектов)</li><br/><li>rmi.exportName - имя сервера приложений (экспортируемого им корневого RMI объекта). Имеет смысл использовать, если на одном порту необходимо экспортировать несколько логик</li><br/><li>http.port - порт веб-сервера встроенного в сервер приложений (используется для [обращения из внешних систем](Access_from_an_external_system.md))</li><br/></ul>|7652, default, 7651|
|<div class="content-wrapper"><br/><p>logics.includePaths, logics.excludePaths, logics.topModule, logics.orderDependencies</p><br/></div>|string. string, string, string|Параметры [проекта](Projects.md) (какие модули загружать и в каком порядке, подробное описание по ссылке)|logics.includePaths равен *, остальные - пустые|
|<div class="content-wrapper"><br/><p>user.country, user.language, user.timezone, user.twoDigitYearStart</p><br/><p>(user.setCountry, user.setLanguage, user.setTimezone)</p><br/></div>|string, string, string, int|<p>Стандартные Java параметры, определяющие параметры [локали](Internationalization.md#locale) (региональные настройки - язык, страна и т.п., подробное описание по ссылке)</p><br/><p>Из-за особенностей Java Spring (а именно, что параметры локали считаются Java Spring заданными, даже если они явно не заданы в команде запуска, то есть настройки этих параметров в .properties файлах игнорируются), в платформе поддерживаются "клоны" этих параметров начинающиеся на set, которые, в случае если заданы (как в .properties файлах так и в строке запуска), "перегружают" родные параметры. То есть приоритет такой ОС, -Duser.*, user.set* в .properties файлах,  -Duser.set* (все вышесказанное не касается user.twoDigitYearStart, так как он не является стандартным Java параметром)</p>|Первые три определяются из настроек операционной системы, Текущий год минус 80|
|<div class="content-wrapper"><br/><p>db.namingPolicy, db.maxIdLength</p><br/></div>|string, int|<p>Параметры [политики именования](Tables.md#name) таблиц и полей:</p><br/><p>db.namingPolicy - имя java-класса политики (полное, с package'м), в конструкторе должен принимать один параметр типа int - максимальный размер имени.</p><br/><p>Имена классов встроенных политик:</p><br/><ul><br/><li>Полное с сигнатурой - lsfusion.server.physics.dev.id.name.FullDBNamingPolicy</li><br/><li>Полное без сигнатуры - lsfusion.server.physics.dev.id.name.NamespaceDBNamingPolicy</li><br/><li>Краткое - lsfusion.server.physics.dev.id.name.ShortDBNamingPolicy</li><br/></ul><br/><p>db.maxIdLength - максимальный размер имени таблицы или поля. Передается первым параметром в конструктор java-класса политики именования таблиц и полей.</p>|Полное с сигнатурой, 63|
|db.denyDropModules, db.denyDropTables|boolean, boolean|<p>Запреты на удаления при запуске:</p><br/><ul><br/><li>db.denyDropModules - модулей</li><br/><li>db.denyDropTables - таблиц</li><br/></ul>|false, false|
|logics.initialAdminPassword|string|Пароль администратора по умолчанию|<br /><br/>|

Пример файла conf/settings.properties ([3-й пункт](#appp3-broken)):

## $FUSION\_DIR$/conf/settings.properties

    db.server=localhost
    db.name=lsfusion
    db.user=postgres
    db.password=pswrd

    rmi.port=7652


:::note
По умолчанию предполагается, что файлы параметров запуска conf/settings.properties и lsfusion.properties находятся в папке запуска сервера приложений. Впрочем при [автоматической установке](Execution_auto_.md) под Linux для этих файлов (как и для папок [логов](Journals_and_logs.md#logs))  автоматически создаются symlink'и на [другие файлы](Execution_auto_.md#settings-broken), расположение которых лучше соответствует идеологии Linux.
:::

## Веб-сервер (Client)

### Java {#appjava}

Java параметры запуска веб-сервера задаются в команде запуска Tomcat, на котором, в свою очередь, запускается этот веб-сервер (например для [автоматической](Execution_auto_.md#webapp-broken) установки). 

|<br /><br/>|Название|Тип|Описание|
|---|---|---|---|
|Системные (начинаются на X)|[Стандартные](https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html)|<br /><br/>|<div class="content-wrapper"><br/><p>Стандартные Java параметры. Прежде всего важно обратить внимание на:</p><br/><ul><br/><li>Xmx - максимальный размер памяти. Для сложных логик рекомендуется устанавливать не менее 2ГБ.</li><br/></ul><br/></div>|

### lsFusion {#applsfusion}

lsFusion параметры запуска веб-сервера могут задаваться одним из следующих способов (в порядке их приоритетов, снизу более приоритетные) :

-   В параметрах [контекста](http://tomcat.apache.org/tomcat-7.0-doc/config/context.html#Defining_a_context) веб-приложения:
    -   в веб-приложении в файле /WEB-INF/web.xml, тег context-param (актуально для форков платформы)
    -   в веб-приложении в файле /META-INF/context.xml, тег Context, тег Parameter (актуально для форков платформы)
    -   в Tomcat в файле $CATALINA\_BASE/conf/\[enginename\]/\[hostname\]/\[contextpath\].xml, тег Context, тег Parameter, где:
        -   $CATALINA\_BASE$ - папка, в которую установлен Tomcat (например, в [автоматической](Execution_auto_.md#settings-broken) установке, эта папка равна $INSTALL\_DIR/Client)
        -   \[contextpath\] - контекстный путь веб-приложения (например, в [автоматической](Execution_auto_.md#settings-broken) установке, по умолчанию это имя пустое, что в Tomcat'е эквивалентно имени ROOT, в [ручной ](Execution_manual_.md#tomcat-broken)- зависит от имени war-файла), 
        -   \[enginename\] и \[hostname\] - имена механизма реализации tomcat и компьютера веб-сервера (например в [автоматической](Execution_auto_.md#settings-broken) установке, эти имена равны catalina и localhost соответственно)
    -   в Tomcat в файле $CATALINA\_BASE/conf/server.xml, тег Context, тег Parameter (не рекомендуется)
-   В параметрах URL'а (например <http://tryonline.lsfusion.org?host=3.3.3.3&port=4444>)

|Название|Тип|Описание|По умолчанию|
|---|---|---|---|
|<div class="content-wrapper"><br/><p>host, port, exportName</p><br/></div>|string, int, string|<p>Параметры подключения к серверу приложений. Должны соответствовать [параметрам доступа](#accessapp-broken) к серверу приложений.</p><br/><ul><br/><li>host - адрес сервера приложений</li><br/><li>port - порт сервера приложений. Должен соответствовать параметру - rmi.port</li><br/><li>exportName - имя сервера приложений. Должен соответствовать параметру - rmi.exportName</li><br/></ul>|localhost, 7652, default|

Пример файла настройки Tomcat ([3-й пункт](#webp3-broken) в параметрах контекста):

## $CATALINA\_BASE/conf/\[enginename\]/\[hostname\]/ROOT.xml

    <?xml version='1.0' encoding='utf-8'?>
    <Context>
        <Parameter name="host" value="localhost" override="false"/>
        <Parameter name="port" value="7652" override="false"/>
    </Context>


:::note
Помимо параметров запуска, в платформе также существуют [системные параметры](Working_parameters.md), которые задаются немного по другому и актуальны преимущественно для процессов работы различных компонент платформы (то есть процессов, происходящих после их запуска).
:::
