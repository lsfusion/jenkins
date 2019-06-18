!define ARCH 64

!include Versions.nsh

!define JAVA_INSTALLER "java-${JDK_MAJORVERSION}-openjdk-${JDK_DISTRVERSION}.b04.ojdkbuild.windows.x86_64"
!define PG_INSTALLER "postgresql-${PG_DISTRVERSION}-windows-x64"
!define IDEA_INSTALLER "ideaIC-${IDEA_VERSION}"
!define IDEA_PLUGIN "lsfusion-idea-plugin"
!define TOMCAT_ARCHIVE "apache-tomcat-${TOMCAT_FULL_VERSION}-windows-x64"
!define ANT_ARCHIVE "apache-ant-${ANT_VERSION}-bin"
!define JASPER_INSTALLER "TIB_js-studiocomm_${JASPER_VERSION}_windows_x86_64"

!define IDEA_EXE "idea64.exe"

!ifndef OUT_FILE
;    !define OUT_FILE "lsfusion-setup-x64.exe"
    !define OUT_FILE "x64.exe"
!endif

!include Installer.nsh
