#include <bits/stdc++.h>

using namespace std;

int main() {
  int n = 1000;
  int k = 1000;
  printf("%d %d\n", n, k);
  for (int i = 0; i < n; i++) printf("0.1 ");
  printf("\n");
  for (int i = 0; i < k; i++) {
    int op = rand() % 2;
    int l = rand() % n + 1;
    int r = rand() % n + 1;
    if (l > r) swap(l, r);
    double x = 0.9;
    printf("%d %d %d", op, l, r);
    if (op == 1) printf(" %.5f", x);
    printf("\n");
  }
  return 0;
}
