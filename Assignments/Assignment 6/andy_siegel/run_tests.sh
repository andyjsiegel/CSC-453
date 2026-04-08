#!/bin/bash
DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$DIR"

make > /dev/null 2>&1

VM_DIR="$DIR/../vm_files"
OUT_DIR="$DIR/../out_files"

PASS_S=0; FAIL_S=0
PASS_I=0; FAIL_I=0

while IFS= read -r -d '' vm; do
    rel="${vm#$VM_DIR/}"          # e.g. INTEGER/add.vm
    base="${rel%.vm}"             # e.g. INTEGER/add
    name="$(basename "$base")"
    expected="$OUT_DIR/$base.out"
    in_file="$OUT_DIR/$base.in"

    [ -f "$expected" ] || continue

    if [ -f "$in_file" ]; then
        actual_s=$(./luca_run_switch  "$vm" < "$in_file" 2>/dev/null)
        actual_i=$(./luca_run_indirect "$vm" < "$in_file" 2>/dev/null)
    else
        actual_s=$(./luca_run_switch  "$vm" 2>/dev/null)
        actual_i=$(./luca_run_indirect "$vm" 2>/dev/null)
    fi

    exp=$(cat "$expected")

    if [ "$actual_s" = "$exp" ]; then
        echo "PASS [switch]:   $base"
        PASS_S=$((PASS_S+1))
    else
        echo "FAIL [switch]:   $base"
        diff <(echo "$exp") <(echo "$actual_s") | head -6 | sed 's/^/  /'
        FAIL_S=$((FAIL_S+1))
    fi

    if [ "$actual_i" = "$exp" ]; then
        echo "PASS [indirect]: $base"
        PASS_I=$((PASS_I+1))
    else
        echo "FAIL [indirect]: $base"
        diff <(echo "$exp") <(echo "$actual_i") | head -6 | sed 's/^/  /'
        FAIL_I=$((FAIL_I+1))
    fi

done < <(find "$VM_DIR" -name "*.vm" -not -name ".*" -print0 | sort -z)

echo ""
echo "Switch:   $PASS_S passed, $FAIL_S failed out of $((PASS_S+FAIL_S)) tests"
echo "Indirect: $PASS_I passed, $FAIL_I failed out of $((PASS_I+FAIL_I)) tests"
