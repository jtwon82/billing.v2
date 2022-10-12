
for /F %%I in ('dir /b /o:-d *.jar') do ( set file=%%I & goto :end )

:end

java -jar %file%
