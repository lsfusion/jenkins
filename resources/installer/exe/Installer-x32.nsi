!define ARCH 32

!include Versions-x32.nsh

!define IDEA_EXE "idea.exe"
!define JAVA_INSTALLER_PARAMS '/quiet INSTALLDIR="$javaDir" ADDLOCAL=FeatureMain,FeatureEnvironment,FeatureJarFileRunWith,FeatureJavaHome,FeatureOracleJavaSoft'

!ifndef OUT_FILE
;    !define OUT_FILE "lsfusion-setup-x32.exe"
    !define OUT_FILE "x32.exe"
!endif

!include Installer.nsh
