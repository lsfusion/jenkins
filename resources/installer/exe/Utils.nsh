!macro _LS_CreateLabel TEXT
    IntOp $5 $0 + 2  
    ${NSD_CreateLabel} 0 $5u 100% 12u "${TEXT}"
    IntOp $0 $0 + 15
!macroend
!define LS_CreateLabel "!insertmacro _LS_CreateLabel"

!macro _LS_CreateLabel2 TEXT
    IntOp $5 $0 + 2  
    ${NSD_CreateLabel} 0 $0u 100% 24u "${TEXT}"
    IntOp $0 $0 + 24
!macroend
!define LS_CreateLabel2 "!insertmacro _LS_CreateLabel2"

!macro _LS_CreateControl TYPE LABEL VALUE_VAR CONTROL_VAR
    IntOp $5 $0 + 2  
    ${NSD_CreateLabel} 0 $5u 110u 14u "${LABEL}"
    ${NSD_Create${TYPE}} 120u $0u 180u 12u "${VALUE_VAR}"
    Pop ${CONTROL_VAR}
    IntOp $0 $0 + 17  
!macroend
!define LS_CreateControl "!insertmacro _LS_CreateControl"

!macro _LS_CreateNumber LABEL VALUE_VAR CONTROL_VAR
    ${LS_CreateControl} Number ${LABEL} ${VALUE_VAR} ${CONTROL_VAR}
!macroend
!define LS_CreateNumber "!insertmacro _LS_CreateNumber"

!macro _LS_CreateText LABEL VALUE_VAR CONTROL_VAR
    ${LS_CreateControl} Text ${LABEL} ${VALUE_VAR} ${CONTROL_VAR}
!macroend
!define LS_CreateText "!insertmacro _LS_CreateText"

!macro _LS_CreatePassword LABEL VALUE_VAR CONTROL_VAR
    ${LS_CreateControl} Password ${LABEL} ${VALUE_VAR} ${CONTROL_VAR}
!macroend
!define LS_CreatePassword "!insertmacro _LS_CreatePassword"

!macro _LS_CreateCheckBox LABEL CONTROL_VAR
    ${LS_CreateControl} CheckBox ${LABEL} "" ${CONTROL_VAR}
!macroend
!define LS_CreateCheckBox "!insertmacro _LS_CreateCheckBox"

!macro _LS_CreateDirRequest LABEL_HEIGHT_IND LABEL VALUE_VAR CONTROL_VAR ONBROWSE
    ${LS_CreateLabel${LABEL_HEIGHT_IND}} ${LABEL}
    
    ${NSD_CreateDirRequest} 0 $0u 282u 12u ${VALUE_VAR}
    Pop ${CONTROL_VAR}

    ${NSD_CreateBrowseButton} 284u $0u 15u 12u "..."
    Pop $R0
    ${NSD_OnClick} $R0 ${ONBROWSE}
    
    IntOp $0 $0 + 17
!macroend
!define LS_CreateDirRequest '!insertmacro _LS_CreateDirRequest ""'
!define LS_CreateDirRequest2 '!insertmacro _LS_CreateDirRequest 2'

!macro DefineOnBrowseFunction DIR_CONTROL ONBROWSE
    Function ${ONBROWSE}
        ${NSD_GetText} ${DIR_CONTROL} $R1
        nsDialogs::SelectFolderDialog "" "$R1"
        Pop $R1
    
        ${If} $R1 != "error"
            ${NSD_SetText} ${DIR_CONTROL} $R1
        ${EndIf}
    FunctionEnd
!macroend

!macro _LS_ConfigWriteSE FILE KEY VALUE RESULT
    ${If} ${VALUE} != ''
        ${StrRep} $0 ${VALUE} '\' '\\'
        ${ConfigWriteS} ${FILE} ${KEY} $0 ${RESULT}
    ${EndIf}
!macroend
!define ConfigWriteSE "!insertmacro _LS_ConfigWriteSE"

!macro _LS_File FILE
    !ifndef SKIP_FILES
        File ${FILE}
    !endif
!macroend
!define SFile "!insertmacro _LS_File" 

!macro _LS_DownloadFileAnyWay SRC DEST
    inetc::get /TOSTACK ${SRC} ${DEST} /END
    Pop $0
    ${ifNot} $0 == "OK"
;        DetailPrint "Trying without current proxy..."
;        inetc::get /POPUP /PROXY /TOSTACK ${SRC} ${DEST} /END ; it's hard to tell why but sometimes only with this options in that order of /POPUP /PROXY it works 
;        Pop $0
;        ${ifNot} $0 == "OK"
            DetailPrint "Downloading failed : $0"
;        ${endIf}
    ${endIf}
!macroend
!define DownloadFileAnyWay "!insertmacro _LS_DownloadFileAnyWay" 

; Dest dir should exist
!macro _LS_DownloadFile SRC LINK DEST
    ${if} ${LINK} == 1
        DetailPrint "Downloading link from ${SRC}"
        ${DownloadFileAnyWay} ${SRC} filelink
        FileOpen $4 filelink r ;read url from downloaded link
        FileRead $4 $1 ; we read until the end of line (including carriage return and new line) and save it to $1
        FileClose $4 ; and close the file
        Delete filelink ; delete temp link file
        DetailPrint "Downloading file from $1"
        ${DownloadFileAnyWay} $1 ${DEST}
    ${else}
        DetailPrint "Downloading file from ${SRC}"
        ${DownloadFileAnyWay} ${SRC} ${DEST} ;download link
    ${endIf}
!macroend
!define DownloadFile "!insertmacro _LS_DownloadFile" 

!macro _Get_File URLFILE URLLINK DESTDIR FILE
    SetOutPath ${DESTDIR} ; need this because DownloadFile doesn't create dir
    !ifndef OFFLINE
        ${DownloadFile} "${URLFILE}" ${URLLINK} "${DESTDIR}\${FILE}"  
    !else
        SetOverwrite on
        ${SFile} "install-bin\${FILE}"
    !endif
!macroend
!define GetFile "!insertmacro _Get_File" 

!macro _Get_Direct_File URLFILE DESTDIR FILE
    ${GetFile} ${URLFILE} 0 ${DESTDIR} ${FILE}
!macroend
!define GetDirectFile "!insertmacro _Get_Direct_File" 

Var runFileName
; for ZIP - PARAMS destination folder, for EXE - command lline params 
!macro _Run_File URLFILE URLLINK FILE EXT INSTNAME PARAMS

    ${GetFile} ${URLFILE} ${URLLINK} ${INSTBINDIR} "${FILE}" 

    StrCpy $runFileName "${INSTBINDIR}\${FILE}"
    ${if} ${EXT} == "zip"
        DetailPrint 'Extracting ${INSTNAME} - $runFileName to ${PARAMS}'
        nsisunz::Unzip "$runFileName" '${PARAMS}'
    ${else}
        DetailPrint "Installing ${INSTNAME}"
        ${if} ${EXT} == "msi"
            nsExec::ExecToLog 'msiexec /i "$runFileName" ${PARAMS}'
        ${else}
            DetailPrint '"$runFileName" ${PARAMS}'
            nsExec::ExecToLog '"$runFileName" ${PARAMS}'
        ${endif}
    ${endif}
    
    Pop $0
    DetailPrint "${INSTNAME} installation returned $0"
        
    Delete "$runFileName"
!macroend
!define RunFile "!insertmacro _Run_File" 

Var isZip
!macro _Run_Link_File FILENAME EXT INSTNAME PARAMS
    ${if} ${EXT} == "zip"
        StrCpy $isZip 1
    ${else}
        StrCpy $isZip 0
    ${endif}    
    ${RunFile} "${DOWNLOADURL}/${FILENAME}.lnk" 1 "${FILENAME}.${EXT}" ${EXT} "${INSTNAME}" '${PARAMS}' 
!macroend
!define RunLinkFile "!insertmacro _Run_Link_File" 
!macro _Run_Direct_File URLFILE FILE EXT INSTNAME PARAMS
    ${RunFile} ${URLFILE} 0 ${FILE} ${EXT} ${INSTNAME} ${PARAMS} 
!macroend
!define RunDirectFile "!insertmacro _Run_Direct_File" 

!macro _RMDir_Silent DIR
    DetailPrint "Deleting ${DIR}"
    SetDetailsPrint none
    RMDir /r "${DIR}" ; will be recreated in next command 
    SetDetailsPrint both
!macroend
!define RMDir_Silent "!insertmacro _RMDir_Silent" 

!macro DisableSection SEC
  !insertmacro UnselectSection ${SEC}
  !insertmacro SetSectionFlag ${SEC} ${SF_RO}
!macroend

!macro HideSection SEC
  !insertmacro UnselectSection ${SEC}
  SectionSetText  ${SEC} ""
!macroend

!macro ExpandSection SEC
  !insertmacro SetSectionFlag ${SEC} ${SF_EXPAND}
!macroend

!macro DefinePreFeatureFunction SEC NAME_PREFIX  
    Function ${NAME_PREFIX}PagePre
      ${IfNot} ${SectionIsSelected} ${SEC}
        Abort
      ${EndIf}
    FunctionEnd
!macroend

!macro CustomDirectoryPage HEADER TEXT_TOP TEXT_DESTINATION DIR_VAR PRE_FUNCTION
    !define MUI_PAGE_HEADER_SUBTEXT ${HEADER}
    !define MUI_DIRECTORYPAGE_VARIABLE ${DIR_VAR}
    !define MUI_DIRECTORYPAGE_TEXT_TOP ${TEXT_TOP}
    !define MUI_DIRECTORYPAGE_TEXT_DESTINATION ${TEXT_DESTINATION}
    !define MUI_PAGE_CUSTOMFUNCTION_PRE ${PRE_FUNCTION}
    !insertmacro MUI_PAGE_DIRECTORY
!macroend

;FileExists is already part of LogicLib, but returns true for directories as well as files
!macro _FileExists2 _a _b _t _f
    !insertmacro _LOGICLIB_TEMP
    StrCpy $_LOGICLIB_TEMP "0"
    StrCmp `${_b}` `` +4 0 ;if path is not blank, continue to next check
    IfFileExists `${_b}` `0` +3 ;if path exists, continue to next check (IfFileExists returns true if this is a directory)
    IfFileExists `${_b}\*.*` +2 0 ;if path is not a directory, continue to confirm exists
    StrCpy $_LOGICLIB_TEMP "1" ;file exists
    ;now we have a definitive value - the file exists or it does not
    StrCmp $_LOGICLIB_TEMP "1" `${_t}` `${_f}`
!macroend
!undef FileExists
!define FileExists `"" FileExists2`

!macro _DirExists _a _b _t _f
    !insertmacro _LOGICLIB_TEMP
    StrCpy $_LOGICLIB_TEMP "0"  
    StrCmp `${_b}` `` +3 0 ;if path is not blank, continue to next check
    IfFileExists `${_b}\*.*` 0 +2 ;if directory exists, continue to confirm exists
    StrCpy $_LOGICLIB_TEMP "1"
    StrCmp $_LOGICLIB_TEMP "1" `${_t}` `${_f}`
!macroend
!define DirExists `"" DirExists`


; Validates that a string name does not use any of the invalid characters: <>:"/\:|?*
Function validateMaybeEmptyNameString
    Pop $R1
    
    ${StrFilter} $R1 "" "" '<>:"/\:|?* ' $R3
    ${if} $R1 != $R3
        Goto error
    ${endIf}
    
    StrCpy $0 "1"
    Goto end
    
    error:
    StrCpy $0 "0"

    end:
FunctionEnd

Function validateNameString
    Pop $R0
    ${if} $R0 == ""
        StrCpy $0 "0"
    ${else}
        Push $R0
        Call validateMaybeEmptyNameString
    ${endIf}
FunctionEnd


;It must match the following criteria:
;  Starts with an alphabet
;  Ends with alphanumeric
;  Allowed special characters are _(underscore), .(dot) and -(hyphen)
;  Minimum lengh: 6 characters & Maximum length: 50 characters
Function validateServiceName
    Pop $R1
    
    StrLen $R2 $R1
    ${if} $R2 < 6
    ${orIf} $R2 > 50
        Goto error
    ${endIf}
    
    ; first letter
    StrCpy $R3 $R1 1
    ${StrFilter} $R3 "2" "" "" $R4
    ${if} $R3 != $R4
        Goto error
    ${endIf}
    
    ; last letter
    StrCpy $R3 $R1 "" -1
    ${StrFilter} $R3 "12" "" "" $R4
    ${if} $R3 != $R4
        Goto error
    ${endIf}
    
    ; all symbols
    ${StrFilter} $R1 "12" "_.-" "" $R3
    ${if} $R1 != $R3
        Goto error
    ${endIf}
    
    StrCpy $0 "1"
    Goto end
    
    error:
    StrCpy $0 "0"

    end:
    
FunctionEnd
