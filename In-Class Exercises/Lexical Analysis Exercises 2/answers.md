Task 1
Circles are states
Arrows are transitions
Double circle is an accepting state

Task 2
Transitions of /*6*7*/
1 -> 2 -> 3 -> 3 -> 4 -> 3 -> 4 -> 5

Task 3
1. 1 -> 2 -> 3 -> 3
2. 1 -> 3 -> 3 -> 4, reject
3. 1 -> 3 -> 3 -> 4 -> 5
4. 1 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8

Task 4
1. 1 -> 2 -> 4, 1 -> 3 -> 4 -> 2 -> 4
2. no paths
3. 1 -> 2 -> 4 -> 2 -> 4, 1 -> 3 -> 4 -> 2 -> 4 -> 2 -> 4
4. 1 -> 2 -> 4 -> 2 -> 4 -> 2 -> 4, 1 -> 3 -> 4 -> 2 -> 4 -> 2 -> 4 -> 2 -> 4

Task 7
'a', 'a*','a*b'

Task 12
grep -iE "^Y..MA..Y$" /usr/share/dict/words

Task 13
grep -iE "^.*[a-b]{4,5}.*$" /usr/share/dict/words