#include <bits/stdc++.h>

using namespace std;

const int N = 1234567;

long long a[N], b[N];

int main() {
  srand(8753);
  int n = 200000;
  long long m = (long long) 1e10;
  long long k = (((((rand() << 15) + rand()) % m + m) % m));
  printf("%d\n", n);
  for (int i = 0; i < n; i++) {
    a[i] = 0;
    for (int j = 0; j < 18; j++) {
      a[i] = a[i] * 10 + rand() % 10;
    }
    printf("%lld ", a[i]);
  }
  printf("\n");
  for (int i = 0; i < n; i++) {
    b[i] = (a[i] + k) % m;
  }
  random_shuffle(b, b + n);
  for (int i = 0; i < n; i++) {
    printf("%lld ", b[i]);
  }
  printf("\n");
  fprintf(stderr, "%lld %lld\n", m, k);
  return 0;
}
