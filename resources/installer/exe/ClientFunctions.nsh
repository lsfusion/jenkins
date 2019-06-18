Var tfClientShutdownPort
Var tfClientHttpPort
Var tfClientAjpPort

Var tfClientContext

Function clientConfigPagePre
    ${ifNot} ${SectionIsSelected} ${SecClient}
        Abort
    ${endIf}
    
    !insertmacro MUI_HEADER_TEXT "$(strClientOptions)" ""

    nsDialogs::Create 1018

    StrCpy $0 "0"
    
    ${LS_CreateNumber} $(strClientShutdownPort) $clientShutdownPort $tfClientShutdownPort
    ${LS_CreateNumber} $(strClientHttpPort) $clientHttpPort $tfClientHttpPort
    ${LS_CreateNumber} $(strClientAjpPort) $clientAjpPort $tfClientAjpPort

    ${LS_CreateLabel} "$(strClientContextMessage)"
    ${LS_CreateText} "$(strClientContext)" $clientContext $tfClientContext
    
    nsDialogs::Show
FunctionEnd

Function clientConfigPageLeave
    ${NSD_GetText} $tfClientShutdownPort $clientShutdownPort
    ${NSD_GetText} $tfClientHttpPort $clientHttpPort
    ${NSD_GetText} $tfClientAjpPort $clientAjpPort

    ${if} $clientShutdownPort < 1
    ${orIf} $clientShutdownPort > 65535
        MessageBox MB_OK|MB_ICONEXCLAMATION $(strInvalidShutdownPort)
        Abort
    ${endif}

    ${if} $clientHttpPort < 1
    ${orIf} $clientHttpPort > 65535
        MessageBox MB_OK|MB_ICONEXCLAMATION $(strInvalidHttpPort)
        Abort
    ${endif}

    ${if} $clientAjpPort < 1
    ${orIf} $clientAjpPort > 65535
        MessageBox MB_OK|MB_ICONEXCLAMATION $(strInvalidAjpPort)
        Abort
    ${endif}

    ${NSD_GetText} $tfClientContext $clientContext

    Push $clientContext
    Call validateMaybeEmptyNameString
    ${if} $0 == "0"
        MessageBox MB_ICONEXCLAMATION|MB_OK $(strInvalidClientDirectory)
        Abort
    ${endIf}
FunctionEnd
