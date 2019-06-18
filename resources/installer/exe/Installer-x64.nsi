!define ARCH 64

!include Versions.nsh

!define JAVA_INSTALLER "java-${JDK_VERSION}"
!define PG_INSTALLER "postgresql-${PG_VERSION}"
!define IDEA_INSTALLER "ideaIC-${IDEA_VERSION}"
!define IDEA_PLUGIN "lsfusion-idea-plugin"
!define TOMCAT_ARCHIVE "apache-tomcat-${TOMCAT_VERSION}"
!define ANT_ARCHIVE "apache-ant-${ANT_VERSION}"
!define JASPER_INSTALLER "jasper-${JASPER_VERSION}"

!define IDEA_EXE "idea64.exe"

!ifndef OUT_FILE
;    !define OUT_FILE "lsfusion-setup-x64.exe"
    !define OUT_FILE "x64.exe"
!endif

!include Installer.nsh
