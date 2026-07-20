@echo off
rem Migrates the dockerized PostgreSQL database to the major version set in compose.yaml.
rem Put next to compose.yaml (together with pg-migrate-container.sh) and run.

if not exist compose.yaml echo compose.yaml not found, run from the folder with compose.yaml & goto fail
if not exist docker-db echo docker-db folder not found & goto fail
if not exist pg-migrate-container.sh echo pg-migrate-container.sh not found & goto fail

set SKIP_BACKUP=
if exist docker-db-backup (
    echo docker-db-backup already exists - probably left by a previous migration attempt.
    echo If it is old and unrelated, press N, remove it manually and rerun.
    choice /C YN /M "Reuse it as the backup and continue"
    if errorlevel 2 goto fail
    set SKIP_BACKUP=1
)

docker compose down || goto fail

if not defined SKIP_BACKUP (
    echo Backing up docker-db to docker-db-backup...
    robocopy docker-db docker-db-backup /E /NFL /NDL /NJH /NJS /NP >nul
    if errorlevel 8 goto fail
)

copy /Y pg-migrate-container.sh docker-db\ >nul

docker compose run --rm db bash -c "S=/var/lib/postgresql/pg-migrate-container.sh; [ -f $S ] || S=/var/lib/postgresql/data/pg-migrate-container.sh; tr -d '\r' < $S > /tmp/pg-migrate.sh && bash /tmp/pg-migrate.sh"
if errorlevel 1 del docker-db\pg-migrate-container.sh & goto fail

del docker-db\pg-migrate-container.sh
docker compose up -d || goto fail

echo.
echo Migration finished. After verifying the system works, remove docker-db-backup and the old cluster folder (see the message above).
pause
exit /b 0

:fail
echo.
echo MIGRATION FAILED. The database folder is untouched or backed up in docker-db-backup.
pause
exit /b 1
