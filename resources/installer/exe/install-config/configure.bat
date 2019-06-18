set JAVA_HOME=JAVA
call "..\install-bin\%1\bin\ant" -verbose -f configure.xml %2 >>configure.log 2>&1
