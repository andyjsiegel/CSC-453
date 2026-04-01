#!/bin/bash
DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$DIR"
TESTS="$DIR/tests"
PASS=0
FAIL=0

while IFS= read -r -d '' luc; do
    base="${luc%.luc}"
    name="$(basename "$base")"
    expected="${base}.out"

    actual=$(java sem.Semantics "$luc" 2>&1)

    if [ "$actual" = "$(cat "$expected")" ]; then
        echo "PASS: $name"
        PASS=$((PASS+1))
    else
        echo "FAIL: $name"
        diff <(cat "$expected") <(echo "$actual") | head -10 | sed 's/^/  /'
        FAIL=$((FAIL+1))
    fi
done < <(find "$TESTS" -name "*.luc" -not -name ".*" -print0 | sort -z)

echo ""
echo "Results: $PASS passed, $FAIL failed out of $((PASS+FAIL)) tests"
