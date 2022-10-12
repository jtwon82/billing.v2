
for /F %%I in ('dir /b /o:-d target\*.jar') do ( set file=%%I & goto :step1 )

:step1

for /F %%I in ('dir /b /o:-d C:\workset\1sender\*.log') do ( set log=%%I & goto :step2 )

:step2

rem copy C:\workset\1sender\%log% logs\topicFile

java -jar target\%file%

pause