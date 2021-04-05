---
title: 'Launch parameters: Overview'
sidebar_label: Overview
---

-   Application server (Server)
    -   [Java](#appjava)
    -   [lsFusion](#applsfusion)
-   Web server (Client)
    -   [Java](#webjava)
    -   [lsFusion](#weblsfusion)

## Application server (Server)

### Java {#appjava}

Java application server startup parameters are set in the launch command (for example, for [manual](Execution_manual_.md#command-broken) or [automatic](Execution_auto_.md#settings-broken) installation):

|<br /><br/>|Name|Type|Description|Default|
|---|---|---|---|---|
|System (starting with X)|[Standard](https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html)|<br /><br/>|<div class="content-wrapper"><br/><p>Standard Java parameters. It is important above all to pay attention to:</p><br/><ul><br/><li>cp - classpath, the paths in which java looks for class files and other resources (including lsFusion modules). The default is. - current folder (different for automatic installation).</li><br/><li>Xmx - maximum memory size. The default value is determined depending on the configuration of the computer on which the application server is running. For complex logics, it is recommended that you allocate at least 4GB. </li><br/></ul><br/></div>|<br /><br/>|
|-XX:CMSInitiatingOccupancyFraction|int|In general, this is the standard parameter responsible for the threshold after which the CMS garbage collector is turned on. At the same time, the platform uses this parameter to target the memory usage amount using LRU caches (setting more aggressive parameters for cleaning them if this goal is exceeded, and less aggressive in the opposite case). For heavily loaded servers, it is recommended that you set it in the range from 40 to 60.|70|
|Custom (starting with D)|<div class="content-wrapper"><br/><p>-Dlsfusion.server.lightstart</p><br/></div>|boolean|<p>"Light" start mode (usually used during development). In this mode, the server does not perform metadata synchronization operations or create [security policy](Security_policy.md) settings forms, etc., and the startup time and the amount of memory consumed at startup are therefore reduced.</p><br/><p>In the [IDE](IDE.md) it is set with a checkmark in [lsFusion server configuration](IDE.md#configuration) (enabled by default).</p>|false|
|<div class="content-wrapper"><br/><p>-Dlsfusion.server.devmode</p><br/></div>|boolean|<p>Development mode. In this mode:</p><br/><ul><br/><li>System tasks are not launched (so as not to interfere with the debugger)</li><br/><li>You can edit [report design](Report_design.md) in [interactive print](In_a_print_view_PRINT_.md#interactive) view</li><br/><li>Anonymous access to the API and UI is enabled ([system parameters](Working_parameters.md) enableAPI, enableUI). In addition, anonymous access in this mode is as an admin and not an anonymous user</li><br/><li>Client is automatically reconnected when connection is lost</li><br/><li>The cache for reading reports from resources is turned off</li><br/></ul><br/><p>In the [IDE](IDE.md), automatically enabled when running [lsFusion server configuration](IDE.md#configuration).</p>|false|
|-Dlsfusion.server.testmode|boolean|<p>Enables some experimental features</p><br/><p>Automatically enabled if assertions are enabled (-ea option)</p>|false|

### lsFusion {#applsfusion}

lsFusion startup parameters for server applications can be set in one of the following ways (in the order of their priorities, lower priority at the bottom):

-   In the resources in the lsfusion.xml file in the places where these parameters are used, after: (relevant for platform forks)
-   In lsfusion.properties (usually part of a project, which means it acts by default for all installations)
-   In conf/settings.properties (for specific installations)
-   In the [Java startup options](#appjava) (starting with D, e.g. -Dlogics.topModule=FFF)

|Name|Type|Description|<p>Default</p>|
|---|---|---|---|
|<div class="content-wrapper"><br/><p>db.server, db.name, db.user, db.password, db.connectTimeout</p><br/></div>|string, string, string, string, int|<p>Database server connection parameters:</p><br/><ul><br/><li>db.server - the address of the database server (plus, if necessary, the port after a colon, for example localhost:6532)</li><br/><li>db.name - database name</li><br/><li>db.user - username to connect to the database server</li><br/><li>db.user - user password to connect to the database server</li><br/><li>db.connectTimeout - DBMS connection timeout</li><br/></ul>|localhost, lsfusion, postgres, , 1000|
|<div class="content-wrapper"><br/><p>rmi.port, rmi.exportName, http.port</p><br/></div>|int, string, int|<p>Access settings for the application server:</p><br/><ul><br/><li>rmi.port - port for the application server (RMI register / objects exported)</li><br/><li>rmi.exportName - name of the application server (the root RMI object exported by it). It makes sense to use it if you need to export several logics on one port</li><br/><li>http.port - port for the web server embedded in the application server (used for access from external systems)</li><br/></ul>|7652, default, 7651|
|<div class="content-wrapper"><br/><p>logics.includePaths, logics.excludePaths, logics.topModule, logics.orderDependencies</p><br/></div>|string. string, string, string|Parameters of the [project](Projects.md) (which modules to load and in what order, detailed description here)|logics.includePaths equals *, others blank|
|<div class="content-wrapper"><br/><p>user.country, user.language, user.timezone, user.twoDigitYearStart</p><br/><p>(user.setCountry, user.setLanguage, user.setTimezone)</p><br/></div>|string, string, string, int|<p>Standard Java parameters defining [locale](Internationalization.md#locale) parameters (regional settings - language, country, etc., detailed description here)</p><br/><p>Due to the peculiarities of Java Spring (namely, locale parameters are considered byJava Spring to be set even if they are not explicitly specified in the start command, that is, settings of these parameters in .properties files are ignored), the platform supports "clones" of these parameters that start as set: if they are specified (either in .properties files or in the launch string), they "overload" the native parameters. That is, the priority is OS, -Duser.*, User.set* in .properties files and -Duser.set* (none of the above applies to user.twoDigitYearStart, since it is not a standard Java parameter)</p>|The first three are determined from the operating system settings, current year minus 80|
|<div class="content-wrapper"><br/><p>db.namingPolicy, db.maxIdLength</p><br/></div>|string, int|<p>Parameters of the [naming policy](Tables.md#name) for tables and fields:</p><br/><p>db.namingPolicy - the name of the java class of the property (full name, with package); in the constructor, it must accept one parameter of type int - the maximum size of the name.</p><br/><p>Builtin policy class names:</p><br/><ul><br/><li>Complete with signature - lsfusion.server.physics.dev.id.name.FullDBNamingPolicy</li><br/><li>Complete without signature - lsfusion.server.physics.dev.id.name.NamespaceDBNamingPolicy</li><br/><li>Brief - lsfusion.server.physics.dev.id.name.ShortDBNamingPolicy</li><br/></ul><br/><p>db.maxIdLength - maximum size of a table or field name. Passed as the first parameter to the constructor of the java class of the naming policy for tables and fields.</p>|Complete with signature, 63|
|db.denyDropModules, db.denyDropTables|boolean, boolean|<p>Ban on deletion at startup:</p><br/><ul><br/><li>db.denyDropModules - modules</li><br/><li>db.denyDropTables - tables</li><br/></ul>|false, false|
|logics.initialAdminPassword|string|Default admin password|<br /><br/>|

Example conf/settings.properties file ([section 3](#appp3-broken)):

## $FUSION\_DIR$/conf/settings.properties

    db.server=localhost
    db.name=lsfusion
    db.user=postgres
    db.password=pswrd

    rmi.port=7652


:::info
By default, it is assumed that the startup parameter files conf/settings.properties and lsfusion.properties are located in the application server's startup folder. However, with [automatic installation](Execution_auto_.md) under GNU Linux symlinks for these files (as well as for [log](Journals_and_logs.md#logs) folders)  are automatically created to [other files](Execution_auto_.md#settings-broken) whose layout is better aligned with Linux ideology.
:::

## Web server (Client)

### Java {#appjava}

Java web server startup parameters are set in the Tomcat launch command, which, in turn, launches this web server (for example, for [automatic](Execution_auto_.md#webapp-broken) installation). 

|<br /><br/>|Name|Type|Description|
|---|---|---|---|
|System (starting with X)|[Standard](https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html)|<br /><br/>|<div class="content-wrapper"><br/><p>Standard Java parameters. It is important above all to pay attention to:</p><br/><ul><br/><li>Xmx - maximum memory size. For complex logics, it is recommended that you allocate at least 2GB. </li><br/></ul><br/></div>|

### lsFusion {#applsfusion}

lsFusion startup parameters for the web server can be set in one of the following ways (in the order of their priorities, lower priority at the bottom):

-   In web applications' [context](http://tomcat.apache.org/tomcat-7.0-doc/config/context.html#Defining_a_context) parameters:
    -   in a web application in the file /WEB-INF/web.xml, the context-param tag (relevant for platform forks)
    -   in a web application in the file /META-INF/context.xml, Context tag, Parameter tag (relevant for platform forks)
    -   in Tomcat, in the file $CATALINA\_BASE/conf/\[enginename\]/\[hostname\]/\[contextpath\].xml, tag Context, tag Parameter, where:
        -   $CATALINA\_BASE$ is the folder where Tomcat is installed (for example, with[automatic](Execution_auto_.md#settings-broken) installation, this folder is $INSTALL\_DIR/Client)
        -   \[contextpath\] - contextual path of the web application (for example, with [automatic](Execution_auto_.md#settings-broken) installation this name is empty by default, which in Tomcat is equivalent to the name ROOT; with [manual](Execution_manual_.md#tomcat-broken) installation it depends on the name of the war file), 
        -   \[enginename\] and \[hostname\] are the names of the tomcat implementation mechanism and the web server computer (for example, with [automatic](Execution_auto_.md#settings-broken) installation these names are catalina and localhost respectively)
    -   in Tomcat, in the file $CATALINA\_BASE/conf/server.xml, Context tag, Parameter tag (not recommended)
-   In URL parameters (e.g. [http://tryonline.lsfusion.org?host=3.3.3.3&port = 4444](http://tryonline.lsfusion.org?host=3.3.3.3&port=4444))

|Name|Type|Description|Default|
|---|---|---|---|
|<div class="content-wrapper"><br/><p>host, port, exportName</p><br/></div>|string, int, string|<p>Connection settings for the application server. Must match the [access parameters](#accessapp-broken) for the application server.</p><br/><ul><br/><li>host - application server address</li><br/><li>port - port of the application server. Must match the parameter rmi.port</li><br/><li>exportName - name of the application server. Must match the parameter rmi.exportName</li><br/></ul>|localhost, 7652, default|

Example Tomcat configuration file ([section 3](#webp3-broken) in context parameters):

**$CATALINA\_BASE/conf/\\\\\[enginename\\\\\]/\\\\\[hostname  
\]/ROOT.xml**

    <?xml version='1.0' encoding='utf-8'?>
    <Context>
        <Parameter name="host" value="localhost" override="false"/>
        <Parameter name="port" value="7652" override="false"/>
    </Context>


:::info
In addition to the launch parameters, the platform also has [system parameters](Working_parameters.md) which are set a little differently and are relevant mainly for processes of various components of the platform (that is, processes that occur after they are launched).
:::
