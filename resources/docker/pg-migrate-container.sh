#!/bin/bash
# Migrates the PostgreSQL cluster on the /var/lib/postgresql volume to the major
# version of this image ($PG_MAJOR). Runs INSIDE the postgres container, started
# by pg-migrate.bat / pg-migrate.sh:
#   docker compose run --rm db bash /var/lib/postgresql/pg-migrate-container.sh
set -e

ROOT=/var/lib/postgresql
NEW=$PG_MAJOR
NEW_DATA=$ROOT/$NEW/docker
SUPERUSER=${POSTGRES_USER:-postgres}

if [ -s "$ROOT/data/PG_VERSION" ]; then
    echo "ERROR: database found in $ROOT/data - compose.yaml still mounts the old path."
    echo "Change the db volume to './docker-db:/var/lib/postgresql' and rerun."
    exit 1
fi

if ls "$ROOT"/*/data.partial >/dev/null 2>&1; then
    echo "ERROR: an interrupted migration detected ($ROOT/*/data.partial)."
    echo "Restore docker-db from docker-db-backup and rerun."
    exit 1
fi

if [ -s "$NEW_DATA/PG_VERSION" ]; then
    echo "Database is already on PostgreSQL $NEW, nothing to do."
    exit 0
fi

# old cluster is either at the volume root (pre-18 layout) or in a <version> subfolder
if [ -s "$ROOT/PG_VERSION" ]; then
    OLD=$(cat "$ROOT/PG_VERSION")
    OLD_DATA=$ROOT/$OLD/data
    echo "Pre-18 layout detected, moving the cluster to $OLD_DATA..."
    PARTIAL=$ROOT/$OLD/data.partial
    mkdir -p "$PARTIAL"
    find "$ROOT" -mindepth 1 -maxdepth 1 \
        ! -name "$OLD" ! -name 'pg-migrate-container.sh' ! -name 'lost+found' \
        -exec mv {} "$PARTIAL/" \;
    mv "$PARTIAL" "$OLD_DATA"
else
    OLD_DATA=$(for d in "$ROOT"/*/docker "$ROOT"/*/data; do
        [ -s "$d/PG_VERSION" ] && [ "$(cat "$d/PG_VERSION")" != "$NEW" ] && echo "$d"; done; true)
    if [ -z "$OLD_DATA" ]; then
        echo "No existing database found, nothing to migrate."
        exit 0
    fi
    if [ "$(echo "$OLD_DATA" | wc -l)" -gt 1 ]; then
        echo "ERROR: several old clusters found:"
        echo "$OLD_DATA"
        echo "Remove the obsolete ones and rerun."
        exit 1
    fi
    OLD=$(cat "$OLD_DATA/PG_VERSION")
fi

echo "Migrating PostgreSQL $OLD -> $NEW..."

apt-get update -qq
apt-get install -y -qq postgresql-$OLD >/dev/null

OLD_BIN=/usr/lib/postgresql/$OLD/bin
NEW_BIN=/usr/lib/postgresql/$NEW/bin

# the new cluster must match the old one's data checksums setting
if "$OLD_BIN/pg_controldata" "$OLD_DATA" | grep -Eq 'checksum version:\s+0$'; then
    CHECKSUMS=--no-data-checksums
else
    CHECKSUMS=--data-checksums
fi

mkdir -p "$ROOT/$NEW"
chown postgres:postgres "$ROOT" "$ROOT/$NEW" "$OLD_DATA"
chmod 700 "$OLD_DATA" 2>/dev/null || true

# if this phase fails, remove the half-created new cluster so that a rerun
# does not mistake it for a completed migration
trap 'rm -rf "$NEW_DATA"' ERR

gosu postgres "$NEW_BIN/initdb" $CHECKSUMS -U "$SUPERUSER" -D "$NEW_DATA" >/dev/null

# settings the image entrypoint applies on first initialization; harmless during
# pg_upgrade, which starts the servers with TCP disabled
cp "$OLD_DATA/pg_hba.conf" "$NEW_DATA/"
echo "listen_addresses = '*'" >> "$NEW_DATA/postgresql.conf"

cd "$ROOT"
UPGRADE="$NEW_BIN/pg_upgrade -b $OLD_BIN -B $NEW_BIN -d $OLD_DATA -D $NEW_DATA -U $SUPERUSER"
if gosu postgres $UPGRADE --link --check; then
    MODE=--link
elif gosu postgres $UPGRADE --check; then
    MODE=  # hard links not supported on this filesystem, fall back to copying
else
    rm -rf "$NEW_DATA"
    exit 1
fi
gosu postgres $UPGRADE $MODE

trap - ERR

echo
echo "Done. The old cluster is left in $OLD_DATA - remove it after verifying the system works."
echo "If you customized postgresql.conf, re-apply the changes to $NEW_DATA/postgresql.conf."
