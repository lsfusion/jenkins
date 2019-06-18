!define DEV 1
!define OUT_FILE "x32-dev.exe"
;!define ONLINE 0

Name "lsFusion"

; NSIS 2 - use another nsis version for unicode 
Unicode true

SetCompressor lzma

RequestExecutionLevel user

!define INSTBINDIR "C:\VM"
!define DOWNLOADURL "https://download.lsfusion.org/exe/links"

# Included files
!include Sections.nsh
!include MUI2.nsh
!include LogicLib.nsh
!include TextFunc.nsh

!include StrFunc.nsh
${StrRep}

!include WordFunc.nsh
!include Utils.nsh

!include Versions.nsh

!define TOMCAT_ARCHIVE "apache-tomcat-${TOMCAT_FULL_VERSION}-windows-x64"

Section "My Program"
;    ${GetFile} "" 0 ${INSTBINDIR} "${TOMCAT_ARCHIVE}.zip"
    
;    nsisunz::Unzip "${INSTBINDIR}\${TOMCAT_ARCHIVE}.zip" "C:\VM"
;    Pop $0
;    DetailPrint "installation returned $0"
    StrCpy $INSTDIR "C:"
   ${RunLinkFile} ${TOMCAT_ARCHIVE} "zip" "Tomcat" "$INSTDIR\Client"
SectionEnd
;
;!include "Installer-x32.nsi"


