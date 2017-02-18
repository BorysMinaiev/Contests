#include <bits/stdc++.h>

using namespace std;

struct Triangle {
  int x;
  int y;
  int z;
  int cost;
};

const int N = 100010;

Triangle best[N];
Triangle good[N][2][2];
int ans = -1;

inline void test(int x, int y, int z, int cost, int rot) {
  if (rot == 0) {
    Triangle t = {x, y, z, cost};
    if (t.cost > best[x].cost) {
      best[x] = t;
    }
  }
  if (rot == 1) {
    Triangle t = {x, y, z, cost};
    int fy = (y == best[x].y || z == best[x].y);
    int fz = (z == best[x].z || y == best[x].z);
    if (t.cost > good[x][fy][fz].cost) {
      good[x][fy][fz] = t;
    }
  }
  if (rot == 2) {
    for (int fy = 0; fy < 2; fy++) {
      for (int fz = 0; fz < 2; fz++) {
        Triangle g = good[x][fy][fz];
        if (g.cost == 0) {
          continue;
        }
        if (g.y == y || g.y == z || g.z == y || g.z == z) {
          continue;
        }
        ans = max(ans, cost + g.cost);
      }
    }
  }
}

vector <int> g[N], c[N];
vector < pair <int, int> > gold[N];
pair <int, int> e[N];
int deg[N], order[N], pos[N];
int have[N];

int main() {
  int n, m;
  scanf("%d %d", &n, &m);
  for (int i = 0; i < m; i++) {
    int x, y, z;
    scanf("%d %d %d", &x, &y, &z);
    x--; y--;
    gold[x].push_back(make_pair(y, z));
    gold[y].push_back(make_pair(x, z));
  }
  for (int i = 0; i < n; i++) {
    deg[i] = gold[i].size();
    e[i] = make_pair(deg[i], i);
  }
  sort(e, e + n);
  reverse(e, e + n);
  for (int i = 0; i < n; i++) {
    order[i] = e[i].second;
    pos[e[i].second] = i;
  }
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < (int) gold[i].size(); j++) {
      if (pos[i] < pos[gold[i][j].first]) {
        g[i].push_back(gold[i][j].first);
        c[i].push_back(gold[i][j].second);
      }
    }
  }
  for (int rot = 0; rot < 3; rot++) {
    for (int it = 0; it < n; it++) {
      int x = order[it];
      for (int j = 0; j < (int) g[x].size(); j++) {
        int z = g[x][j];
        have[z] = c[x][j];
      }
      for (int j = 0; j < (int) g[x].size(); j++) {
        int y = g[x][j];
        for (int k = 0; k < (int) g[y].size(); k++) {
          int z = g[y][k];
          if (have[z]) {
            int cost = c[x][j] + c[y][k] + have[z];
            test(x, y, z, cost, rot);
            test(y, z, x, cost, rot);
            test(z, x, y, cost, rot);
          }
        }
      }
      for (int j = 0; j < (int) g[x].size(); j++) {
        int z = g[x][j];
        have[z] = 0;
      }
    }
  }
  printf("%d\n", ans);
  return 0;
}
