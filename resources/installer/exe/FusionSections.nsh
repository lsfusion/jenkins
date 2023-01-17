
;!define SKIP_FILES 1

Section "${PG_SECTION_NAME}" SecPG
    AddSize 471818 ;directory size
    ${RunLinkFile} "${PG_INSTALLER}" "exe" "PostgreSQL" '--mode unattended --unattendedmodeui none --prefix "$pgDir" --datadir "$pgDir\data" --superpassword "$pgPassword" --serverport $pgPort --servicename "$pgServiceName"' 
    
    WriteRegStr HKLM "${REGKEY}\Components" "${PG_SECTION_NAME}" 1
    WriteRegStr HKLM "${REGKEY}" "postgreInstallDir" "$pgDir"
SectionEnd

Section "${JAVA_SECTION_NAME}" SecJava
    AddSize 206832 ;directory size
    ${RunLinkFile} ${JAVA_INSTALLER} ${JAVA_INSTALLER_EXT} "Java" '${JAVA_INSTALLER_PARAMS}' 

    Call initJavaFromRegistry
    ${if} $javaHome == ""
        ${LogMessage} "JDK wasn't installed successfully: can't find javaHome in registry. Try to install JDK manually and restart installer"
        Abort
    ${endIf}

    WriteRegStr HKLM "${REGKEY}\Components" "${JAVA_SECTION_NAME}" 1
SectionEnd

# Installer sections
Section "${SERVER_SECTION_NAME}" SecServer
    SetOutPath ${INSTSERVERDIR}
    File /r "lib"
    File /r "conf"

    AddSize 108871 ;directory size
    ${GetDirectFile} "${DOWNLOAD_SERVER_JAR}" "${INSTSERVERDIR}" ${SERVER_JAR}
    ${GetDirectFile} "${DOWNLOAD_SERVER_SOURCES_JAR}" "${INSTSERVERDIR}" ${SERVER_SOURCES_JAR}  
    
    ${if} $serverCreateService == "1"
        SetOutPath ${INSTSERVERDIR}\bin
        File /oname=$serverServiceName.exe bin\lsfusion${ARCH}.exe
        File /oname=$serverServiceNamew.exe bin\lsfusionw.exe
    ${endIf}

    WriteRegStr HKLM "${REGKEY}\Components" "${SERVER_SECTION_NAME}" 1
SectionEnd

Var clientContextFile
Section "${CLIENT_SECTION_NAME}" SecClient
    AddSize 402263 ;directory size
    ${RunLinkFile} ${TOMCAT_ARCHIVE} "zip" "Tomcat" "$INSTDIR" ; because in current installation tomcat is inside folder with name apache-tomcat-{VERSION}
    Rename "$INSTDIR\apache-tomcat-${TOMCAT_VERSION}" ${INSTCLIENTDIR}
    
    ${if} $clientContext == ""
        StrCpy $clientContextFile "ROOT"
    ${else}
        StrCpy $clientContextFile $clientContext
    ${endIf}

    ${RMDir_Silent} "${INSTCLIENTDIR}\webapps" ; will be recreated in next command 
    ${GetDirectFile} "${DOWNLOAD_CLIENT_WAR}" "${INSTCLIENTDIR}\webapps" "$clientContextFile.war"   

    WriteRegStr HKLM "${REGKEY}\Components" "${CLIENT_SECTION_NAME}" 1
SectionEnd

Section "${DESKTOP_CLIENT_SECTION_NAME}" SecDesktopClient
    ${GetDirectFile} "${DOWNLOAD_CLIENT_JAR}" "$INSTDIR" ${CLIENT_JAR}  
    
    WriteRegStr HKLM "${REGKEY}\Components" "${DESKTOP_CLIENT_SECTION_NAME}" 1
SectionEnd

Var ideaConfigFile
Section "${IDEA_SECTION_NAME}" SecIdea
    AddSize 1024336 ;directory size
    SetOutPath ${INSTCONFDIR}
    File install-config\silent.config

    StrCpy $ideaConfigFile "${INSTCONFDIR}\silent.config"
    ${ConfigWriteSE} "$ideaConfigFile" "launcher${ARCH}=" "1" $R0
    ${ConfigWriteSE} "$ideaConfigFile" "jre${ARCH}=" "1" $R0

    ${RunLinkFile} ${IDEA_INSTALLER} "exe" "IntelliJ Idea" '/S /CONFIG="$ideaConfigFile" /D=$ideaDir' ; no quotes in ideaDir, otherwise doesn't work

    Delete "$ideaConfigFile"
    
    WriteRegStr HKLM "${REGKEY}\Components" "${IDEA_SECTION_NAME}" 1
    WriteRegStr HKLM "${REGKEY}" "ideaInstallDir" "$ideaDir"

    SetOutPath "${IDEA_CONFIG_DIR}\plugins" ; to create dir
    ${RunLinkFile} ${IDEA_PLUGIN} "zip" "lsFusion Idea Plugin" "${IDEA_CONFIG_DIR}\plugins" ; putting in $ideaDir/plugins will make plugin bundled and not updatable 
SectionEnd

Section "${JASPER_SECTION_NAME}" SecJasper
    AddSize 537122 ;directory size
    ${RunLinkFile} ${JASPER_INSTALLER} "exe" "Jaspersoft Studio" '/S /D="$jasperDir"' 

    WriteRegStr HKLM "${REGKEY}\Components" "${JASPER_SECTION_NAME}" 1
    WriteRegStr HKLM "${REGKEY}" "jaspersoftStudioInstallDir" "$jasperDir"
SectionEnd

# Section Descriptions
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${SecPG} $(strPgSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecJava} $(strJavaSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecServer} $(strServerSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecClient} $(strClientSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecDesktopClient} $(strDesktopClientSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecIdea} $(strIdeaSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecJasper} $(strJasperSectionDescription)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

!insertmacro DefinePreFeatureFunction ${SecJava} java
!insertmacro DefinePreFeatureFunction ${SecPG} pg
!insertmacro DefinePreFeatureFunction ${SecIdea} idea
!insertmacro DefinePreFeatureFunction ${SecJasper} jasper
