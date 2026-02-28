 @echo off
    for /f "delims=" %%a in (../.env) do (
        set "%%a"
    )
    mvn clean install spring-boot:run