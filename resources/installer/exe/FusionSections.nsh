
;!define SKIP_FILES 1

Section "${PG_SECTION_NAME}" SecPG
    ${RunLinkFile} "${PG_INSTALLER}" "exe" "PostgreSQL" '--mode unattended --unattendedmodeui none --prefix "$pgDir" --datadir "$pgDir\data" --superpassword "$pgPassword" --serverport $pgPort --servicename "$pgServiceName"' 
    
    WriteRegStr HKLM "${REGKEY}\Components" "${PG_SECTION_NAME}" 1
    WriteRegStr HKLM "${REGKEY}" "postgreInstallDir" "$pgDir"
SectionEnd

Section "${JAVA_SECTION_NAME}" SecJava
    ${RunLinkFile} ${JAVA_INSTALLER} ${JDK_DISTREXTENSION} "Java" '/s ADDLOCAL="ToolsFeature,SourceFeature,PublicjreFeature"' 

    Call initJavaFromRegistry
    ${if} $javaHome == ""
        DetailPrint "JDK wasn't isntalled succesfully: can't find javaHome in registry. Try to install JDK manually and restart installer"
        Abort
    ${endIf}

    WriteRegStr HKLM "${REGKEY}\Components" "${JAVA_SECTION_NAME}" 1
SectionEnd

# Installer sections
SubSection "!${PLATFORM_SECTION_NAME}" SecPlatform
    Section -CoreFiles
        DetailPrint "Installing lsFusion"
        
        SetOutPath $INSTDIR\resources
        File "resources\lsfusion.ico"
        
        ; TODO
;        File "resources\license-english.txt"
;        File "resources\license-russian.txt"
    SectionEnd

    Section "${SERVER_SECTION_NAME}" SecServer
        ${GetDirectFile} "${DOWNLOAD_SERVER_JAR}" "$INSTDIR\${SERVER_DIR}" ${SERVER_JAR}  
        ${GetDirectFile} "${DOWNLOAD_SERVER_SOURCES_JAR}" "$INSTDIR\${SERVER_DIR}" ${SERVER_SOURCES_JAR}  
        
        SetOutPath $INSTDIR\${SERVER_DIR}
        File /r "lib"
        File /r "conf"

        SetOutPath $INSTDIR\${SERVER_DIR}\bin
        File /oname=lsfusion${LSFUSION_MAJOR_VERSION}_server.exe bin\lsfusion${ARCH}.exe
        File /oname=lsfusion${LSFUSION_MAJOR_VERSION}_serverw.exe bin\lsfusionw.exe

        WriteRegStr HKLM "${REGKEY}\Components" "${SERVER_SECTION_NAME}" 1
    SectionEnd

    Section "${CLIENT_SECTION_NAME}" SecClient

        ${RunLinkFile} ${TOMCAT_ARCHIVE} "zip" "Tomcat" "$INSTDIR\${CLIENT_DIR}"
        
        ; will be moved later by ant to the webapps
        ${GetDirectFile} "${DOWNLOAD_CLIENT_WAR}" "$INSTDIR\${CLIENT_DIR}" ${CLIENT_WAR}   

        WriteRegStr HKLM "${REGKEY}\Components" "${CLIENT_SECTION_NAME}" 1
    SectionEnd

    Section "${DESKTOP_CLIENT_SECTION_NAME}" SecDesktopClient
        ${GetDirectFile} "${DOWNLOAD_CLIENT_JAR}" "$INSTDIR" ${CLIENT_JAR}  
        
        WriteRegStr HKLM "${REGKEY}\Components" "${DESKTOP_CLIENT_SECTION_NAME}" 1
    SectionEnd
SubSectionEnd

Section "${IDEA_SECTION_NAME}" SecIdea
    ${RunLinkFile} ${IDEA_INSTALLER} "exe" "IntelliJ Idea" '/S /D=$ideaDir' 
    
    WriteRegStr HKLM "${REGKEY}\Components" "${IDEA_SECTION_NAME}" 1
    WriteRegStr HKLM "${REGKEY}" "ideaInstallDir" "$ideaDir"

    ${RunLinkFile} ${IDEA_PLUGIN} "zip" "lsFusion Idea Plugin" "$ideaDir/plugins" 
SectionEnd

Section "${JASPER_SECTION_NAME}" SecJasper
    ${RunLinkFile} ${JASPER_INSTALLER} "exe" "Jaspersoft Studio" '/S /D=$jasperDir' 

    WriteRegStr HKLM "${REGKEY}\Components" "${JASPER_SECTION_NAME}" 1
    WriteRegStr HKLM "${REGKEY}" "jaspersoftStudioInstallDir" "$jasperDir"
SectionEnd

# Section Descriptions
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${SecPG} $(strPgSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecJava} $(strJavaSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${SecPlatform} $(strPlatformSectionDescription)
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
