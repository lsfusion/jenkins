!define ARCH 32

!include Versions.nsh

!define JAVA_INSTALLER "jdk-${JDK_DISTRVERSION}-windows-i586"
!define PG_INSTALLER "postgresql-${PG_DISTRVERSION}-windows"
!define IDEA_INSTALLER "ideaIC-${IDEA_VERSION}"
!define IDEA_PLUGIN "lsfusion-idea-plugin"
!define TOMCAT_ARCHIVE "apache-tomcat-${TOMCAT_FULL_VERSION}-windows-x86"
!define ANT_ARCHIVE "apache-ant-${ANT_VERSION}-bin"
!define JASPER_INSTALLER "TIB_js-studiocomm_${JASPER_VERSION}_windows_x86"

!define IDEA_EXE "idea.exe"

!ifndef OUT_FILE
;    !define OUT_FILE "lsfusion-setup-x32.exe"
    !define OUT_FILE "x32.exe"
!endif

!include Installer.nsh
