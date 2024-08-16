Var Dialog
Var tfPgHost
Var tfPgPort
Var tfPgUser
Var tfPgPassword1
Var tfPgPassword2
Var tfPgDbName

Function pgConfigPagePre
;   if we're not installing server or idea we don't need pg config 
    ${IfNot} ${SectionIsSelected} ${SecServer}
    ${andIfNot} ${SectionIsSelected} ${SecIdea}
    ${andIfNot} ${SectionIsSelected} ${SecPG}
        Abort
    ${endIf}

    !insertmacro MUI_HEADER_TEXT $(strPostgreOptions) ""

    nsDialogs::Create /NOUNLOAD 1018

    Pop $Dialog

    ${If} $Dialog == error
        Abort
    ${EndIf}

    StrCpy $0 "0"
;    ${LS_CreateLabel} $(strPasswordMessage)

    ${IfNot} ${SectionIsSelected} ${SecPG}
        ${LS_CreateNumber} $(strHost) $pgHost $tfPgHost
    ${endIf}

    ${LS_CreateNumber} $(strPort) $pgPort $tfPgPort

    ${IfNot} ${SectionIsSelected} ${SecPG}
        ${LS_CreateText} $(strUser) $pgUser  $tfPgUser
    ${endIf}

    ${LS_CreateText} $(strPassword) $pgPassword $tfPgPassword1

    ${If} ${SectionIsSelected} ${SecPG}
        ${LS_CreateText} $(strPasswordRetype) $pgPassword $tfPgPassword2
    ${endIf}

    ${LS_CreateText} $(strDbName) $pgDbName $tfPgDbName

    nsDialogs::Show

FunctionEnd


Function pgConfigPageLeave
    ${NSD_GetText} $tfPgPort $1
    ${NSD_GetText} $tfPgPassword1 $2
    ${NSD_GetText} $tfPgDbName $3
    
    ${if} ${SectionIsSelected} ${SecPG}
        ${NSD_GetText} $tfPgPassword2 $4
    ${else}
        ${NSD_GetText} $tfPgHost $6
        ${NSD_GetText} $tfPgUser $7
    ${endIf}

    ${if} $1 < 1
    ${orIf} $1 > 65535
        MessageBox MB_OK|MB_ICONEXCLAMATION $(strInvalidPort)
        Abort
    ${endif}
    
    Push $3
    Call validateNameString
    ${if} $0 == "0"
        MessageBox MB_ICONEXCLAMATION|MB_OK $(strInvalidDbName)
        Abort
    ${endIf}

    ${If} ${SectionIsSelected} ${SecPG}
        ${if} $2 != $4
            MessageBox MB_OK|MB_ICONEXCLAMATION $(strNotIdenticalPasswords)
            Abort
        ${endif}
        StrLen $0 $2
        ${if} $0 < 5
            MessageBox MB_OK|MB_ICONEXCLAMATION $(strPasswordTooShort)
            Abort
        ${endif}
    ${else}
        Push $6
        Call validateNameString
        ${if} $0 == "0"
            MessageBox MB_ICONEXCLAMATION|MB_OK $(strInvalidHostName)
            Abort
        ${endIf}
        
        Push $7
        Call validateNameString
        ${if} $0 == "0"
            MessageBox MB_ICONEXCLAMATION|MB_OK $(strInvalidUsername)
            Abort
        ${endIf}
        
        StrCpy $pgHost $6
        StrCpy $pgUser $7
    ${endIf}

    StrCpy $pgPort $1
    StrCpy $pgPassword $2
    StrCpy $pgDbName $3

FunctionEnd
