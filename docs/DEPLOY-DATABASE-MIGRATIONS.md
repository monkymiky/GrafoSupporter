# Deploy e migrazioni database (Flyway)

## Obiettivo

Lo schema del database non è più gestito da Hibernate (`ddl-auto=update`) ma da **Flyway**: script SQL versionati in `backend/src/main/resources/db/migration/`. Hibernate usa solo `ddl-auto=validate` per verificare che le entità JPA corrispondano allo schema.

Il database esistente può essere sovrascritto (è vuoto).

---

## Applicare le modifiche sul server

1. **Opzionale – ripartire da zero:** per ricreare un DB pulito, elimina i container e i volumi con lo **stesso** compose file usato per l’avvio:
   - Ambiente **produzione** (docker-compose.yml): `docker compose down -v` (rimuove i volumi `postgres_data` e `uploads`).
   - Ambiente **sviluppo** (docker-compose.dev.yml): `docker compose -f docker-compose.dev.yml down -v` (rimuove `postgres_data_dev` e `uploads_dev`). Se usi solo `docker compose down -v` i volumi del dev non vengono toccati.
   In alternativa: `docker volume ls` per vedere i nomi, poi `docker volume rm <nome_volume>` per rimuovere a mano il volume del DB.

2. **Deploy come di consueto:**
   - `docker compose pull`
   - `docker compose up -d`

3. Al primo avvio del backend:
   - **DB vuoto:** Flyway crea `flyway_schema_history` ed esegue `V1__initial_schema.sql`, creando tutte le tabelle.
   - **DB già popolato senza Flyway** (es. creato in passato con `ddl-auto=update`): con `baseline-on-migrate=true` Flyway crea la tabella di storia e considera la versione 1 già applicata (baseline), quindi non riesegue V1; lo schema esistente resta com’è.
   In entrambi i casi Hibernate con `validate` verifica la corrispondenza con le entità. Non serve eseguire comandi Flyway a mano: tutto avviene nel container.

---

## Se l’applicazione non parte (validate fallisce)

Significa che lo schema nel DB non coincide con le entità. Possibili passi:

- Controllare i log del backend per il messaggio di errore di Hibernate validate.
- Allineare il DB allo schema definito in `V1__initial_schema.sql` (modifiche manuali o script correttivo) oppure correggere entità/migrazione.

Se il DB può essere resettato, la via più semplice è eliminare il volume del DB e rifare il deploy (vedi passo 1 sopra).

---

## Modifiche allo schema in futuro

- Aggiungere nuovi script in `backend/src/main/resources/db/migration/` con il naming Flyway, ad esempio:  
  `V2__descrizione_modifica.sql`, `V3__altro_campo.sql`.
- **Non modificare** uno script già applicato (Flyway tiene traccia con checksum). Per correzioni usare una nuova migrazione (es. `V4__fix_xyz.sql`).
- Dopo le modifiche: build, push immagine, deploy come al solito; Flyway applicherà le nuove migrazioni all’avvio del backend.
