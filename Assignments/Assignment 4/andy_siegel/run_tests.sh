#!/bin/bash
DIR="$(cd "$(dirname "$0")" && pwd)"
TESTS="$DIR/tests"
PASS=0
FAIL=0
TMPXML="$(mktemp /tmp/luca_ast_XXXXXX.xml)"
TMPGV="$(mktemp /tmp/luca_ast_XXXXXX.gv)"

for luc in "$TESTS"/*.luc; do
    base="${luc%.luc}"
    name="$(basename "$base")"
    expected_xml="${base}.xml"

    java -cp "$DIR" parser.Parse "$luc" "$TMPXML" "$TMPGV" 2>/dev/null

    if diff -q "$TMPXML" "$expected_xml" > /dev/null 2>&1; then
        echo "PASS: $name"
        PASS=$((PASS+1))
    else
        echo "FAIL: $name"
        diff "$expected_xml" "$TMPXML" | head -10 | sed 's/^/  /'
        FAIL=$((FAIL+1))
    fi
done

rm -f "$TMPXML" "$TMPGV"

echo ""
echo "Results: $PASS passed, $FAIL failed out of $((PASS+FAIL)) tests"
