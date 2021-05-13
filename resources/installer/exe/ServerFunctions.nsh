
Var tfServerHost
Var tfServerPort
Var tfServerPassword1
Var tfServerPassword2
Var cbServerCreateService
Var tfServiceName
Var tfServiceDisplayName

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

        ${ifNot} ${SectionIsSelected} ${SecIdea}
            ${LS_CreateCheckBox} "$(strServerCreateService)" $cbServerCreateService
            ${NSD_SetState} $cbServerCreateService $serverCreateService
            
            ${NSD_OnClick} $cbServerCreateService EnDisableServiceTextFields
            
            ${LS_CreateText} "$(strServiceName)" $serverServiceName $tfServiceName
            ${LS_CreateText} "$(strServiceDisplayName)" $serverServiceDisplayName $tfServiceDisplayName
            Call EnDisableServiceTextFields
        ${endIf}
    ${endIf}

    nsDialogs::Show
FunctionEnd

Function EnDisableServiceTextFields
    ${NSD_GetState} $cbServerCreateService $serverCreateService
    ${If} $serverCreateService == 1
        EnableWindow $tfServiceName 1
        EnableWindow $tfServiceDisplayName 1
    ${Else}
        EnableWindow $tfServiceName 0
        EnableWindow $tfServiceDisplayName 0
    ${EndIf}
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
    
        ${ifNot} ${SectionIsSelected} ${SecIdea}
            ${NSD_GetState} $cbServerCreateService $serverCreateService
            
            ${If} $serverCreateService == 1
                ${NSD_GetText} $tfServiceName $serverServiceName          
                Push $serverServiceName
                Call validateServiceName
                ${if} $0 == "0"
                    MessageBox MB_ICONEXCLAMATION|MB_OK $(strInvalidServiceName)
                    Abort
                ${endIf}
                          
                ${NSD_GetText} $tfServiceDisplayName $serverServiceDisplayName 
            ${endIf}           
        ${endIf}
    ${endIf}
    
    StrCpy $serverPassword $9
FunctionEnd
