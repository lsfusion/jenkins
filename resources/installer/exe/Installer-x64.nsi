!define ARCH 64

!include Versions.nsh

!define IDEA_EXE "idea64.exe"

!ifndef OUT_FILE
;    !define OUT_FILE "lsfusion-setup-x64.exe"
    !define OUT_FILE "x64.exe"
!endif

!include Installer.nsh
