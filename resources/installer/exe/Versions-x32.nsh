; it is stated that 2021.1 (2021.1.3) is the last version for x32 systems (https://blog.jetbrains.com/idea/2021/04/end-of-support-for-32-bit-operating-systems-in-intellij-based-ides/)
; however we were unable to start it on Windows 7 x32
!define IDEA_MAJORVERSION 2020.3
!define IDEA_VERSION ${IDEA_MAJORVERSION}.4

!define JDK_MAJORVERSION 11 ; used in version compare and idea SDK
!define JDK_VERSION ${JDK_MAJORVERSION}.0.16 ; log, idea SDK
;!define JDK_FOLDER "$ProgramFiles${ARCH}\ojdkbuild\java-${JDK_MAJORVERSION}-openjdk-${JDK_DISTRVERSION}"

; last for x32
!define PG_MAJORVERSION 10
!define PG_VERSION ${PG_MAJORVERSION}.22

!define TOMCAT_MAJOR_VERSION 9
!define TOMCAT_VERSION ${TOMCAT_MAJOR_VERSION}.0.67

; last for x32
!define JASPER_VERSION 6.8.0

!define ANT_VERSION 1.10.9

# LSFUSION_MAJOR_VERSION, LSFUSION_VERSION and VI_LSFUSION_VERSION will be added automatically before building installers