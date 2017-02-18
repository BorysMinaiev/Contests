#include <bits/stdc++.h>

using namespace std;

struct rect {
  int xa;
  int ya;
  int xb;
  int yb;
};

const int N = 2222;

char s[N][N];


int main() {
  int h, w;
  scanf("%d %d", &h, &w);
  for (int i = 0; i < h; i++) {
    scanf("%s", s[i]);
  }
  vector <rect> res;
  for (int i = 0; i < h; i++) {
    for (int j = 0; j < w; j++) {
      if (s[i][j] == 'X') {
        rect r;
        r.xa = i;
        r.ya = j;
        {
          int jj = j + 1;
          while (jj < w) {
            if (s[i + 1][jj] == 'X' && s[i + 1][jj - 1] == '.') {
              bool corner = false;
              if (s[i + 2][jj] == 'X' && s[i + 1][jj + 1] == 'X' && s[i + 3][jj] == 'X' && s[i + 1][jj + 2] == 'X' && s[i + 2][jj + 1] == '.' && s[i][jj + 2] == 'X') {
                corner = true;
              }
              if (!corner) {
                r.yb = jj;
                break;
              }
            }
            jj++;
          }
        }
        {
          int ii = i + 1;
          while (ii < h) {
            if (s[ii][j + 1] == 'X' && s[ii - 1][j + 1] == '.') {
              bool corner = false;
              if (s[ii][j + 2] == 'X' && s[ii][j + 3] == 'X' && s[ii + 1][j + 1] == 'X' && s[ii + 2][j + 1] == 'X' && s[ii + 1][j + 2] == '.' && s[ii + 2][j] == 'X') {
                corner = true;
              }
              if (!corner) {
                r.xb = ii;
                break;
              }
            }
            ii++;
          }
        }
        for (int ii = r.xa; ii <= r.xb; ii++) {
          assert(s[ii][r.ya] == 'X' && s[ii][r.yb] == 'X');
          s[ii][r.ya] = s[ii][r.yb] = '.';
        }
        for (int jj = r.ya + 1; jj <= r.yb - 1; jj++) {
          assert(s[r.xa][jj] == 'X' && s[r.xb][jj] == 'X');
          s[r.xa][jj] = s[r.xb][jj] = '.';
        }  
        res.push_back(r);
      }
    }
  }
  int sz = res.size();
  printf("%d\n", sz);
  for (int i = 0; i < sz; i++) {
    printf("%d %d %d %d\n", 1 + res[i].xa, 1 + res[i].ya, 1 + res[i].xb, 1 + res[i].yb);
  }
  return 0;
}
