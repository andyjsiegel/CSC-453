#include <stdlib.h>
#include <stdio.h>

struct Tree {
   int value;
   struct Tree *left;
   struct Tree *right;
};
typedef struct Tree Tree;

Tree * new (int value) {
   Tree *t = (Tree*)malloc(sizeof(Tree));
   t->value = value;
   t->left = NULL;
   t->right = NULL;
   return t;
}

int main () {
   Tree *t = new(42);
   Tree *l = new(57);
   Tree *r = new(99);
   t->left = l;
   t->right = r;

   Tree *s = new(11);

   t = s;

   t->left = s;

   t->left = s->right;

   t->left = NULL;
}
