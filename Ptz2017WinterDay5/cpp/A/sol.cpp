#include <bits/stdc++.h>

using namespace std;

const int C = 250;
const int SZ = (int) (1e10 / C + 10);

const int N = 200010;

long long a[N], b[N];
long long c[N];
long long db[N], dc[2 * N];
int pr[N];

bool have[SZ], weird[SZ];

int main() {
  int n;
  scanf("%d", &n);
  for (int i = 0; i < n; i++) {
    scanf("%lld", a + i);
  }
  for (int i = 0; i < n; i++) {
    scanf("%lld", b + i);
  }
  sort(b, b + n);
  long long z = b[n - 1];
  int D = min(n, (int) ((long long) 1e13 / z));
  int MAX = z / C;
  for (int i = 0; i < n; i++) {
    for (int j = i + 1; j < n; j++) {
      if (b[j] - b[i] <= MAX) {
        have[b[j] - b[i]] = 1;
      } else {
        break;
      }
    }
  }
  for (int i = 0; i < n; i++) {
    for (int j = n - 1; j > i; j--) {
      if (b[i] + z - b[j] <= MAX) {
        weird[b[i] + z - b[j]] = 1;
      } else {
        break;
      }
    }
  }
  cerr << "starting z = " << z << endl;
  long long m = z;
  while (true) {
    m++;
    for (int i = 0; i < D; i++) {
      c[i] = a[i] % m;
    }
    long long CUR_MAX = MAX - (m - z);
    sort(c, c + D);
    bool ok = true;
    for (int i = 0; i < D; i++) {
      for (int j = i + 1; j < D; j++) {
        if (c[j] - c[i] <= CUR_MAX) {
          long long diff = c[j] - c[i];
          if (have[diff]) {
            continue;
          }
          if (diff >= m - z && weird[diff - (m - z)]) {
            continue;
          }
          ok = false;
          break;
        } else {
          break;
        }
      }
      if (!ok) {
        break;
      }
    }
    if (!ok) {
      continue;
    }
    for (int i = 0; i < n; i++) {
      c[i] = a[i] % m;
    }
    sort(c, c + n);
    b[n] = b[0] + m;
    for (int i = 1; i <= n; i++) {
      db[i] = b[i] - b[i - 1];
    }
    int k = 0;
    pr[1] = 0;
//    cerr << "pr ";
    for (int i = 2; i <= n; i++) {
      while (k > 0 && db[i] != db[k + 1]) {
        k = pr[k];
      }
      if (db[i] == db[k + 1]) {
        k++;
      }
      pr[i] = k;
//      cerr << pr[i] << " ";
    }
//    cerr << endl;
    c[n] = c[0] + m;
    for (int i = 1; i <= n; i++) {
      dc[i] = c[i] - c[i - 1];
      dc[i + n] = dc[i];
    }
/*    cerr << "m = " << m << endl;
    for (int i = 1; i <= n; i++) cerr << dc[i] << " ";
    cerr << endl;
    for (int i = 1; i <= n; i++) cerr << db[i] << " ";
    cerr << endl;*/
    k = 0;
    long long res = -1;
    for (int i = 1; i <= 2 * n; i++) {
      while (k > 0 && dc[i] != db[k + 1]) {
        k = pr[k];
      }
      if (dc[i] == db[k + 1]) {
        k++;
      }
//      cerr << "i = " << i << ", k = " << k << endl;
      if (k == n) {
//        cerr << "pair " << b[n] << " " << c[i % n] << endl;
        res = ((b[n] - c[i % n]) % m + m) % m;
        break;
      }
    }
    if (res == -1) {
      continue;
    }
    printf("%lld %lld\n", m, res);
    break;
  }
  return 0;
}
