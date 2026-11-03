@echo off
setlocal
set "CLASSPATH=gradle\wrapper\gradle-wrapper.jar"

if defined JAVA_HOME (
	"%JAVA_HOME%\bin\java.exe" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
) else (
	java -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
)

set "EXIT_CODE=%ERRORLEVEL%"
endlocal & exit /b %EXIT_CODE%