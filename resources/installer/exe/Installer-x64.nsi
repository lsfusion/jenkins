!define ARCH 64

!include Versions-x64.nsh

!define IDEA_EXE "idea64.exe"
!define JAVA_INSTALLER_PARAMS '/quiet INSTALLDIR="$javaDir" ADDLOCAL=FeatureMain,FeatureEnvironment,FeatureJarFileRunWith,FeatureJavaHome,FeatureOracleJavaSoft'

!ifndef OUT_FILE
;    !define OUT_FILE "lsfusion-setup-x64.exe"
    !define OUT_FILE "x64.exe"
!endif

!include Installer.nsh
