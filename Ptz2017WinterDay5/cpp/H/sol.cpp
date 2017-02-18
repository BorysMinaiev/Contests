#include <bits/stdc++.h>

using namespace std;

const int K = 15;
const int N = 100010;

double mul[K + 2][4 * N];
double sum[K + 2][4 * N];
double p[N];

void push(int j, int x) {
  mul[j][x + x] *= mul[j][x];
  mul[j][x + x + 1] *= mul[j][x];
  sum[j][x + x] *= mul[j][x];
  sum[j][x + x + 1] *= mul[j][x];
  mul[j][x] = 1.0;
}

void pull(int j, int x) {
  sum[j][x] = sum[j][x + x] + sum[j][x + x + 1];
}

void build(int j, int x, int l, int r) {
  if (l == r) {
    double tmp = 1.0 / j;
    for (int z = 0; z < j; z++) {
      tmp *= p[l];
    }
    sum[j][x] = tmp;
    mul[j][x] = 1.0;
    return;
  }
  int y = (l + r) >> 1;
  build(j, x + x, l, y);
  build(j, x + x + 1, y + 1, r);
  mul[j][x] = 1.0;
  pull(j, x);
}

void modify(int j, int x, int l, int r, int ll, int rr, double v) {
  if (ll <= l && r <= rr) {
    mul[j][x] *= v;
    sum[j][x] *= v;
    return;
  }
  push(j, x);
  int y = (l + r) >> 1;
  if (ll <= y) {
    modify(j, x + x, l, y, ll, rr, v);
  }
  if (rr > y) {
    modify(j, x + x + 1, y + 1, r, ll, rr, v);
  }
  pull(j, x);
}

double find_sum(int j, int x, int l, int r, int ll, int rr) {
  if (ll <= l && r <= rr) {
    return sum[j][x];
  }
  push(j, x);
  int y = (l + r) >> 1;
  double res = 0.0;
  if (ll <= y) {
    res += find_sum(j, x + x, l, y, ll, rr);
  }
  if (rr > y) {
    res += find_sum(j, x + x + 1, y + 1, r, ll, rr);
  }
  pull(j, x);
  return res;
}

int main() {
  int n, tt;
  while (scanf("%d %d", &n, &tt) == 2) {
    for (int i = 0; i < n; i++) {
      scanf("%lf", p + i);
    }
    for (int j = 1; j <= K; j++) {
      build(j, 1, 0, n - 1);
    }
    while (tt--) {
      int op, from, to;
      scanf("%d %d %d", &op, &from, &to);
      from--; to--;
      if (op == 0) {
        double ans = 0.0;
        for (int j = 1; j <= K; j++) {
          ans += find_sum(j, 1, 0, n - 1, from, to);
        }
        printf("%.17f\n", -ans);
      } else {
        double t;
        scanf("%lf", &t);
        double cur = 1.0;
        for (int j = 1; j <= K; j++) {
          cur *= t;
          modify(j, 1, 0, n - 1, from, to, cur);
        }
      }
    }
  }
  return 0;
}
