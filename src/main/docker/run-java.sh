#!/bin/bash

# Fail on a single failed command
set -eo pipefail

# Default the application dir to the deployment directory
if [ -z "$JAVA_APP_DIR" ]; then
  JAVA_APP_DIR=/deployments
fi

# ==========================================================
# Simplified run script for running arbitrary Java applications
# ==========================================================

# Function to auto-detect the JAR file
auto_detect_jar_file() {
  local dir=$1

  # Check for Quarkus fast-jar package type
  if [ -f "$dir/quarkus-app/quarkus-run.jar" ]; then
    echo "$dir/quarkus-app/quarkus-run.jar"
    return 0
  fi

  local nr_jars=$(ls "$dir"/*.jar 2>/dev/null | grep -v '^original-' | wc -l | tr -d '[[:space:]]')
  if [ "$nr_jars" = "1" ]; then
    ls "$dir"/*.jar | grep -v '^original-'
    return 0
  fi

  echo "ERROR: Neither JAVA_MAIN_CLASS nor JAVA_APP_JAR is set and multiple JARs found in $dir (1 expected)" >&2
  exit 1
}

# Function to combine all Java options
get_java_options() {
  # Since we don't have external scripts, we manually append options
  local opts="${JAVA_OPTS-} ${JAVA_OPTS_APPEND-}"
  echo "$opts" | awk '$1=$1' # Normalize spaces
}

# Function to set the classpath
get_classpath() {
  local cp_path="."
  if [ -z "$JAVA_MAIN_CLASS" ]; then
    if [ -n "$JAVA_APP_JAR" ]; then
      cp_path="$cp_path:$JAVA_APP_JAR"
    fi
    cp_path="$cp_path:$JAVA_APP_DIR/*"
  fi
  echo "$cp_path"
}

# Start the JVM
startup() {
  # Auto-detect the JAR file if JAVA_APP_JAR is not set
  if [ -z "$JAVA_APP_JAR" ]; then
    JAVA_APP_JAR=$(auto_detect_jar_file "$JAVA_APP_DIR")
  fi

  # Build the java execution command
  local args
  if [ -n "$JAVA_MAIN_CLASS" ]; then
    args="$JAVA_MAIN_CLASS"
  else
    args="-jar $JAVA_APP_JAR"
  fi

  local procname="${JAVA_APP_NAME-java}"

  # Log the java command (simplified logging)
  echo "Running: exec -a \"$procname\" java $(get_java_options) -cp \"$(get_classpath)\" $args $*"
  echo "Running in directory: $PWD"

  # Execute the Java command
  exec -a "$procname" java $(get_java_options) -cp "$(get_classpath)" $args "$@"
}

# Fire up the JVM
startup "$@"
