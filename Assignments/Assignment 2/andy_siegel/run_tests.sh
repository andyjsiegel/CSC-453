#!/bin/bash
# Test runner for the Luca lexer
# Relaxed matching rules:
#   - Line numbers can differ by +/- 1
#   - Output after the first ERROR token is not checked
#   - Only the first error token must match kind (and line within tolerance)

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
TESTS_DIR="$BASE_DIR/tests"
RESULTS_DIR="$BASE_DIR/test_results"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
CYAN='\033[0;36m'
NC='\033[0m'

PASS=0
FAIL=0
TOTAL=0

# Compile
echo "Compiling lexer..."
cd "$BASE_DIR"
javac lexer/*.java 2>/tmp/lexer_compile_err.txt
if [ $? -ne 0 ]; then
    echo -e "${RED}COMPILATION FAILED:${NC}"
    cat /tmp/lexer_compile_err.txt
    exit 1
fi
echo -e "${GREEN}Compilation successful.${NC}"
echo ""

# For each test
for test_input in "$TESTS_DIR"/*.luc; do
    test_name=$(basename "$test_input" .luc)
    expected_file="$RESULTS_DIR/${test_name}.out"
    TOTAL=$((TOTAL + 1))

    if [ ! -f "$expected_file" ]; then
        echo -e "${YELLOW}SKIP${NC} $test_name (no expected output)"
        continue
    fi

    # Run lexer
    actual=$(cd "$BASE_DIR" && java lexer.Lex "$test_input" 2>/dev/null)

    # Extract TOKEN lines
    exp_tokens=$(echo "$expected_file" | xargs grep '<TOKEN' 2>/dev/null)
    act_tokens=$(echo "$actual" | grep '<TOKEN')

    # Parse into arrays
    IFS=$'\n' read -rd '' -a exp_lines <<< "$(grep '<TOKEN' "$expected_file")"
    IFS=$'\n' read -rd '' -a act_lines <<< "$(echo "$actual" | grep '<TOKEN')"

    errors=""
    token_idx=0

    for i in "${!exp_lines[@]}"; do
        exp="${exp_lines[$i]}"
        act="${act_lines[$i]:-}"

        # Parse expected
        exp_kind=$(echo "$exp" | sed -n 's/.*kind="\([^"]*\)".*/\1/p')
        exp_line=$(echo "$exp" | sed -n 's/.*line="\([^"]*\)".*/\1/p')
        exp_val=$(echo "$exp" | sed -n 's/.*value="\([^"]*\)".*/\1/p')

        if [ -z "$act" ]; then
            errors="${errors}  Token $((i+1)): expected ${exp_kind} but got no more tokens\n"
            break
        fi

        # Parse actual
        act_kind=$(echo "$act" | sed -n 's/.*kind="\([^"]*\)".*/\1/p')
        act_line=$(echo "$act" | sed -n 's/.*line="\([^"]*\)".*/\1/p')
        act_val=$(echo "$act" | sed -n 's/.*value="\([^"]*\)".*/\1/p')

        # Check kind
        if [ "$exp_kind" != "$act_kind" ]; then
            errors="${errors}  Token $((i+1)): kind mismatch: expected '${exp_kind}', got '${act_kind}'\n"
            errors="${errors}    Expected: $(echo "$exp" | xargs)\n"
            errors="${errors}    Actual:   $(echo "$act" | xargs)\n"
        fi

        # Check line number (tolerance +/- 1)
        if [ -n "$exp_line" ] && [ -n "$act_line" ]; then
            diff=$((act_line - exp_line))
            if [ $diff -lt -1 ] || [ $diff -gt 1 ]; then
                errors="${errors}  Token $((i+1)) ($exp_kind): line mismatch: expected ${exp_line}, got ${act_line} (tolerance +/-1)\n"
            fi
        fi

        # Check value if expected has one
        if [ -n "$exp_val" ] && [ "$exp_kind" = "$act_kind" ]; then
            if [ "$exp_val" != "$act_val" ]; then
                errors="${errors}  Token $((i+1)) ($exp_kind): value mismatch: expected '${exp_val}', got '${act_val}'\n"
            fi
        fi

        # If this was an error token in the expected output, stop comparing
        case "$exp_kind" in
            ERROR_*) break ;;
        esac
    done

    if [ -z "$errors" ]; then
        echo -e "${GREEN}PASS${NC} $test_name"
        PASS=$((PASS + 1))
    else
        echo -e "${RED}FAIL${NC} $test_name"
        echo -e "$errors"
        FAIL=$((FAIL + 1))
    fi
done

echo ""
echo "=========================================="
echo -e "Total: $TOTAL | ${GREEN}Passed: $PASS${NC} | ${RED}Failed: $FAIL${NC}"

if [ $FAIL -eq 0 ]; then
    echo -e "${GREEN}All tests passed!${NC}"
    exit 0
else
    exit 1
fi
