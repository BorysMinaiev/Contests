#include <bits/stdc++.h>

using namespace std;

const int N = 1234567;

int e, st[N];
vector <int> g[N];
int a[N];
int ans[N];

void push(int i) {
  st[e++] = i;
}

void pull(int i) {
  g[i].push_back(st[e - 1]);
  g[st[e - 1]].push_back(i);
  e--;
}

int main() {
  int n;
  scanf("%d", &n);
  for (int i = 0; i < n - 1; i++) {
    scanf("%d", a + i);
  }
  for (int i = 0; i < n - 1; i++) {
    if (a[i] % 2 != 0) {
      puts("No");
      return 0;
    }
  }
  if (a[0] != 0 && a[0] != 2) {
    puts("No");
    return 0;
  }
  a[n - 1] = 0;
  for (int i = 0; i < n - 1; i++) {
    int diff = abs(a[i] - a[i + 1]);
    if (diff != 0 && diff != 2) {
      puts("No");
      return 0;
    }
  }
  int pr = 0;
  e = 0;
  for (int i = 0; i < n; i++) {
    if (a[i] == pr) {
      push(i);
      pull(i);
    } else {
      if (a[i] > pr) {
        push(i);
        push(i);
      } else {
        pull(i);
        pull(i);
      }
    }
    pr = a[i];
  }
  for (int i = 0; i < n; i++) {
    ans[i] = -1;
  }
  for (int i = 0; i < n; i++) {
    if (ans[i] != -1) {
      continue;
    }
    int p = i;
    int pr = g[i][1];
    while (ans[p] == -1) {
      ans[p] = g[p][0] + g[p][1] - pr;
      pr = p;
      p = ans[p];
    }
  }
  puts("Yes");
  for (int i = 0; i < n; i++) {
    printf("%d ", ans[i] + 1);
  }
  return 0;
}
