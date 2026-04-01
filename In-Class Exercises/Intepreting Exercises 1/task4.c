#include <stdio.h>
#include <stdlib.h>

struct foo {
   unsigned char tag;
   union {
      int i;
      float f;
   };
};

struct foo bar (int i) {
   struct foo r;
   r.i = i; r.tag='i';
   return r;
}

struct foo baz (float f) {
   struct foo r;
   r.f = f; r.tag='f';
   return r;
}

void zap (struct foo u) {
   switch (u.tag) {
      case 'i': {printf("int: %d\n", u.i); break;};
      case 'f': {printf("float: %f\n", u.f); break;};
      otherwise: abort();
   }
}

struct foo quux (struct foo a,struct foo b) {
   if ((a.tag=='i') && (b.tag=='i')) {
      return bar(a.i+b.i);
   } else if ((a.tag=='f') && (b.tag=='f')) {
      return baz(a.f+b.f);
   } else if ((a.tag=='i') && (b.tag=='f')) {
      return baz((float)a.i+b.f);
   } else if ((a.tag=='f') && (b.tag=='i')) {
      return baz(a.f+(float)b.i);
   } else {
     abort();
   }
}

int main() {
   struct foo a = bar(32);
   struct foo b = baz(35.0);
   zap(quux(a,b));
   return 0;
}
