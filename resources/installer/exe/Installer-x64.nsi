!define ARCH 64

!include Versions-x64.nsh

!define IDEA_EXE "idea64.exe"
!define JAVA_INSTALLER_PARAMS '/quiet INSTALLDIR="$javaDir" ADDLOCAL=jdk,jdk_registry_standard,jdk_env_path,jdk_env_java_home,jdk_env_vendor_java_home,jdk_registry_jar,jmc,jmc_env'

!ifndef OUT_FILE
;    !define OUT_FILE "lsfusion-setup-x64.exe"
    !define OUT_FILE "x64.exe"
!endif

!include Installer.nsh
