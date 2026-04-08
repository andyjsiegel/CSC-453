int main() {
    int x = 0;
    if(x > 1) {
        return 0;
    }
    while(0) {
        if(x == 0) {
            printf(x);
            x = 1;
        } else {
            printf("Hello");
            x = 2;
        }
    }
    return 0;
}