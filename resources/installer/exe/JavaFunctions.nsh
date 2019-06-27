;Var tfClientShutdownPort
;Var tfClientHttpPort
;Var tfClientAjpPort

Var tfJavaHome
Function javaConfigPagePre
    ${if} ${SectionIsSelected} ${SecJava}
        Abort
    ${endIf}
    
    ${IfNot} ${SectionIsSelected} ${SecServer}
    ${andIfNot} ${SectionIsSelected} ${SecClient}
    ${andIfNot} ${SectionIsSelected} ${SecDesktopClient}
    ${andIfNot} ${SectionIsSelected} ${SecIdea} ; we need it for ant + configuring SDK's
        Abort
    ${endIf}

    !insertmacro MUI_HEADER_TEXT "$(strJavaTitle)" "$(strJavaSubtitle)"

    nsDialogs::Create 1018

    StrCpy $0 "0"
    ${LS_CreateDirRequest} $(strSelectJavaMessage) $javaHome $tfJavaHome javaConfigPagePre_onDirBrowse

    nsDialogs::Show
FunctionEnd

!insertmacro DefineOnBrowseFunction $tfJavaHome javaConfigPagePre_onDirBrowse

Function javaConfigPageLeave
    ${NSD_GetText} $tfJavaHome $0
    
    ${if} $0 == ""
    ${orIfNot} ${FileExists} "$0\bin\java.exe"
        MessageBox MB_OK|MB_ICONSTOP "$(strNoJavaError)$0"
        DetailPrint "$(strNoJavaError)$0"
        Abort
    ${EndIf}
    

    ; Need path to jvm.dll to configure the service - uses $javaHome
    Call setJvmPath
    
    StrCpy $javaHome $0
    StrCpy $jvmDll $2
    ${If} $jvmDll == ""
        MessageBox MB_OK|MB_ICONSTOP "$(strNoJavaError)$0"
        DetailPrint "$(strNoJavaError)$0"
        Abort
    ${EndIf}


FunctionEnd

; ====================
; setJvmPath Function
; ====================
;
; Find the full JVM path, and put the result on top of the stack
; Implicit argument: $javaHome
; Will return an empty string if the path cannot be determined
;
Function setJvmPath

    ClearErrors

    ;Step one: Is this a JRE path (Program Files\Java\XXX)
    StrCpy $1 "$0"

    StrCpy $2 "$1\bin\hotspot\jvm.dll"
    IfFileExists "$2" foundJVM
    StrCpy $2 "$1\bin\server\jvm.dll"
    IfFileExists "$2" foundJVM
    StrCpy $2 "$1\bin\client\jvm.dll"
    IfFileExists "$2" foundJVM
    StrCpy $2 "$1\bin\classic\jvm.dll"
    IfFileExists "$2" foundJVM

    ;Step two: Is this a Java path (Program Files\XXX\jre)
    StrCpy $1 "$0\jre"

    StrCpy $2 "$1\bin\hotspot\jvm.dll"
    IfFileExists "$2" foundJVM
    StrCpy $2 "$1\bin\server\jvm.dll"
    IfFileExists "$2" foundJVM
    StrCpy $2 "$1\bin\client\jvm.dll"
    IfFileExists "$2" foundJVM
    StrCpy $2 "$1\bin\classic\jvm.dll"
    IfFileExists "$2" foundJVM

    StrCpy $2 ""

    foundJVM:

    ClearErrors
FunctionEnd
