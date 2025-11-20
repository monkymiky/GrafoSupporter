# Cerca il file .env nella directory backend o nella root del progetto
$envFile = $null
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$backendDir = Split-Path -Parent $scriptDir
$rootDir = Split-Path -Parent $backendDir

if (Test-Path (Join-Path $rootDir ".env")) {
    $envFile = Join-Path $rootDir ".env"
} elseif (Test-Path (Join-Path $backendDir ".env")) {
    $envFile = Join-Path $backendDir ".env"
}

# Leggi le variabili dal file .env se esiste
if ($envFile) {
    Write-Host "Lettura variabili da: $envFile" -ForegroundColor Cyan
    Get-Content $envFile | ForEach-Object {
        if ($_ -match '^\s*([^#][^=]+)=(.*)$') {
            $key = $matches[1].Trim()
            $value = $matches[2].Trim()
            if ($key -eq "DB_USER" -and -not $env:DB_USER) {
                $env:DB_USER = $value
            }
            if ($key -eq "DB_PASS" -and -not $env:DB_PASS) {
                $env:DB_PASS = $value
            }
        }
    }
}

# Usa valori di default per lo sviluppo locale se non definiti
if (-not $env:DB_USER) {
    $env:DB_USER = "user"
    Write-Host "DB_USER non definito, uso valore di default: user" -ForegroundColor Yellow
}
if (-not $env:DB_PASS) {
    $env:DB_PASS = "pass"
    Write-Host "DB_PASS non definito, uso valore di default: pass" -ForegroundColor Yellow
}

# Rimuove il container esistente se presente
if (docker ps -a --filter "name=pg" --format "{{.Names}}" | Select-String -Pattern "^pg$") {
    Write-Host "Rimozione container esistente 'pg'..." -ForegroundColor Yellow
    docker rm -f pg
}

docker run --name pg `
-e POSTGRES_USER=$env:DB_USER `
-e POSTGRES_PASSWORD=$env:DB_PASS `
-e POSTGRES_DB=grafosupporterDB `
-p 127.0.0.1:5432:5432 `
-v postgres_data:/var/lib/postgresql/data `
-d postgres:16

if ($LASTEXITCODE -eq 0) {
    Write-Host "Container PostgreSQL avviato con successo!" -ForegroundColor Green
    Write-Host "Nome: pg" -ForegroundColor Cyan
    Write-Host "Porta: 127.0.0.1:5432" -ForegroundColor Cyan
    Write-Host "Database: grafosupporterDB" -ForegroundColor Cyan
    Write-Host "Utente: $env:DB_USER" -ForegroundColor Cyan
} else {
    Write-Host "Errore durante l'avvio del container." -ForegroundColor Red
    docker logs pg
}
