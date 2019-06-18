!define ARCH 32

!include Versions.nsh

!define JAVA_INSTALLER "java-${JDK_VERSION}-x86"
!define PG_INSTALLER "postgresql-${PG_VERSION}-x86"
!define IDEA_INSTALLER "ideaIC-${IDEA_VERSION}"
!define IDEA_PLUGIN "lsfusion-idea-plugin"
!define TOMCAT_ARCHIVE "apache-tomcat-${TOMCAT_VERSION}-x86"
!define ANT_ARCHIVE "apache-ant-${ANT_VERSION}"
!define JASPER_INSTALLER "jasper-${JASPER_VERSION}-x86"

!define IDEA_EXE "idea.exe"

!ifndef OUT_FILE
;    !define OUT_FILE "lsfusion-setup-x32.exe"
    !define OUT_FILE "x32.exe"
!endif

!include Installer.nsh
