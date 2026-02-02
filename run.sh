#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MVN="${SCRIPT_DIR}/mvnw"

show_help() {
  cat <<'EOF'
Usage: run.sh [options]

Options:
  -c, --compile   Compile the project (mvn compile)
  -t, --test      Run tests (mvn test)
  -b, --build     Build the project (mvn clean package)
  -r, --run       Run the app (mvn spring-boot:run)
  -h, --help      Show this help

If no options are provided, the app is run (same as --run).
When multiple options are provided, they run in this order:
compile, test, build, run.
EOF
}

do_compile=false
do_test=false
do_build=false
do_run=false

if [[ $# -eq 0 ]]; then
  do_run=true
else
  while [[ $# -gt 0 ]]; do
    case "$1" in
      -c|--compile) do_compile=true ;;
      -t|--test) do_test=true ;;
      -b|--build) do_build=true ;;
      -r|--run) do_run=true ;;
      -h|--help)
        show_help
        exit 0
        ;;
      *)
        echo "Unknown option: $1" >&2
        echo "" >&2
        show_help >&2
        exit 1
        ;;
    esac
    shift
  done
fi

if [[ "$do_compile" == "true" ]]; then
  "$MVN" compile
fi

if [[ "$do_test" == "true" ]]; then
  "$MVN" test
fi

if [[ "$do_build" == "true" ]]; then
  "$MVN" clean package
fi

if [[ "$do_run" == "true" ]]; then
  "$MVN" spring-boot:run
fi
