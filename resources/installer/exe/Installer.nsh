Name "lsFusion"

; NSIS 2 - use another nsis version for unicode 
Unicode true

SetCompressor lzma

RequestExecutionLevel user

# General Symbol Definitions
!define REGKEY "SOFTWARE\$(^Name)"
!define VERSION ${LSFUSION_VERSION}
!define COMPANY lsFusion
!define URL lsfusion.org

!define DOWNLOADURL "https://download.lsfusion.org/exe/links"
!define DOWNLOADURL_JAVA "https://download.lsfusion.org/java"
!define DOWNLOAD_SERVER_JAR "${DOWNLOADURL_JAVA}/lsfusion-server-${LSFUSION_VERSION}.jar"
!define DOWNLOAD_SERVER_SOURCES_JAR "${DOWNLOADURL_JAVA}/lsfusion-server-${LSFUSION_VERSION}-sources.jar"
!define DOWNLOAD_CLIENT_JAR "${DOWNLOADURL_JAVA}/lsfusion-client-${LSFUSION_VERSION}.jar"
!define DOWNLOAD_CLIENT_WAR "${DOWNLOADURL_JAVA}/lsfusion-client-${LSFUSION_VERSION}.war"

!define PLATFORM_SECTION_NAME "lsFusion"
!define SERVER_SECTION_NAME "Server"
!define CLIENT_SECTION_NAME "Client (Web & Desktop)"
!define DESKTOP_CLIENT_SECTION_NAME "Desktop Client"
!define PG_SECTION_NAME "PostgreSQL ${PG_VERSION}"
!define JAVA_SECTION_NAME "JDK ${JDK_VERSION}"
!define TOMCAT_SECTION_NAME "Apache Tomcat ${TOMCAT_FULL_VERSION}"
!define IDEA_SECTION_NAME "IntelliJ IDEA Community Edition ${IDEA_VERSION} with lsFusion plugin"
!define JASPER_SECTION_NAME "Jaspersoft Studio ${JASPER_VERSION}"

!define CLIENT_JAR "client.jar"
!define SERVER_JAR "server.jar"
!define SERVER_LIBRARY_NAME "server"
!define SERVER_SOURCES_JAR "server-sources.jar"
!define CLIENT_WAR "client.war"

!define SERVER_DIR "Server"
!define CLIENT_DIR "Client"

!define INSTBINDIR "$INSTDIR\install-bin"
!define INSTCONFDIR "$INSTDIR\install-config"

# MUI Symbol Definitions
!define MUI_ICON resources\lsfusion.ico
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_UNICON resources\lsfusion.ico
!define MUI_UNFINISHPAGE_NOAUTOCLOSE
!define MUI_LANGDLL_REGISTRY_ROOT HKLM
!define MUI_LANGDLL_REGISTRY_KEY ${REGKEY}
!define MUI_LANGDLL_REGISTRY_VALUENAME InstallerLanguage
; !define MUI_LANGDLL_ALWAYSSHOW 1

# Needed Variables
Var javaVersion
Var javaDir
Var javaHome
Var javaExe
Var jvmDll

Var pgHost
Var pgPort
Var pgDbName
Var pgUser
Var pgPassword
Var pgVersion
Var pgDir
Var pgServiceName

Var ideaDir

Var jasperDir

Var serverHost
Var serverPort
Var serverPassword
Var serverCreateService
Var serverServiceName
Var serverDisplayServiceName

Var clientShutdownPort
Var clientHttpPort
Var clientAjpPort
Var clientServiceName 
Var clientDisplayServiceName 
Var clientContext

# Included files
!include Sections.nsh
!include MUI2.nsh
!include LogicLib.nsh
!include TextFunc.nsh

!include StrFunc.nsh
${StrRep}

!include WordFunc.nsh
!include Utils.nsh

# Installer pages
!insertmacro MUI_PAGE_WELCOME

# TODO License page
;!insertmacro MUI_PAGE_LICENSE $(lsLicense)

!define MUI_PAGE_CUSTOMFUNCTION_LEAVE pageComponentsLeave
!insertmacro MUI_PAGE_COMPONENTS

!include FusionSections.nsh

# pgPagePre, javaPagePre, etc. are defined with DefinePreFeatureFunction in the end of Section.nsh, and all they do is enabling / disabling page if section is selected / unselected 

# PG pages
!include PgFunctions.nsh
Page custom pgConfigPagePre pgConfigPageLeave
!insertmacro CustomDirectoryPage $(strPostgreDirHeader) $(strPostgreDirTextTop) $(strDestinationFolder) $pgDir pgPagePre

# Java pages
!include JavaFunctions.nsh
Page custom javaConfigPagePre javaConfigPageLeave
!insertmacro CustomDirectoryPage $(strJavaDirHeader) $(strJavaDirTextTop) $(strDestinationFolder) $javaDir javaPagePre

# Server pages
!include ServerFunctions.nsh
Page custom serverConfigPagePre serverConfigPageLeave
; directory - main

# Client pages
!include ClientFunctions.nsh
Page custom clientConfigPagePre clientConfigPageLeave
; directory - main

# IDE (IntelliJ Idea + plugin) pages
; leaf - we don't need any config
!insertmacro CustomDirectoryPage $(strIdeaDirHeader) $(strIdeaDirTextTop) $(strDestinationFolder) $ideaDir ideaPagePre

# Jaspersoft Studio pages
; leaf - we don't need any config
!insertmacro CustomDirectoryPage $(strJasperDirHeader) $(strJasperDirTextTop) $(strDestinationFolder) $jasperDir jasperPagePre

!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

# Uninstaller pages
!insertmacro MUI_UNPAGE_COMPONENTS
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

# Reserved Files
!insertmacro MUI_RESERVEFILE_LANGDLL
; NSIS 2 - ReserveFile "${NSISDIR}\Plugins\x86-ansi\AdvSplash.dll"
ReserveFile "${NSISDIR}\Plugins\x86-unicode\AdvSplash.dll"

# Installer languages
!insertmacro MUI_LANGUAGE English
!insertmacro MUI_LANGUAGE Russian
!include I18nEn.nsh
!include I18nRu.nsh

LicenseLangString lsLicense ${LANG_ENGLISH} "resources\license-english.txt"
LicenseLangString lsLicense ${LANG_RUSSIAN} "resources\license-russian.txt"

# Installer attributes
OutFile ${OUT_FILE}
InstallDir "$ProgramFiles${ARCH}\lsFusion ${LSFUSION_MAJOR_VERSION}"
CRCCheck on
XPStyle on
ShowInstDetails show
VIProductVersion ${VI_LSFUSION_VERSION}
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductName "lsFusion"
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyName "${COMPANY}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyWebsite "${URL}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileDescription ""
VIAddVersionKey /LANG=${LANG_ENGLISH} LegalCopyright ""
InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails show

!include Uninstaller.nsh

Function pageComponentsLeave
FunctionEnd

# Installer functions
Function .onInit
    SetRegView ${ARCH}
    
    InitPluginsDir
    
;    Call checkUserAdmin

    Push $R1
    File /oname=$PLUGINSDIR\spltmp.bmp resources\lsfusion.bmp
    advsplash::show 1000 600 400 -1 $PLUGINSDIR\spltmp
    Pop $R1
    Pop $R1
    
    StrCpy $javaDir "${JDK_FOLDER}"

    StrCpy $ideaDir "$ProgramFiles32\JetBrains\IDEA Community Edition ${IDEA_VERSION}"
    
    StrCpy $jasperDir "$ProgramFiles${ARCH}\TIBCO\Jaspersoft Studio-${JASPER_VERSION}"

    StrCpy $pgDir "$ProgramFiles${ARCH}\PostgreSQL\${PG_VERSION}"
    StrCpy $pgHost "localhost"
    StrCpy $pgPort "5432"
    StrCpy $pgUser "postgres"
    StrCpy $pgDbName "lsfusion"
    StrCpy $pgServiceName "postgresql-${PG_VERSION}"
    
    StrCpy $clientHttpPort "8080"
    StrCpy $clientContext ""
    StrCpy $clientShutdownPort "8005"
    StrCpy $clientAjpPort "8009"
    StrCpy $clientServiceName "lsfusion${LSFUSION_MAJOR_VERSION}_client"
    StrCpy $clientDisplayServiceName "lsFusion ${LSFUSION_MAJOR_VERSION} Client"

    StrCpy $serverHost "localhost"
    StrCpy $serverPort "7652"
    StrCpy $serverServiceName "lsfusion${LSFUSION_MAJOR_VERSION}-server"
    StrCpy $serverDisplayServiceName "lsFusion ${LSFUSION_MAJOR_VERSION} Server"
    !ifndef DEV
        StrCpy $serverCreateService 1
    !else
        StrCpy $serverCreateService 0
    !endif
    
    !insertmacro MUI_LANGDLL_DISPLAY

    Call initJavaFromRegistry
    ; if installed Java is outdated then install the new one.
    ${ifNot} $javaVersion == ""
        !insertmacro DisableSection ${SecJava}
    ${endIf}

    ; Check if PostgreSQL is installed
    EnumRegKey $1 HKLM "SOFTWARE\PostgreSQL\Installations\" "0"
    ${if} $1 != ""
        ReadRegStr $pgVersion HKLM "SOFTWARE\PostgreSQL\Installations\$1" "Version"
        ReadRegStr $pgDir HKLM "SOFTWARE\PostgreSQL\Installations\$1" "Base Directory"
        ; if installed version is < PG_VERSION then abort
        ${VersionCompare} $pgVersion "${PG_VERSION}" $0
        ${if} $0 == "2"
            MessageBox MB_ICONEXCLAMATION|MB_OK $(strOldPostgreMessage)
            Abort
        ${else}
            !insertmacro DisableSection ${SecPG}
        ${endIf}
    ${endIf}
    
    !insertmacro ExpandSection ${SecPlatform}

    !ifndef DEV
        !insertmacro HideSection ${SecIdea}
        !insertmacro HideSection ${SecJasper}
    !endif
FunctionEnd

Section -post SecPost
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    
    SetOutPath $INSTDIR

    WriteUninstaller $INSTDIR\uninstall.exe
    
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${URL}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1

    Call execAntConfiguration
    
    CALL createServices

    CALL createShortcuts
SectionEnd

;Function CheckUserAdmin
;    ClearErrors
;    UserInfo::GetName
;    ${if} ${Errors}
;        MessageBox MB_OK "Error! This DLL can't run under Windows 9x!"
;        Quit
;    ${endIf}
    
;    Pop $0
;    UserInfo::GetAccountType
;    Pop $1
  
;    ${ifNot} $1 == "Admin"
;        MessageBox MB_OK|MB_ICONSTOP "$(strUserShouldBeAdmin)"
;        Quit
;    ${endIf}
;FunctionEnd

Function initJavaFromRegistry
    ClearErrors

    ReadRegStr $javaVersion HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
    ReadRegStr $javaHome HKLM "SOFTWARE\JavaSoft\Java Development Kit\$javaVersion" "JavaHome"
    ReadRegStr $jvmDll HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$javaVersion" "RuntimeLib"
    ${VersionCompare} $javaVersion "${JDK_MAJORVERSION}" $0

    IfFileExists $jvmDll noProblem

    ; there is a bug, when RuntimeLib refers client instead of server folder

    StrCpy $0 $javaHome

    Call setJvmPath

    StrCpy $javaHome $0
    StrCpy $jvmDll $2

    noProblem:

    ${if} $0 == "2"
    ${orIf} ${Errors}
       StrCpy $javaVersion ""
       StrCpy $javaHome ""
       StrCpy $jvmDll ""
   ${endIf}
FunctionEnd


# no locals in nsis
Var clientContextFile
Function execAntConfiguration

    DetailPrint "Configuring lsFusion"
    
    ${if} ${SectionIsSelected} ${SecServer}
        DetailPrint "Configuring server"
        DetailPrint "$INSTDIR\conf\settings.properties"
        ${ConfigWriteSE} "$INSTDIR\conf\settings.properties" "db.server=" "$pgHost:$pgPort" $R0
        ${ConfigWriteSE} "$INSTDIR\conf\settings.properties" "db.name=" "$pgDbName" $R0
        ${ConfigWriteSE} "$INSTDIR\conf\settings.properties" "db.user=" "$pgUser" $R0
        ${ConfigWriteSE} "$INSTDIR\conf\settings.properties" "db.password=" "$pgPassword" $R0
        ${ConfigWriteSE} "$INSTDIR\conf\settings.properties" "rmi.host=" "$serverHost" $R0
        ${ConfigWriteSE} "$INSTDIR\conf\settings.properties" "rmi.port=" "$serverPort" $R0
        ${ConfigWriteSE} "$INSTDIR\conf\settings.properties" "logics.initialAdminPassword=" "$serverPassword" $R0
    ${endIf}
    
    ${if} ${SectionIsSelected} ${SecClient}
    ${orIf} ${SectionIsSelected} ${SecIdea}
    
        ${RunLinkFile} ${ANT_ARCHIVE} "zip" "Ant" "${INSTBINDIR}" ; assuming that will unzip to ANT_ARCHIVE dir    
    
        SetOutPath ${INSTCONFDIR}
        File "install-config\*.*"
    
        ${ConfigWriteS} "${INSTCONFDIR}\configure.bat" "set JAVA_HOME=" "$javaHome" $R0
    
        ${if} ${SectionIsSelected} ${SecClient}
            DetailPrint "Configuring Client (Web & Desktop)"
    
            ${if} $clientContext == ""
                StrCpy $clientContextFile "ROOT"
            ${else}
                StrCpy $clientContextFile $clientContext
            ${endIf}
    
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "client.dir=" "$INSTDIR\${CLIENT_DIR}" $R0
            
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "client.httpPort=" "$clientHttpPort" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "client.shutdownPort=" "$clientShutdownPort" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "client.ajpPort=" "$clientAjpPort" $R0
            
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "client.war=" "${CLIENT_WAR}" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "client.conf=" "${INSTCONFDIR}\tomcat.xml" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "client.context.file=" "$clientContextFile" $R0
    
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "server.host=" "$serverHost" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "server.port=" "$serverPort" $R0
            nsExec::ExecToLog '"${INSTCONFDIR}\configure.bat" ${ANT_ARCHIVE} configureClient'
            Pop $0
    
            DetailPrint "Ant returned $0"
        ${endIf}
    
        ${if} ${SectionIsSelected} ${SecIdea}
            DetailPrint "Configuring Intellij IDEA"
            
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "jdk.home=" "$javaHome" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "jdk.majorversion=" "${JDK_MAJORVERSION}" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "jdk.version=" "${JDK_VERSION}" $R0

            ${if} ${SectionIsSelected} ${SecServer}
                ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "lsfusion.library.name=" "${SERVER_LIBRARY_NAME}" $R0
                ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "server.archive=" "$INSTDIR\${SERVER_JAR}" $R0
                ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "server.sources=" "$INSTDIR\${SERVER_SOURCES_JAR}" $R0
            ${endIf}
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "idea.majorversion=" "${IDEA_MAJORVERSION}" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "idea.dir=" "$ideaDir" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "idea.plugin=" "${IDEA_PLUGIN}" $R0
            
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "db.host=" "$pgHost" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "db.port=" "$pgPort" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "db.user=" "$pgUser" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "db.pass=" "$pgPassword" $R0
            ${ConfigWriteSE} "${INSTCONFDIR}\configure.properties" "admin.pass=" "$serverPassword" $R0
            nsExec::ExecToLog '"${INSTCONFDIR}\configure.bat" ${ANT_ARCHIVE} configureIdea'
            Pop $0
    
            DetailPrint "Ant returned $0"
        ${endIf}
        
        Delete ${INSTBINDIR}
        Delete ${INSTCONFDIR}
        Delete "bin"
    ${endIf}
FunctionEnd

# no locals in nsis
Var clientDir
Var serverDir
Var serviceFile
Function createServices
    ${if} ${SectionIsSelected} ${SecClient}
        StrCpy $clientDir "$INSTDIR\${CLIENT_DIR}"
        StrCpy $serviceFile "$clientDir\bin\lsfusion${LSFUSION_MAJOR_VERSION}_client.exe"
    
        ClearErrors
        DetailPrint "Installing Client service"
        
        Rename "$clientDir\bin\tomcat${TOMCAT_MAJOR_VERSION}.exe" $serviceFile
        Rename "$clientDir\bin\tomcat${TOMCAT_MAJOR_VERSION}w.exe" "$clientDir\bin\lsfusion${LSFUSION_MAJOR_VERSION}_clientw.exe"
        
        nsExec::ExecToStack '"$serviceFile" //IS//$clientServiceName --DisplayName "$clientDisplayServiceName" --Description "lsFusion web server" --LogPath "$clientDir\logs" --Install "$serviceFile" --Jvm "$jvmDll" --StartPath "$clientDir" --StopPath "$clientDir"'
        Pop $0
        Pop $1
        ${ifNot} $0 == "0"
            DetailPrint $1
            MessageBox MB_OK|MB_ICONSTOP $(strErrorInstallingClientService)
        ${else}
            DetailPrint "Configuring $clientServiceName service"
    
            WriteRegStr HKLM "${REGKEY}" "clientServiceName" "$clientServiceName"
    
            nsExec::ExecToLog '"$serviceFile" //US//$clientServiceName --Startup auto'
            nsExec::ExecToLog '"$serviceFile" //US//$clientServiceName --Classpath "$clientDir\bin\bootstrap.jar;$clientDir\bin\tomcat-juli.jar" --StartClass org.apache.catalina.startup.Bootstrap --StopClass org.apache.catalina.startup.Bootstrap --StartParams start --StopParams stop --StartMode jvm --StopMode jvm'
            nsExec::ExecToLog '"$serviceFile" //US//$clientServiceName --JvmOptions "-Dcatalina.home=$clientDir#-Dcatalina.base=$clientDir#-Djava.endorsed.dirs=$clientDir\endorsed#-Djava.io.tmpdir=$clientDir\temp#-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager#-Djava.util.logging.config.file=$clientDir\conf\logging.properties"'
            nsExec::ExecToLog '"$serviceFile" //US//$clientServiceName --StdOutput auto --StdError auto'

            DetailPrint "Starting $clientServiceName service"
            nsExec::ExecToLog '"$serviceFile" //ES//$clientServiceName'
        ${endIf}
    ${endIf}

    ${if} ${SectionIsSelected} ${SecServer}
    ${andIf} $serverCreateService == "1"
        StrCpy $serverDir "$INSTDIR\${SERVER_DIR}"
        StrCpy $serviceFile "$serverDir\bin\lsfusion${LSFUSION_MAJOR_VERSION}_server.exe"

        ClearErrors
        DetailPrint "Installing Server service"

        nsExec::ExecToStack '"$serviceFile" //IS//$serverServiceName --DisplayName "$serverDisplayServiceName" --Description "lsFusion application server" --LogPath "$serverDir\logs" --Install "$serviceFile" --Jvm "$jvmDll" --StartPath "$serverDir" --StopPath "$serverDir"'
        Pop $0
        Pop $1
        ${ifNot} $0 == "0"
            DetailPrint $1
            MessageBox MB_OK|MB_ICONSTOP $(strErrorInstallingServerService)
        ${else}
            DetailPrint "Configuring $serverServiceName service"

            WriteRegStr HKLM "${REGKEY}" "serverServiceName" "$serverServiceName"

            nsExec::ExecToLog '"$serviceFile" //US//$serverServiceName --Startup auto'
            nsExec::ExecToLog '"$serviceFile" //US//$serverServiceName --Classpath "$serverDir\${SERVER_JAR};$serverDir\lib\*;$serverDir\lib" --StartClass lsfusion.server.logics.BusinessLogicsBootstrap --StopClass lsfusion.server.logics.BusinessLogicsBootstrap --StartMethod start --StopMethod stop --StartMode jvm --StopMode jvm'
            nsExec::ExecToLog '"$serviceFile" //US//$serverServiceName --JvmMs=512 --JvmMx=1024'
            nsExec::ExecToLog '"$serviceFile" //US//$serverServiceName --StdOutput auto --StdError auto'

            DetailPrint "Starting $serverServiceName service"
            nsExec::ExecToLog '"$serviceFile" //ES//$serverServiceName'
        ${endIf}
    ${endIf}
    
FunctionEnd

Function createShortcuts
    DetailPrint "Creating shortcuts"
    
    SetOutPath "$INSTDIR"
    
    CreateDirectory "$SMPROGRAMS\lsFusion ${LSFUSION_MAJOR_VERSION}"

    ${if} ${SectionIsSelected} ${SecServer}
        StrCpy $serverDir "$INSTDIR\${SERVER_DIR}"
        ${if} $serverCreateService == "1"
            CreateShortCut "$SMPROGRAMS\lsFusion ${LSFUSION_MAJOR_VERSION}\Start Server.lnk" "$serverDir\bin\lsfusion.exe" "//ES//$serverServiceName" "$INSTDIR\resources\lsfusion.ico"
            CreateShortCut "$SMPROGRAMS\lsFusion ${LSFUSION_MAJOR_VERSION}\Stop Server.lnk" "$serverDir\bin\lsfusion.exe" "//SS//$serverServiceName" "$INSTDIR\resources\lsfusion.ico"
        ${else}
            CreateShortCut "$SMPROGRAMS\lsFusion ${LSFUSION_MAJOR_VERSION}\Start Server as console application.lnk" \
                            "$javaExe" \
                            "-Xmx1200m -cp $serverDir\${SERVER_JAR};$serverDir\lib\*;$serverDir\lib lsfusion.server.logics.BusinessLogicsBootstrap" \
                            "$INSTDIR\resources\lsfusion.ico"
        ${endIf}
    ${endIf}

    ${if} ${SectionIsSelected} ${SecDesktopClient}
        CreateShortCut "$SMPROGRAMS\lsFusion ${LSFUSION_MAJOR_VERSION}\Run Desktop Client.lnk" \
                        "$javaHome\bin\javaw.exe" \
                        "-Xmx300m -cp ${CLIENT_JAR} -Dlsfusion.client.hostname=$serverHost -Dlsfusion.client.hostport=$serverPort -Dlsfusion.client.exportname=default lsfusion.client.controller.MainController" \
                        "$INSTDIR\resources\lsfusion.ico"
        CreateShortCut "$DESKTOP\lsFusion Desktop Client.lnk" \
                        "$javaHome\bin\javaw.exe" \
                        "-Xmx300m -cp {CLIENT_JAR} -Dlsfusion.client.hostname=$serverHost -Dlsfusion.client.hostport=$serverPort -Dlsfusion.client.exportname=default lsfusion.client.controller.MainController" \
                        "$INSTDIR\resources\lsfusion.ico"
    ${endIf}

    ${if} ${SectionIsSelected} ${SecClient}
        CreateShortCut "$SMPROGRAMS\lsFusion ${LSFUSION_MAJOR_VERSION}\Run Web Client.lnk" "http://127.0.0.1:$clientHttpPort/$clientContext" "" "$INSTDIR\resources\lsfusion.ico"
        CreateShortCut "$DESKTOP\lsFusion Web Client.lnk" "http://127.0.0.1:$clientHttpPort/$clientContext" "" "$INSTDIR\resources\lsfusion.ico"
    ${endIf}

    CreateShortCut "$SMPROGRAMS\lsFusion ${LSFUSION_MAJOR_VERSION}\Uninstall lsFusion.lnk" "$INSTDIR\uninstall.exe"
    
    ${if} ${SectionIsSelected} ${SecIdea}
        SetOutPath "$ideaDir"
        CreateShortCut "$SMPROGRAMS\JetBrains\IntelliJ IDEA Community Edition ${IDEA_VERSION}.lnk" "$ideaDir\bin\${IDEA_EXE}" "" "$ideaDir\bin\${IDEA_EXE}"
        CreateShortCut "$DESKTOP\IntelliJ IDEA Community Edition ${IDEA_VERSION}.lnk" "$ideaDir\bin\${IDEA_EXE}" "" "$ideaDir\bin\${IDEA_EXE}"
    ${endIf}

FunctionEnd
