#!/bin/bash
DIR="$(cd "$(dirname "$0")" && pwd)"
TESTS="$DIR/tests"
PASS=0
FAIL=0

for luc in "$TESTS"/*.luc; do
    base="${luc%.luc}"
    name="$(basename "$base")"
    xml="$base.xml"
    output=$(java -cp "$DIR" parser.Parse "$luc" 2>/dev/null)

    if [ -f "$xml" ]; then
        # error test: extract SYNTAX_ERROR block and compare
        actual=$(echo "$output" | grep -A 2 "^<SYNTAX_ERROR")
        expected=$(cat "$xml")
        if [ "$actual" = "$expected" ]; then
            echo "PASS: $name"
            PASS=$((PASS+1))
        else
            echo "FAIL: $name"
            echo "  expected: $expected"
            echo "  actual:   $actual"
            FAIL=$((FAIL+1))
        fi
    else
        # valid test: should have no SYNTAX_ERROR
        if echo "$output" | grep -q "SYNTAX_ERROR"; then
            echo "FAIL: $name (unexpected error)"
            echo "  $output"
            FAIL=$((FAIL+1))
        else
            echo "PASS: $name"
            PASS=$((PASS+1))
        fi
    fi
done

echo ""
echo "Results: $PASS passed, $FAIL failed"
