RequestExecutionLevel user

# Macro for selecting uninstaller sections
!macro HideUnsection SECTION_NAME UNSECTION_ID
    ReadRegStr $0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"

    ${if} ${Errors}
    ${orIf} $0 == ""
        !insertmacro HideSection ${UNSECTION_ID}
    ${endIf}
!macroend

# Uninstaller sections
Section /o "un.${PG_SECTION_NAME}" UnSecPG
SectionEnd

Section /o "un.${JAVA_SECTION_NAME}" UnSecJava
    SectionIn RO
SectionEnd

Section /o "un.${SERVER_SECTION_NAME}" UnSecServer
SectionEnd

Section /o "un.${CLIENT_SECTION_NAME}" UnSecClient
SectionEnd

Section /o "un.${DESKTOP_CLIENT_SECTION_NAME}" UnSecDesktopClient
SectionEnd

Section /o "un.${IDEA_SECTION_NAME}" UnSecIdea
SectionEnd

Section /o "un.${JASPER_SECTION_NAME}" UnSecJasper
SectionEnd

Var hasComponents
Section -un.Uninstall
    StrCpy $hasComponents 0    
    ${if} ${SectionIsSelected} ${UnSecServer}
        StrCpy $serverServiceName "lsfusion${LSFUSION_MAJOR_VERSION}_server"
        StrCpy $serviceFile "${INSTSERVERDIR}\bin\$serverServiceName.exe"

        ${if} ${FileExists} "$serviceFile" ; checking if service was created
            DetailPrint "Removing lsFusion Server service"
            nsExec::ExecToLog '"$serviceFile" //DS//$serverServiceName'

            DetailPrint "Removing lsFusion Server shortcuts"
            Delete "$SMPROGRAMS\${LSFUSION_NAME}\Start Server.lnk"
            Delete "$SMPROGRAMS\${LSFUSION_NAME}\Stop Server.lnk"
        ${else}
            DetailPrint "Removing lsFusion Server shortcuts"
            Delete "$SMPROGRAMS\${LSFUSION_NAME}\Start Server as console application.lnk"
        ${endIf}            
        
        DetailPrint "Removing lsFusion Server directory"
        ${RMDir_Silent} ${INSTSERVERDIR}
        
        WriteRegStr HKLM "${REGKEY}\Components" "${SERVER_SECTION_NAME}" ""
    ${else}
        SectionGetText ${UnSecServer} $0
        ${ifNot} $0 == "" ; if not hidden and not uninstalled 
            StrCpy $hasComponents 1
        ${endIf}
    ${endIf}

    ${if} ${SectionIsSelected} ${UnSecClient}
        StrCpy $clientServiceName "lsfusion${LSFUSION_MAJOR_VERSION}_client"
        StrCpy $serviceFile "${INSTCLIENTDIR}\bin\$clientServiceName.exe"

        DetailPrint "Removing lsFusion Client service"
        nsExec::ExecToLog '"$serviceFile" //DS//$clientServiceName'

        DetailPrint "Removing lsFusion Client shortcuts"
        Delete "$SMPROGRAMS\${LSFUSION_NAME}\Open Web Client.lnk"
        Delete "$SMPROGRAMS\${LSFUSION_NAME}\Start Client.lnk"
        Delete "$SMPROGRAMS\${LSFUSION_NAME}\Stop Client.lnk"        
        Delete "$DESKTOP\lsFusion Web Client.lnk"

        DetailPrint "Removing lsFusion Client directory"
        ${RMDir_Silent} ${INSTCLIENTDIR}
         
        WriteRegStr HKLM "${REGKEY}\Components" "${CLIENT_SECTION_NAME}" ""
    ${else}
        SectionGetText ${UnSecClient} $0
        ${ifNot} $0 == "" ; if not hidden and not uninstalled 
            StrCpy $hasComponents 1
        ${endIf}
    ${endIf}

    ${if} ${SectionIsSelected} ${UnSecDesktopClient}
        DetailPrint "Removing lsFusion Desktop Client shortcuts"
        Delete "$SMPROGRAMS\${LSFUSION_NAME}\Open Desktop Client.lnk"
        Delete "$DESKTOP\lsFusion Desktop Client.lnk"

        DetailPrint "Removing lsFusion Desktop Client file"
        Delete "$INSTDIR\${CLIENT_JAR}"

        WriteRegStr HKLM "${REGKEY}\Components" "${DESKTOP_CLIENT_SECTION_NAME}" ""
    ${else}
        SectionGetText ${UnSecDesktopClient} $0
        ${ifNot} $0 == "" ; if not hidden and not uninstalled 
            StrCpy $hasComponents 1
        ${endIf}
    ${endIf}

    ${if} ${SectionIsSelected} ${UnSecPG}
        ReadRegStr $0 HKLM "${REGKEY}" "postgreInstallDir"
        ${ifNot} ${Errors}
        ${andIfNot} $0 == ""
        ${andIf} ${FileExists} "$0\uninstall-postgresql.exe"
            DetailPrint "Removing PostgreSQL"
            nsExec::ExecToLog '"$0\uninstall-postgresql.exe" --mode unattended'
        ${endIf}

        WriteRegStr HKLM "${REGKEY}\Components" "${PG_SECTION_NAME}" ""
    ${endIf}
        
    ${if} ${SectionIsSelected} ${UnSecIdea}
        ReadRegStr $0 HKLM "${REGKEY}" "ideaInstallDir"
        ${ifNot} ${Errors}
        ${andIfNot} $0 == ""
        ${andIf} ${FileExists} "$0\bin\Uninstall.exe"
            DetailPrint "Removing Intellij IDEA"
            nsExec::ExecToLog "$0\bin\Uninstall.exe /S"
            
            Delete "$DESKTOP\IntelliJ IDEA Community Edition ${IDEA_VERSION}.lnk"
            Delete "$SMPROGRAMS\JetBrains\IntelliJ IDEA Community Edition ${IDEA_VERSION}.lnk"
            RMDir "$SMPROGRAMS\JetBrains"
        ${endIf}

        WriteRegStr HKLM "${REGKEY}\Components" "${IDEA_SECTION_NAME}" ""
    ${endIf}
    
    ${if} ${SectionIsSelected} ${UnSecJasper}
        ReadRegStr $0 HKLM "${REGKEY}" "jaspersoftStudioInstallDir"
        DetailPrint "Jaspersoft Studio jasperDir: $0"
        ${ifNot} ${Errors}
        ${andIfNot} $0 == ""
        ${andIf} ${FileExists} "$0\uninst.exe"
            DetailPrint "Removing Jaspersoft Studio"
            nsExec::ExecToLog "$0\uninst.exe /S"
        ${endIf}

        WriteRegStr HKLM "${REGKEY}\Components" "${JASPER_SECTION_NAME}" ""
    ${endIf}

    ${ifNot} $hasComponents == 1        
        DetailPrint "Cleaning registry"
        DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
        DeleteRegKey HKLM "${REGKEY}"

        DetailPrint "Removing shortcuts"
        Delete "$SMPROGRAMS\${LSFUSION_NAME}\Uninstall.lnk"
        ; in theory we don't need /r but just in case
        RMDir /r /REBOOTOK "$SMPROGRAMS\${LSFUSION_NAME}"

        Delete /REBOOTOK $INSTDIR\lsfusion.ico
        Delete /REBOOTOK $INSTDIR\uninstall.exe
        RmDir /r /REBOOTOK $INSTDIR
    ${endIf}
SectionEnd

# Uninstaller functions
Function un.onInit
    SetRegView ${ARCH}

    ReadRegStr $INSTDIR HKLM "${REGKEY}" Path

    !insertmacro MUI_UNGETLANGUAGE

    !insertmacro HideUnsection "${PG_SECTION_NAME}" ${UnSecPG}
    !insertmacro HideUnsection "${JAVA_SECTION_NAME}" ${UnSecJava}
    !insertmacro HideUnsection "${SERVER_SECTION_NAME}" ${UnSecServer}
    !insertmacro HideUnsection "${CLIENT_SECTION_NAME}" ${UnSecClient}
    !insertmacro HideUnsection "${DESKTOP_CLIENT_SECTION_NAME}" ${UnSecDesktopClient}
    !insertmacro HideUnsection "${IDEA_SECTION_NAME}" ${UnSecIdea}
    !insertmacro HideUnsection "${JASPER_SECTION_NAME}" ${UnSecJasper}
FunctionEnd

# Section Descriptions
!insertmacro MUI_UNFUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${UnSecPG} $(strPgUnSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${UnSecJava} $(strJavaUnSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${UnSecServer} $(strServerUnSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${UnSecClient} $(strClientUnSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${UnSecDesktopClient} $(strDesktopClientUnSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${UnSecIdea} $(strIdeaUnSectionDescription)
!insertmacro MUI_DESCRIPTION_TEXT ${UnSecJasper} $(strJasperUnSectionDescription)
!insertmacro MUI_UNFUNCTION_DESCRIPTION_END
