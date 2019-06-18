
Var tfServerHost
Var tfServerPort
Var tfServerPassword1
Var tfServerPassword2
Var cbServerCreateService

Function serverConfigPagePre
    ${IfNot} ${SectionIsSelected} ${SecServer}
    ${andIfNot} ${SectionIsSelected} ${SecClient}
    ${andIfNot} ${SectionIsSelected} ${SecDesktopClient}
    ${andIfNot} ${SectionIsSelected} ${SecIdea}
        Abort
    ${endIf}

    !insertmacro MUI_HEADER_TEXT $(strServerOptions) ""

    nsDialogs::Create /NOUNLOAD 1018

    StrCpy $0 "0"
    
    ${ifNot} ${SectionIsSelected} ${SecServer}
        ${LS_CreateText} "$(strServerHost)" $serverHost $tfServerHost
    ${endIf}

    ${LS_CreateNumber} "$(strServerPort)" $serverPort $tfServerPort
    ${LS_CreateLabel} "$(strServerPasswordMessage)"
    ${LS_CreatePassword} "$(strPassword)" $serverPassword $tfServerPassword1
    
    ${if} ${SectionIsSelected} ${SecServer}
        ${LS_CreatePassword} "$(strPasswordRetype)" $serverPassword $tfServerPassword2

        ${LS_CreateCheckBox} "$(strCreateService)" $cbServerCreateService
        ${NSD_SetState} $cbServerCreateService $serverCreateService
    ${endIf}

    nsDialogs::Show
FunctionEnd

Function serverConfigPageLeave
    ${ifNot} ${SectionIsSelected} ${SecServer}
        ${NSD_GetText} $tfServerHost $serverHost
        Push $serverHost
        Call validateNameString
        ${if} $0 == "0"
            MessageBox MB_ICONEXCLAMATION|MB_OK $(strInvalidHostName)
            Abort
        ${endIf}
    ${endIf}

    ${NSD_GetText} $tfServerPort $serverPort
    ${if} $serverPort < 1
    ${orIf} $serverPort > 65535
        MessageBox MB_OK|MB_ICONEXCLAMATION $(strInvalidPort)
        Abort
    ${endif}
    
    ${NSD_GetText} $tfServerPassword1 $9
    ${if} $9 == ""
        MessageBox MB_ICONEXCLAMATION|MB_YESNO $(strContinueOnEmptyPassword) IDYES yes
        Abort
        yes:
    ${endIf}

    ${if} ${SectionIsSelected} ${SecServer}
        ${NSD_GetText} $tfServerPassword2 $8
        ${if} $9 != $8
            MessageBox MB_OK|MB_ICONEXCLAMATION $(strNotIdenticalPasswords)
            Abort
        ${endif}
    
        ${NSD_GetState} $cbServerCreateService $serverCreateService
    ${endIf}
    
    StrCpy $serverPassword $9
FunctionEnd
