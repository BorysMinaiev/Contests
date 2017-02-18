#include <bits/stdc++.h>

using namespace std;

int a[12345];

int main() {
  int n;
  scanf("%d", &n);
  for (int i = 0; i < n; i++) {
    scanf("%d", a + i);
  }
  if (a[0] == 0) {
    printf("%d\n", 0);
    return 0;
  }
  long long ans = (1LL << n) - 1;
  while (true) {
    bool found = false;
    for (int i = 0; i < n - 1; i++) {
      if (a[i] == 0) {
        break;
      }
      if (a[i] > a[i + 1]) {
        swap(a[i], a[i + 1]);
        ans -= (1LL << i);
        found = true;
        break;
      }
    }
    if (!found) {
      break;
    }
  }
  cout << ans - 1 << endl;
  return 0;
}
