# GrafoSupporter

## Panoramica

GrafoSupporter è un'applicazione web full-stack progettata per assistere i grafologi nell'analisi della personalità attraverso la scrittura a mano libera permettendo di ricercare e gestire combinazioni di segni grafologici a partire dai valori misurati relativi ad ogni segno individuato nello scritto.

L'applicazione è composta da un backend basato su **Spring Boot** e un frontend **Angular**, ed è completamente containerizzata con **Docker**.

**Link all'applicazione live:** coming soon

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

- Java 17+
- Maven 3.8+
- Node.js 20+
- Docker e Docker Compose

### 1. Configurazione dei Segreti

Questo progetto richiede delle credenziali per l'autenticazione Google e per la firma dei token JWT.

1.  Crea un file `.env` nella directory principale del progetto.
2.  Popola il file con le seguenti variabili (sostituendo i valori di esempio):

    ```
    GOOGLE_CLIENT_ID=tuo_client_id
    GOOGLE_CLIENT_SECRET=tuo_secret

    DB_USER=user
    DB_PASS=pass
    SPRING_DATASOURCE_URL=tuo_url_DB
    ```

### 2. Avvio con Docker Compose (Metodo Consigliato)

Il modo più semplice per avviare l'intera applicazione (backend, frontend, database) è tramite Docker Compose.

```bash
# Dalla root del progetto
docker-compose up --build
```

L'applicazione sarà accessibile ai seguenti indirizzi:

- **Frontend**: `http://localhost:4200`
- **Backend**: `http://localhost:8080`

### 3. Avvio Manuale (per lo Sviluppo)

Se preferisci avviare i servizi manualmente:

**Backend (Spring Boot) su windows:**

```bash
cd backend
./start-dev.bat # carica le variabili d'ambiente da .env
```

**Backend (Spring Boot) su Linux:**

```bash
cd backend
chmod +x start-dev.sh
./start-dev.sh # carica le variabili d'ambiente da .env
```

**Frontend (Angular):**

```bash
# Naviga nella cartella del frontend
cd frontend

# Installa le dipendenze
npm install

# Avvia il server di sviluppo
npm start
```

---

\_Autore: Michele Nesler
\_LinkedIn: www.linkedin.com/in/michele-nesler-88a743198

Copyright (c) 2025 Michele Nesler. Tutti i diritti riservati.

Questo progetto e tutto il suo contenuto sono di proprietà esclusiva di Michele Nesler.
L'utilizzo, la riproduzione, la distribuzione o la modifica di questo progetto, in tutto o in parte,
sono strettamente proibiti senza il consenso scritto ed esplicito del titolare del copyright.
