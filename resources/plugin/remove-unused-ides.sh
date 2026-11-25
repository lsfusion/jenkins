#!/bin/bash
# Removes from the Gradle cache the IntelliJ Platform IDEs that the plugin-idea build no longer needs: the archives Gradle
# downloaded and their extractions. The IDEs it needs are listed in the file $1, the output of the
# printIntellijPlatformArtifacts task (see intellij-platform-artifacts.gradle); of each <group>:<name> listed there, the
# versions not listed are removed. Also removes the plugins that the plugin verifier downloaded for the IDEs it checked
# against, once they have not been read for two weeks.
set -euo pipefail
shopt -s nullglob

artifacts=$1
caches=${GRADLE_USER_HOME:-$HOME/.gradle}/caches
verifierPlugins=$HOME/.pluginVerifier/loaded-plugins

# a listing without either of them is not what the build is going to use
if ! grep -q '^platform ' "$artifacts" || ! grep -q '^verifier ' "$artifacts"; then
    echo "The IDEs the build needs are not listed, nothing is removed. The listing:"
    cat "$artifacts"
    exit 0
fi

declare -A keep modules
while read -r coordinates; do
    # the parts become paths in the cache
    if [[ ! $coordinates =~ ^[A-Za-z0-9._-]+:[A-Za-z0-9._-]+:[A-Za-z0-9._-]+$ ]]; then
        echo "Unexpected IDE $coordinates, nothing is removed."
        exit 0
    fi
    IFS=: read -r group name version <<< "$coordinates"
    keep[$group/$name/$version]=1
    modules[$group/$name]=1
done < <(sed -n -E 's/^(platform|verifier) //p' "$artifacts")
echo "The IDEs the build needs: $(sed -n -E 's/^(platform|verifier) //p' "$artifacts" | tr '\n' ' ')"

echo "Free space before: $(df -h --output=avail "$caches" | tail -n 1 | tr -d ' ')"
for module in "${!modules[@]}"; do
    name=${module#*/}
    for dir in "$caches/modules-2/files-2.1/$module"/*/; do
        version=$(basename "$dir")
        if [[ -z ${keep[$module/$version]:-} ]]; then
            echo "Removing $dir"
            rm -rf -- "$dir"
        fi
    done
    # an extraction is the only output of its transform: transforms/<hash>/transformed/<name>-<version>
    for dir in "$caches"/*/transforms/*/transformed/"$name"-*/; do
        [[ -f $dir/build.txt ]] || continue
        version=$(basename "$dir")
        version=${version#"$name"-}
        [[ -z ${keep[$module/$version]:-} ]] || continue
        transform=$(dirname "$(dirname "$dir")")
        outputs=("$transform"/transformed/*)
        if (( ${#outputs[@]} == 1 )); then
            echo "Removing $transform ($name-$version)"
            rm -rf -- "$transform"
        else
            echo "Leaving $transform: it has other outputs than $name-$version"
        fi
    done
done

if [[ -d $verifierPlugins ]]; then
    find "$verifierPlugins" -maxdepth 1 -type f -name '*.zip' -atime +14 -print -delete | sed 's/^/Removed /'
fi
echo "Free space after: $(df -h --output=avail "$caches" | tail -n 1 | tr -d ' ')"
