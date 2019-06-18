# English locale
LangString strUserShouldBeAdmin ${LANG_ENGLISH} "Installer requres administrator permissions to run."

LangString strPlatformSectionDescription ${LANG_ENGLISH} "lsFusion ${LSFUSION_MAJOR_VERSION}"
LangString strServerSectionDescription ${LANG_ENGLISH} "Server"
LangString strWebClientSectionDescription ${LANG_ENGLISH} "Client (Web & Desktop)"
LangString strDesktopClientSectionDescription ${LANG_ENGLISH} "Desktop Client"
LangString strServicesSectionDescription ${LANG_ENGLISH} "Install server applications as window services"
LangString strShortcutsSectionDescription ${LANG_ENGLISH} "Install shortcuts"
LangString strCreateService ${LANG_ENGLISH} "Create service"
LangString strPgSectionDescription ${LANG_ENGLISH} "PostgresSQL database"
LangString strJavaSectionDescription ${LANG_ENGLISH} "Java Development Kit ${JDK_VERSION}"
LangString strIdeaSectionDescription ${LANG_ENGLISH} "IntelliJ IDEA Community Edition ${IDEA_VERSION} with bundled lsFusion plugin"
LangString strJasperSectionDescription ${LANG_ENGLISH} "Jaspersoft Studio ${JASPER_VERSION}"

LangString strPlatformUnSectionDescription ${LANG_ENGLISH} "Uninstall lsFusion"
LangString strPgUnSectionDescription ${LANG_ENGLISH} "Uninstall PostgresSQL database"
LangString strJavaUnSectionDescription ${LANG_ENGLISH} "Java Development Kit ${JDK_VERSION} can't be uninstalled automatically, please uninstall it manually from 'Add or remove programs' console"
LangString strIdeaUnSectionDescription ${LANG_ENGLISH} "Uninstall IntelliJ IDEA Community Edition ${IDEA_VERSION}"
LangString strJasperUnSectionDescription ${LANG_ENGLISH} "Uninstall Jaspersoft Studio ${JASPER_VERSION}"

LangString strPlatformIsNotSelected ${LANG_ENGLISH} "You have to select at least one of lsFusion components to install."

LangString strPostgreDirHeader ${LANG_ENGLISH} "Choose the folder in which to install PostgreSQL ${PG_MAJORVERSION}."
LangString strPostgreDirTextTop ${LANG_ENGLISH} "Setup will install PostgreSQL ${PG_MAJORVERSION} to the following folder."
LangString strDestinationFolder ${LANG_ENGLISH} "Destination Folder"

LangString strJavaDirHeader ${LANG_ENGLISH} "Choose the folder in which to install JDK ${JDK_VERSION}."
LangString strJavaDirTextTop ${LANG_ENGLISH} "Setup will install JDK ${JDK_VERSION} to the following folder."
LangString strJavaTitle ${LANG_ENGLISH} "Java Virtual Machine"
LangString strJavaSubtitle ${LANG_ENGLISH} "Java Virtual Machine path selection."
LangString strSelectJavaMessage ${LANG_ENGLISH} "Please select the path of a Java SE 7.0 or later JDK installed on your system."
LangString strNoJavaError ${LANG_ENGLISH} "No Java Virtual Machine found in folder:$\r$\n"

LangString strClientOptions ${LANG_ENGLISH} "Options for lsFusion Client installation."

LangString strIdeaDirHeader ${LANG_ENGLISH} "Choose the folder in which to install IntelliJ IDEA Community Edition ${IDEA_VERSION}."
LangString strIdeaDirTextTop ${LANG_ENGLISH} "Setup will install IntelliJ IDEA Community Edition ${IDEA_VERSION} to the following folder."

LangString strJasperDirHeader ${LANG_ENGLISH} "Choose the folder in which to install Jaspersoft Studio ${JASPER_VERSION}."
LangString strJasperDirTextTop ${LANG_ENGLISH} "Setup will install Jaspersoft Studio ${JASPER_VERSION} to the following folder."

LangString strClientShutdownPort ${LANG_ENGLISH} "Server Shutdown Port"
LangString strClientHttpPort ${LANG_ENGLISH} "HTTP/1.1 Connector Port"
LangString strClientAjpPort ${LANG_ENGLISH} "AJP/1.3 Connector Port"
LangString strServiceName ${LANG_ENGLISH} "Windows Service Name"
LangString strInvalidShutdownPort ${LANG_ENGLISH} "The shutdown port should be between 1 and 65535 inclusive."
LangString strInvalidHttpPort ${LANG_ENGLISH} "The http port should be between 1 and 65535 inclusive."
LangString strInvalidAjpPort ${LANG_ENGLISH} "The ajp port should be between 1 and 65535 inclusive."

LangString strInvalidServiceName ${LANG_ENGLISH} 'Invalid service name. It must match the following criteria:$\r$\n* Starts with an alphabet$\r$\n* Ends with alphanumeric$\r$\n* Allowed special characters are _(underscore), .(dot) and -(hyphen)$\r$\n* Minimum lengh: 6 characters & Maximum length: 50 characters'
LangString strInvalidHostName ${LANG_ENGLISH} 'The HostName may not contain a space or any of the following characters: <>:"/\:|?*'
LangString strInvalidDbName ${LANG_ENGLISH} 'Database name may not contain a space or any of the following characters: <>:"/\:|?*'
LangString strInvalidUsername ${LANG_ENGLISH} 'Username may not contain a space or any of the following characters: <>:"/\:|?*'

LangString strServerOptions ${LANG_ENGLISH} "Options for lsFusion Server installation."
LangString strServerHost ${LANG_ENGLISH} "Host"
LangString strServerPort ${LANG_ENGLISH} "Port"
LangString strServerPasswordMessage ${LANG_ENGLISH} "Enter administrator password:"
LangString strShortcutsForAllUsers ${LANG_ENGLISH} "Create shortcuts for all users"
LangString strClientContextMessage ${LANG_ENGLISH} "Enter lsFusion Client WebApp context (base URL path after deployment):"
LangString strClientContext ${LANG_ENGLISH} "WebApp context"
LangString strInvalidClientDirectory ${LANG_ENGLISH} 'The WebApp context may not contain a space or any of the following characters: <>:"/\:|?*'
LangString strContinueOnEmptyPassword ${LANG_ENGLISH} "Do you really want to continue and use empty password?"

LangString strOldPostgreMessage ${LANG_ENGLISH} "The installed version of PostgreSQL is too old (<${PG_MAJORVERSION}). Please uninstall this version or try a manual installation of PostgreSQL."

LangString strPostgreOptions ${LANG_ENGLISH} "PostgreSQL Server options."
LangString strPasswordMessage ${LANG_ENGLISH} "Please provide a password for the database superuser."
LangString strPassword ${LANG_ENGLISH} "Password"
LangString strPasswordRetype ${LANG_ENGLISH} "Retype password"
LangString strPortMessage ${LANG_ENGLISH} "Please select the port number the server should listen on."
LangString strPort ${LANG_ENGLISH} "Port"
LangString strHost ${LANG_ENGLISH} "Hostname"
LangString strUser ${LANG_ENGLISH} "Username"
LangString strDbName ${LANG_ENGLISH} "Datbase name"
LangString strNotIdenticalPasswords ${LANG_ENGLISH} "The passwords you have entered are not identical."
LangString strPasswordTooShort ${LANG_ENGLISH} "The password is too short (<5 characters)."
LangString strPasswordEmpty ${LANG_ENGLISH} "Password should not be empty."
LangString strInvalidPort ${LANG_ENGLISH} "The port should be between 1 and 65535 inclusive."

LangString strErrorInstallingClientService ${LANG_ENGLISH} "Failed to install lsFusion Client service.$\r$\nCheck your settings and permissions and try to install manually.$\r$\n"
LangString strErrorInstallingServerService ${LANG_ENGLISH} "Failed to install lsFusion Server service.$\r$\nCheck your settings and permissions and try to install manually.$\r$\n"
