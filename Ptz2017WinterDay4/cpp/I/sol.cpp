#include <bits/stdc++.h>

using namespace std;

const int N = 100010;
const int NN = (N >> 6) + 2;
const int P = 6;
const int C = 100;

unsigned long long one = 1;
unsigned long long z[N * P / C + 2][NN];
unsigned long long mask[NN];

int l[N], r[N], x[N], res[N];
vector <int> all[N];
vector <int> divs[N];

int p[N];
int pos[N];
int a[N];

int main() {
  for (int i = 1; i < N; i++) {
    p[i] = i;
  }
  for (int i = 2; i < N; i++) {
    if (p[i] == i) {
      for (int j = i + i; j < N; j += i) {
        if (p[j] == j) {
          p[j] = i;
        }
      }
    }
  }
  for (int i = 1; i < N; i++) {
    divs[i].clear();
    int x = i;
    while (x > 1) {
      if (divs[i].empty() || divs[i].back() != p[x]) {
        divs[i].push_back(p[x]);
      }
      x /= p[x];
    }
  }
  int n, tt;
  scanf("%d %d", &n, &tt);
  for (int i = 0; i < n; i++) {
    scanf("%d", a + i);
    for (int j : divs[a[i]]) {
      all[j].push_back(i);
    }
  }
  int big = 0;
  for (int i = 1; i < N; i++) {
    int sz = all[i].size();
    if (sz >= C) {
      pos[i] = big;
      for (int j : all[i]) {
        z[big][j >> 6] |= (one << (j & 63));
      }
      big++;
    } else {
      pos[i] = -1;
    }
  }
  for (int i = 0; i < tt; i++) {
    scanf("%d %d %d", l + i, r + i, x + i);
    l[i]--; r[i]--;
  }
  int nn = ((n - 1) >> 6) + 1;
  for (int i = 0; i < tt; i++) {
    for (int j = 0; j < nn; j++) {
      mask[j] = 0;
    }
    for (int u : divs[x[i]]) {
      if (pos[u] == -1) {
        for (int v : all[u]) {
          mask[v >> 6] |= (one << (v & 63));
        }
      } else {
        unsigned long long *y = z[pos[u]];
        for (int j = 0; j < nn; j++) {
          mask[j] |= y[j];
        }
      }
    }
    res[i] = -2;
    int pos = r[i];
    while ((pos & 63) != 63) {
      if (mask[pos >> 6] & (one << (pos & 63))) {
        pos--;
        continue;
      }
      res[i] = pos;
      break;
    }
    if (res[i] < 0) {
      unsigned long long full = (unsigned long long) (-1LL);
      while (pos >= 0) {
        if (mask[pos >> 6] != full) {
          for (int j = pos; j >= pos - 63; j--) {
            if (mask[j >> 6] & (one << (j & 63))) {
              continue;
            }
            res[i] = j;
            break;
          }
          break;
        }
        pos -= 64;
      }
    }
    if (res[i] < l[i]) {
      res[i] = -2;
    }
  }
  for (int i = 0; i < tt; i++) {
    printf("%d\n", 1 + res[i]);
  }
  return 0;
}
