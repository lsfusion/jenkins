!define ARCH 32

!include Versions.nsh

!define IDEA_EXE "idea.exe"

!ifndef OUT_FILE
;    !define OUT_FILE "lsfusion-setup-x32.exe"
    !define OUT_FILE "x32.exe"
!endif

!include Installer.nsh
