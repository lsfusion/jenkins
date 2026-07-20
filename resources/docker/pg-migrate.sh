#!/bin/bash
# Migrates the dockerized PostgreSQL database to the major version set in compose.yaml.
# Put next to compose.yaml (together with pg-migrate-container.sh) and run as root.
set -e
cd "$(dirname "$0")"

[ -f compose.yaml ] || { echo "compose.yaml not found, run from the folder with compose.yaml"; exit 1; }
[ -d docker-db ] || { echo "docker-db folder not found"; exit 1; }
[ -f pg-migrate-container.sh ] || { echo "pg-migrate-container.sh not found"; exit 1; }
[ "$(id -u)" -eq 0 ] || { echo "Run as root (sudo): docker-db is owned by the postgres container user."; exit 1; }

SKIP_BACKUP=
if [ -e docker-db-backup ]; then
    echo "docker-db-backup already exists - probably left by a previous migration attempt."
    echo "If it is old and unrelated, answer n, remove it manually and rerun."
    read -r -p "Reuse it as the backup and continue? [y/N] " answer
    case $answer in y|Y) SKIP_BACKUP=1 ;; *) exit 1 ;; esac
fi

docker compose down

if [ -z "$SKIP_BACKUP" ]; then
    echo "Backing up docker-db to docker-db-backup..."
    cp -a docker-db docker-db-backup
fi

cp -f pg-migrate-container.sh docker-db/

if docker compose run --rm db bash -c "S=/var/lib/postgresql/pg-migrate-container.sh; [ -f \$S ] || S=/var/lib/postgresql/data/pg-migrate-container.sh; tr -d '\r' < \$S > /tmp/pg-migrate.sh && bash /tmp/pg-migrate.sh"; then
    rm -f docker-db/pg-migrate-container.sh
    docker compose up -d
    echo
    echo "Migration finished. After verifying the system works, remove docker-db-backup and the old cluster folder (see the message above)."
else
    rm -f docker-db/pg-migrate-container.sh
    echo
    echo "MIGRATION FAILED. The database folder is backed up in docker-db-backup."
    exit 1
fi
