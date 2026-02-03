#!/usr/bin/env bash
set -euo pipefail

IMAGE_NAME="beauty_salon"
HOST_PORT="8081"
CONTAINER_PORT="8081"

usage() {
  cat <<'USAGE'
Usage: ./run-docker.sh [options]

Options:
  --build           Build the Docker image
  --run             Run the Docker container
  --name <name>     Image name (default: beauty_salon)
  --port <port>     Host port to expose (default: 8080)
  --help            Show this help

Examples:
  ./run-docker.sh --build --run
  ./run-docker.sh --build
  ./run-docker.sh --run --port 9090
USAGE
}

DO_BUILD=false
DO_RUN=false

while [[ $# -gt 0 ]]; do
  case "$1" in
    --build) DO_BUILD=true ;;
    --run) DO_RUN=true ;;
    --name)
      IMAGE_NAME="$2"
      shift
      ;;
    --port)
      HOST_PORT="$2"
      shift
      ;;
    --help)
      usage
      exit 0
      ;;
    *)
      echo "Unknown option: $1" >&2
      usage >&2
      exit 1
      ;;
  esac
  shift
  done

if [[ "$DO_BUILD" == "true" ]]; then
  docker build -t "$IMAGE_NAME" .
fi

if [[ "$DO_RUN" == "true" ]]; then
  ENV_FILE_ARGS=()
  if [[ -f .env ]]; then
    ENV_FILE_ARGS=(--env-file .env)
  fi
  docker run --rm --add-host=host.docker.internal:host-gateway "${ENV_FILE_ARGS[@]}" \
    -p "${HOST_PORT}:${CONTAINER_PORT}" "$IMAGE_NAME"
fi

if [[ "$DO_BUILD" == "false" && "$DO_RUN" == "false" ]]; then
  usage
fi
