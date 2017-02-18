#include <bits/stdc++.h>

using namespace std;

const int md = 1000000007;
const int inv4 = (md + 1) / 4;

inline void add(int &a, int b) {
  a += b;
  if (a >= md) {
    a -= md;
  }
}

inline int mul(int a, int b) {
  return (long long) a * b % md;
}

const int N = 1234567;

bool was[N];
vector < pair <int, int> > g[N];
bool tree[N];
int de[N];
long long addv[N], value[N];
int from[N], to[N];

void dfs(int v) {
  was[v] = true;
  int sz = g[v].size();
  for (int j = 0; j < sz; j++) {
    int u = g[v][j].first;
    if (was[u]) {
      continue;
    }
    int id = g[v][j].second;
    tree[id] = true;
    de[u] = de[v] + 1;
    dfs(u);
  }
}

long long solve(int v) {
  was[v] = true;
  int sz = g[v].size();
  long long res = 0;
  for (int j = 0; j < sz; j++) {
    int u = g[v][j].first;
    int id = g[v][j].second;
    if (was[u]) {
      if (de[u] < de[v]) {
        res ^= value[id];
        addv[u] ^= value[id];
      }
      continue;
    }
    value[id] = solve(u);
    res ^= value[id];
  }
  return res ^ addv[v];
}

int main() {
  srand(8753);
  int n, m;
  while (scanf("%d %d", &n, &m) == 2) {
    for (int i = 0; i < n; i++) {
      g[i].clear();
    }
    for (int i = 0; i < m; i++) {
      scanf("%d %d", from + i, to + i);
      from[i]--; to[i]--;
      g[from[i]].push_back(make_pair(to[i], i));
      g[to[i]].push_back(make_pair(from[i], i));
      tree[i] = false;
    }
    for (int i = 0; i < n; i++) {
      was[i] = false;
    }
    for (int i = 0; i < n; i++) {
      if (was[i]) {
        continue;
      }
      de[i] = 0;
      dfs(i);
    }
    int coeff = 1;
    for (int i = 0; i < m; i++) {
      value[i] = 0;
      if (!tree[i]) {
        add(coeff, coeff);
        for (int j = 0; j < 8; j++) {
          value[i] = (value[i] << 8) + (rand() & 255);
        }
      }
    }
    for (int i = 0; i < n; i++) {
      was[i] = false;
      addv[i] = 0;
    }
    for (int i = 0; i < n; i++) {
      if (was[i]) {
        continue;
      }
      solve(i);
    }
    sort(value, value + m);
    int ans = 0;
    int total = 0;
    int beg = 0;
    while (beg < m) {
      int end = beg;
      while (end + 1 < m && value[end + 1] == value[beg]) {
        end++;
      }
      if (value[beg] != 0) {
        int cnt = end - beg + 1;
        total += cnt;
        add(ans, mul(cnt, cnt));
      }
      beg = end + 1;
    }
    add(ans, mul(total, total));
    ans = mul(ans, coeff);
    ans = mul(ans, inv4);
    printf("%d\n", ans);
  }
  return 0;
}
