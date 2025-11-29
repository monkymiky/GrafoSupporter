# GrafoSupporter

## Panoramica

GrafoSupporter è un'applicazione web full-stack progettata per assistere i grafologi nell'analisi della personalità attraverso la scrittura a mano libera permettendo di ricercare e gestire combinazioni di segni grafologici a partire dai valori misurati relativi ad ogni segno individuato nello scritto.

L'applicazione è composta da un backend basato su **Spring Boot** e un frontend **Angular**, ed è completamente containerizzata con **Docker**.

**Link all'applicazione live:** https://grafosupporter.net.in/

## Stack Tecnologico

### Backend

- Java 17
- Spring Boot 3
- Spring Security (OAuth2 + JWT)
- Spring Data JPA / Hibernate
- PostgreSQL

### Frontend

- Angular 17
- TypeScript
- Bootstrap

### DevOps

- Docker & Docker Compose
- GitHub Actions (CI/CD)
- Hosting su VPS (Hetzner) con NGINX come reverse proxy

## Features Principali

- Funzionalità di ricerca avanzata per le combinazioni di segni (Specification).
- Autenticazione sicura tramite Google (OAuth2).
- Gestione della sessione tramite token JWT stateless.
- API rest-full per comunicare con il Backend.
- CRUD (Create, Read, Update, Delete) per le combinazioni personalizzate degli utenti.
- Upload di immagini associate alle combinazioni.
- Responsività dell UI

## Come Avviare il Progetto in Locale

### Prerequisiti

- Docker e Docker Compose

### 1. Configurazione dei Segreti

Questo progetto richiede delle credenziali per l'autenticazione Google e per la firma dei token JWT.

1.  Crea un file `.env` nella directory principale del progetto.
2.  Popola il file con le seguenti variabili (sostituendo i valori di esempio):

    ```
    GOOGLE_CLIENT_ID=tuo_client_id
    GOOGLE_CLIENT_SECRET=tuo_secret
    JWT_SECRET=tuo_secret

    APP_BASE_URL=http://localhost:8080
    FRONTEND_BASE_URL=http://localhost:4200

    DB_USER=user
    DB_PASS=pass
    SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/databaseLocale
    ```

### 2. Avvio con Docker Compose

Tramite Docker Compose si avvia l'intera applicazione (backend, frontend, database) con un unico comando.

```bash
# Dalla root del progetto
docker-compose up --build
```

### 2 Avvio per lo sviluppo con live reload

```bash
# Dalla root del progetto
docker-compose -f docker-compose.dev.yml up --build
```

L'applicazione sarà accessibile ai seguenti indirizzi:

- **Frontend**: `http://localhost:4200`
- **Backend**: `http://localhost:8080`

---

Autore: Michele Nesler
LinkedIn: www.linkedin.com/in/michele-nesler-88a743198

Copyright (c) 2025 Michele Nesler. Tutti i diritti riservati.

Questo progetto e tutto il suo contenuto sono di proprietà esclusiva di Michele Nesler.
L'utilizzo, la riproduzione, la distribuzione o la modifica di questo progetto, in tutto o in parte,
sono strettamente proibiti senza il consenso scritto ed esplicito del titolare del copyright.
