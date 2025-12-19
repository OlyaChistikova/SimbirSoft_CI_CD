#!/bin/bash
# wait-for-it.sh

# Скрипт ожидает, пока указанный хост и порт не станут доступны,
# а затем выполняет команду, указанную после аргумента --

TIMEOUT=15
QUIET=0
WAITFORIT_HOST=
WAITFORIT_PORT=
CMD=

usage() {
    echo "Usage: $0 host:port [-s] [-t timeout] [-- command args]"
    echo "    -h HOST | --host=HOST       Host or IP for the service to wait on"
    echo "    -p PORT | --port=PORT       Port for the service to wait on"
    echo "    -s | --strict               Only execute command if the proxy starts successfully"
    echo "    -q | --quiet                Don't output any status messages"
    echo "    -t TIMEOUT | --timeout=TIMEOUT  Timeout in seconds, zero for unlimited (default 15s)"
    echo "    -- COMMAND ARGS             Execute command after the service is ready"
    exit 1
}

wait_for() {
    if [ "$TIMEOUT" -gt 0 ]; then
        echo "Waiting for $WAITFORIT_HOST:$WAITFORIT_PORT for $TIMEOUT seconds..."
    else
        echo "Waiting for $WAITFORIT_HOST:$WAITFORIT_PORT indefinitely..."
    fi

    start_ts=$(date +%s)
    while :
    do
        if [ "$QUIET" -eq 1 ]; then
            nc -z "$WAITFORIT_HOST" "$WAITFORIT_PORT"
            result=$?
        else
            nc -z "$WAITFORIT_HOST" "$WAITFORIT_PORT"
            result=$?
        fi
        if [ $result -eq 0 ]; then
            end_ts=$(date +%s)
            echo "$WAITFORIT_HOST:$WAITFORIT_PORT is available after $((end_ts - start_ts)) seconds"
            break
        fi
        sleep 1
        current_ts=$(date +%s)
        if [ "$TIMEOUT" -gt 0 ] && [ "$((current_ts - start_ts))" -ge "$TIMEOUT" ]; then
            echo "Timeout occurred after $((current_ts - start_ts)) seconds waiting for $WAITFORIT_HOST:$WAITFORIT_PORT"
            exit 1
        fi
    done
    return 0
}

while [ $# -gt 0 ]
do
    case "$1" in
        *:* )
        WAITFORIT_HOST=$(echo "$1" | cut -d: -f1)
        WAITFORIT_PORT=$(echo "$1" | cut -d: -f2)
        shift 1
        ;;
        -h | --host)
        WAITFORIT_HOST="$2"
        shift 2
        ;;
        -p | --port)
        WAITFORIT_PORT="$2"
        shift 2
        ;;
        -t | --timeout)
        TIMEOUT="$2"
        shift 2
        ;;
        -q | --quiet)
        QUIET=1
        shift 1
        ;;
        --)
        shift 1
        CMD="$@"
        break
        ;;
        *)
        usage
        ;;
    esac
done

if [ "$WAITFORIT_HOST" = "" -o "$WAITFORIT_PORT" = "" ]; then
    echo "Error: you need to provide a host and port to wait for."
    usage
fi

wait_for

if [ "$CMD" != "" ]; then
    exec $CMD
fi
