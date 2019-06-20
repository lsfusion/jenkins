
;!define SKIP_FILES 1

Section "${PG_SECTION_NAME}" SecPG
    ${RunLinkFile} "${PG_INSTALLER}" "exe" "PostgreSQL" '--mode unattended --unattendedmodeui none --prefix "$pgDir" --datadir "$pgDir\data" --superpassword "$pgPassword" --serverport $pgPort --servicename "$pgServiceName"' 
    
    WriteRegStr HKLM "${REGKEY}\Components" "${PG_SECTION_NAME}" 1
    WriteRegStr HKLM "${REGKEY}" "postgreInstallDir" "$pgDir"
SectionEnd

Section "${JAVA_SECTION_NAME}" SecJava
    ${RunLinkFile} ${JAVA_INSTALLER} ${JAVA_INSTALLER_EXT} "Java" '${JAVA_INSTALLER_PARAMS}' 

    Call initJavaFromRegistry
    ${if} $javaHome == ""
        DetailPrint "JDK wasn't isntalled succesfully: can't find javaHome in registry. Try to install JDK manually and restart installer"
        Abort
    ${endIf}

    WriteRegStr HKLM "${REGKEY}\Components" "${JAVA_SECTION_NAME}" 1
SectionEnd

# Installer sections
Section "${SERVER_SECTION_NAME}" SecServer
    SetOutPath ${INSTSERVERDIR}
    File /r "lib"
    File /r "conf"

    ${GetDirectFile} "${DOWNLOAD_SERVER_JAR}" "${INSTSERVERDIR}" ${SERVER_JAR}  
    ${GetDirectFile} "${DOWNLOAD_SERVER_SOURCES_JAR}" "${INSTSERVERDIR}" ${SERVER_SOURCES_JAR}  
    
    SetOutPath ${INSTSERVERDIR}\bin
    File /oname=$serverServiceName.exe bin\lsfusion${ARCH}.exe
    File /oname=$serverServiceNamew.exe bin\lsfusionw.exe

    WriteRegStr HKLM "${REGKEY}\Components" "${SERVER_SECTION_NAME}" 1
SectionEnd

Var clientContextFile
Section "${CLIENT_SECTION_NAME}" SecClient

    ${RunLinkFile} ${TOMCAT_ARCHIVE} "zip" "Tomcat" "$INSTDIR" ; because in current installation tomcat is inside folder with name apache-tomcat-{VERSION}
    Rename "$INSTDIR\apache-tomcat-${TOMCAT_VERSION}" ${INSTCLIENTDIR}
    
    ${if} $clientContext == ""
        StrCpy $clientContextFile "ROOT"
    ${else}
        StrCpy $clientContextFile $clientContext
    ${endIf}

    RMDir /r "${INSTCLIENTDIR}\webapps" ; will be recreated in next command 
    ${GetDirectFile} "${DOWNLOAD_CLIENT_WAR}" "${INSTCLIENTDIR}\webapps" "$clientContextFile.war"   

    WriteRegStr HKLM "${REGKEY}\Components" "${CLIENT_SECTION_NAME}" 1
SectionEnd

Section "${DESKTOP_CLIENT_SECTION_NAME}" SecDesktopClient
    ${GetDirectFile} "${DOWNLOAD_CLIENT_JAR}" "$INSTDIR" ${CLIENT_JAR}  
    
    WriteRegStr HKLM "${REGKEY}\Components" "${DESKTOP_CLIENT_SECTION_NAME}" 1
SectionEnd

Section "${IDEA_SECTION_NAME}" SecIdea

    SetOutPath ${INSTCONFDIR}
    File "install-config\silent.config"

    ${RunLinkFile} ${IDEA_INSTALLER} "exe" "IntelliJ Idea" '/S /CONFIG="${INSTCONFDIR}\silent.config" /D=$ideaDir' ; no quotes in ideaDir, otherwise doesn't work 
    
    WriteRegStr HKLM "${REGKEY}\Components" "${IDEA_SECTION_NAME}" 1
    WriteRegStr HKLM "${REGKEY}" "ideaInstallDir" "$ideaDir"

    SetOutPath "${IDEA_CONFIG_DIR}\plugins" ; to create dir
    ${RunLinkFile} ${IDEA_PLUGIN} "zip" "lsFusion Idea Plugin" "${IDEA_CONFIG_DIR}\plugins" ; putting in $ideaDir/plugins will make plugin bundled and not updatable 
SectionEnd

Section "${JASPER_SECTION_NAME}" SecJasper
    ${RunLinkFile} ${JASPER_INSTALLER} "exe" "Jaspersoft Studio" '/S /D="$jasperDir"' 

    WriteRegStr HKLM "${REGKEY}\Components" "${JASPER_SECTION_NAME}" 1
    WriteRegStr HKLM "${REGKEY}" "jaspersoftStudioInstallDir" "$jasperDir"
SectionEnd

# Section Descriptions
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${SecPG} $(strPgSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecJava} $(strJavaSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecServer} $(strServerSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecClient} $(strWebClientSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecDesktopClient} $(strDesktopClientSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecIdea} $(strIdeaSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecJasper} $(strJasperSectionDescription)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

!insertmacro DefinePreFeatureFunction ${SecJava} java
!insertmacro DefinePreFeatureFunction ${SecPG} pg
!insertmacro DefinePreFeatureFunction ${SecIdea} idea
!insertmacro DefinePreFeatureFunction ${SecJasper} jasper
