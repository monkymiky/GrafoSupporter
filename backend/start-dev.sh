#!/bin/bash

while IFS= read -r line; do
    [[ $line =~ ^[[:space:]]*$ || $line =~ ^# ]] && continue
    line=$(echo "$line" | sed 's/\r$//')
    export "$line"
done < ../.env

mvn clean install spring-boot:run