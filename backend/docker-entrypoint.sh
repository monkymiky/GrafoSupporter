#!/bin/bash
set -e

# Assicura che la directory target esista
mkdir -p /app/target/classes

# Funzione per ricompilare quando i file cambiano
watch_and_compile() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] File watcher avviato - monitoraggio file Java ogni 2 secondi..."
    LAST_CHECK=$(date +%s)
    
    while true; do
        sleep 2
        CURRENT_TIME=$(date +%s)
        
        # Trova tutti i file .java e controlla se sono stati modificati dopo l'ultimo check
        MODIFIED_FILES=$(find /app/src -name "*.java" -type f -newermt "@${LAST_CHECK}" 2>/dev/null | head -1)
        
        if [ -n "$MODIFIED_FILES" ]; then
            echo "[$(date +'%Y-%m-%d %H:%M:%S')] ⚡ File modificati rilevati, ricompilazione in corso..."
            if mvn compile -q; then
                echo "[$(date +'%Y-%m-%d %H:%M:%S')] ✅ Ricompilazione completata con successo"
                # Trigger per DevTools - tocca un file in target/classes per forzare il reload
                touch /app/target/classes/.reloadtrigger 2>/dev/null || true
            else
                echo "[$(date +'%Y-%m-%d %H:%M:%S')] ⚠️  Errore durante la ricompilazione (controlla i log sopra)"
            fi
        fi
        
        LAST_CHECK=$CURRENT_TIME
    done
}

# Avvia il watcher in background
watch_and_compile &

# Avvia Spring Boot
echo "[$(date +'%Y-%m-%d %H:%M:%S')] Avvio applicazione Spring Boot..."
exec mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
