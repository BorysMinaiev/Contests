#include <bits/stdc++.h>

using namespace std;

const int md = 1000000007;

inline void add(int &a, int b) {
  a += b;
  if (a >= md) a -= md;
}

inline int mul(int a, int b) {
  return (long long) a * b % md;
}

inline int power(int a, int b) {
  int res = 1;
  while (b > 0) {
    if (b & 1) {
      res = mul(res, a);
    }
    a = mul(a, a);
    b >>= 1;
  }
  return res;
}

inline int inv(int x) {
  return power(x, md - 2);
}

const int N = 5010;

int C[N][N];
int f[N][N];
int fact[N], inverse[N];
int pr[N];
int sz[N];
int d[N], prefd[N];
bool on_path[N];

int main() {
  fact[0] = 1;
  for (int i = 1; i < N; i++) {
    fact[i] = mul(fact[i - 1], i);
  }
  for (int i = 0; i < N; i++) {
    inverse[i] = inv(i);
  }
  for (int i = 0; i < N; i++) {
    for (int j = 0; j < N; j++) {
      if (j == 0) C[i][j] = 1; else
      if (i == 0) C[i][j] = 0; else {
        C[i][j] = C[i - 1][j] + C[i - 1][j - 1];
        if (C[i][j] >= md) C[i][j] -= md;
      }
    }
  }
  int n;
  scanf("%d", &n);
  for (int i = 1; i < n; i++) {
    scanf("%d", pr + i);
    pr[i]--;
  }
  pr[0] = -1;
  int v, k;
  scanf("%d %d", &v, &k);
  v--; k--;
  vector <int> path;
  int tmp = v;
  while (tmp != -1) {
    path.push_back(tmp);
    on_path[tmp] = true;
    tmp = pr[tmp];
  }
  int cnt = path.size();
  reverse(path.begin(), path.end());
  for (int i = n - 1; i >= 0; i--) {
    sz[i] = 1;
  }
  for (int i = n - 1; i > 0; i--) {
    sz[pr[i]] += sz[i];
  }
  int ans = 1;
  for (int i = 0; i < n; i++) {
    if (!on_path[i]) {
      ans = mul(ans, inverse[sz[i]]);
    }
  }
  for (int i = 0; i < cnt; i++) {
    int diff = sz[path[i]];
    if (i + 1 < cnt) {
      diff -= sz[path[i + 1]];
    }
    d[i] = diff - 1;
    ans = mul(ans, fact[d[i]]);
  }
  prefd[0] = 0;
  for (int i = 0; i < cnt; i++) {
    prefd[i + 1] = prefd[i] + d[i];
  }
  if ((v == 0) != (k == 0)) {
    printf("%d\n", 0);
    return 0;
  }
  f[0][0] = ans;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j <= i && j <= cnt; j++) {
      if (f[i][j] == 0) {
        continue;
      }
      int have = prefd[j] + j - i;
      if (have > 0) {
        if (i != k) {
          add(f[i + 1][j], f[i][j]);
        }
      }
      if (i == k && j != cnt - 1) {
        continue;
      }
      if (j < cnt) {
        add(f[i + 1][j + 1], mul(f[i][j], C[have + d[j]][d[j]]));
      }
    }
  }
  printf("%d\n", f[n][cnt]);
  return 0;
}
