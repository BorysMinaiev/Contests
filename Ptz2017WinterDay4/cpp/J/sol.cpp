#include <bits/stdc++.h>

using namespace std;

typedef pair <double, double> state;

int n, k;
double p;
map < vector <int>, state > mp;

state solve(vector <int> a) {
  if (mp.find(a) != mp.end()) {
    return mp[a];
  }
  state &OUT = mp[a];
  int pos = a.back();
  bool any = false;
  for (int i = 0; i < k; i++) {
    if (a[i] >= 2) {
      any = true;
      break;
    }
  }
  if (any) {
    vector <int> b(k);
    for (int i = 0; i < k; i++) {
      b[i] += (a[i] + 1) / 2;
      if (i + 1 < k) {
        b[i + 1] += a[i] / 2;
      }
    }
    double p_stay;
    if (a[pos] % 2 == 0) {
      p_stay = p;
    } else {
      p_stay = 1.0 / a[pos] + (a[pos] - 1.0) / a[pos] * p;
    }
    b.push_back(pos);
    state res = solve(b);
    res.first *= p_stay;
    res.second *= p_stay;
    if (pos + 1 < k && a[pos] >= 2) {
      (b.back())++;
      state other = solve(b);
      res.first += other.first * (1.0 - p_stay);
      res.second += other.second * (1.0 - p_stay);
    }
    return OUT = res;
  }
  int x = -1, y = -1;
  for (int i = 0; i < k; i++) {
    if (a[i] > 0) {
      if (x == -1) {
        x = i;
      } else {
        y = i;
        break;
      }
    }
  }
  if (y == -1) {
    return OUT = make_pair(1.0, 1.0);
  }
  if (y == pos) {
    swap(x, y);
  }
  if (x == pos) {
    state res = make_pair(0.0, 0.0);
    if (x + 1 < k) {
      vector <int> b = a;
      b[x]--;
      b[x + 1]++;
      b.back() = pos + 1;
      state help = solve(b);
      res.first += help.first * (1.0 - p);
      res.second += help.second * (1.0 - p);
    }
    vector <int> b = a;
    b[y]--;
    if (y + 1 < k) {
      b[y + 1]++;
    }
    b.back() = pos;
    state help = solve(b);
    res.first += help.first * p;
    res.second += help.second * p;
    return OUT = res;
  }
  state s1, s2;
  {
    vector <int> b = a;
    b[x]--;
    if (x + 1 < k) {
      b[x + 1]++;
    }
    b.back() = pos;
    s1 = solve(b);
  }
  {
    vector <int> b = a;
    b[y]--;
    if (y + 1 < k) {
      b[y + 1]++;
    }
    b.back() = pos;
    s2 = solve(b);
  }
  return OUT = make_pair(min(s1.first, s2.first), max(s1.second, s2.second));
}

int main() {
  cin >> n >> k >> p;
  vector <int> a(k);
  a[0] = n;
  a.push_back(0);
  state z = solve(a);
  printf("%.17f %.17f\n", z.first, z.second);
  return 0;
}
