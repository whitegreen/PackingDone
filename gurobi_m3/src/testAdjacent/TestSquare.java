package testAdjacent;

import java.util.ArrayList;
import ip.IPadj;
import ip.M;
import processing.core.PApplet;

public class TestSquare extends PApplet { //same results by Python: IP Adj / Test_square.py
	private int[][] P; //  grid
	private int[][] _P_; //  boundary, be void
	private static final int K = 10; // number of templates
	private int[][][] templates = new int[K][][]; // template shapes
	private final int[][][] Hs = { { { 11, 4 }, { 11, 5 }, { 11, 6 } }, { {5, -1 }, { 6, -1 }, { 7, -1 } } }; // fixed adj patch
	private boolean[][] res; // to be obtained from optimize()
	private int[] cols;
	private float sx = 40;
	private float sy = -sx;

	public void setup() {
		size(1000, 800);
		templates[0] = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 } };   //L
		templates[1] = new int[][] { { 0, 0 }, { -1, 0 }, { 0, -1 } };  //L
		templates[2] = new int[][] { { 0, 0 }, { 0, 1 }, { -1, 0 } };  //L
		templates[3] = new int[][] { { 0, 0 }, { 0, -1 }, { 1, 0 } };  //L
		
		templates[4] = new int[][] { { 0, 1 }, { 0, 0 }, { 0, -1 } };   //vertical 3
		templates[5] = new int[][] { { 1, 0 }, { 0, 0 }, { -1, 0 } };  // horizon 3
		templates[6] = new int[][] { { 0, 1 }, { 0, 0 }, { -1, 0 }, { -2, 0 } };
		templates[7] = new int[][] { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -2, 0 } };
		templates[8] = new int[][] { { 1, 0 }, { 0, 0 }, { -1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 } };  // 2*3
		templates[9] = new int[][] { { 0, 1 }, { 0, 0 }, { 0, -1 }, { 1, 1 }, { 1, 0 }, { 1, -1 } };  // 3*2
		
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int x = 0; x < 11; x++) {
			for (int y = 0; y < 13; y++) {
				if (5 == x && 6 < y&& y<10)
					continue;
				if (6 == x && 6 < y&& y<10)
					continue;
				list.add(new int[] { x, y });
			}
		}
		for (int y = 0; y < 4; y++) {
			list.add(new int[] { -1, y });
			list.add(new int[] { -2, y });
			list.add(new int[] { -3, y });
		}
		P = M.toArray(list); // available cells
		_P_ = expand(P); // boundary (should be void)
		println("P " + P.length + ", " + "Q " + _P_.length);

		int[] AH_is = { 4, 2 };
		int[][] mm = new int[K][];
		for (int k = 0; k < K; k++)
			mm[k] = new int[] { (M.has(AH_is, k) ? 2 : 1), 6 }; // {1,4} works
		mm[8] = new int[] { 2, 6 }; 
		
		IPadj ip = new IPadj(P, _P_, templates, mm);
		int[][][] AHs = { { { 10, 3 }, { 10, 4 }, { 10, 5 }, { 10, 6 }, { 10, 7 } }, { { 5, 0 }, { 6, 0 }, { 7, 0 }, { 8, 0 } } }; // see H
		
		int[][] pairs = { { 0, 8 }, { 1, 8 }, { 2, 8 }, { 3, 8 } };
		int[][][] AAs = new int[pairs.length][][];
		for (int i = 0; i < pairs.length; i++)
			AAs[i] = AA(pairs[i]);
//		res = ip.optimize(AHs, AH_is,null,null);
		res = ip.optimize(AHs, AH_is, AAs, pairs);

		cols = new int[P.length];
		for (int i = 0; i < P.length; i++)
			cols[i] = color(random(256), random(256), random(256));
		rectMode(CENTER);
		
	}

	public void draw() {
		background(255);
		translate(300, 600);
		stroke(0);
		fill(255);
		for (int[] p : P)
			rect(p[0] * sx, p[1] * sy, sx, sx); // draw grid
		fill(100);
		for (int[] p : _P_)
			ellipse(p[0] * sx, p[1] * sy, sx - 8, sx - 8); // draw boundary (be void)
		
		fill(255, 0, 0);
		for (int[][] H : Hs)
			for (int[] p : H)
				ellipse(p[0] * sx, p[1] * sy, sx - 8, sx - 8); // draw grid
		// ******** draw results
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < P.length; i++) {
				fill(cols[i]);
				if (res[k][i]) {
					int[] v = P[i];
					for (int[] p : templates[k]) {
						int[] t = M.add(v, p);
						rect(t[0] * sx, t[1] * sy, sx, sx);
					}
				}
			}
		}
		stroke(255, 0, 0);
		line(0, 0, sx, 0);
		stroke(0, 255, 0);
		line(0, 0, 0, sy);
	}

	private int[][] expand(int[][] vs) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int[] p : vs) {
			int[][] bs = { {p[0] - 1, p[1]}, {p[0] + 1, p[1]}, {p[0], p[1] - 1}, {p[0], p[1] + 1} };
			for (int[] b : bs) {
				if (M.has(vs, b) || M.has(list, b))
					continue;
				list.add(b);
			}
		}
		return M.toArray(list);
	}

	private int[][] AA( int[] ab) { // given A[b] at (0,0)
		int ka=ab[0];
		int kb=ab[1];
		int min0a = M.min(templates[ka], 0);
		int min1a =  M.min(templates[ka], 1);
		int max0a =  M.max(templates[ka], 0);
		int max1a =  M.max(templates[ka], 1);

		int min0b =  M.min(templates[kb], 0);
		int min1b =  M.min(templates[kb], 1);
		int max0b =  M.max(templates[kb], 0);
		int max1b =  M.max(templates[kb], 1);
		
		int[][] bound_B = expand(templates[kb]);
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int i = min0b - max0a-1; i <= max0b - min0a+1; i++) {
			for (int j = min1b - max1a-1; j <= max1b - min1a+1; j++) {
				int[] ca = { i, j };
				int[][] patch = M.addEach(templates[ka], ca);
				if (!M.overlap(templates[kb], patch) && M.overlap(bound_B, patch))
					list.add(ca);
			}
		}
		return M.toArray(list);
	}


}
