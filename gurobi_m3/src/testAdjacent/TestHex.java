package testAdjacent;

import java.util.ArrayList;
import ip.IPadj;
import ip.M;
import processing.core.PApplet;

public class TestHex extends PApplet { //same results by Python: IP Adj / Test_hex.py
	private int[][] P; // grid
	private int[][] _P_; // boundary, be void
	private static final int K=9; //number of templates
	private int[][][] templates= new int[K][][];  // template shapes
	private boolean[][] res ;  // to be obtained from optimize()
	private int[] cols;
	private float sx = 30;
	private float sy = -sx;
	private float[][] hex=new float[6][];
	
	public void setup() {
		size(1000, 800);
		for (int i = 0; i < 6; i++) {
			float a = i*PI / 3;
			hex[i] = new float[] { sin(a),  cos(a) };
		}
		templates[0] = new int[][] { {0, -1, 1}, {0, 0, 0}, {0, 1, -1}};
		templates[1] = new int[][] { {1, -1, 0}, {0, 0, 0}, {-1, 1, 0} };
		templates[2] = new int[][] { {1, 0, -1}, {0, 0, 0}, {-1, 0, 1} };

		templates[3] = new int[][] {{0, -1, 1}, {0, 0, 0}, {-1, 1, 0} };
		templates[4] = new int[][] { {0, -1, 1}, {0, 0, 0}, {-1, 0, 1} };
		templates[5] = new int[][] {{1, -1, 0}, {0, 0, 0}, {0, 1, -1} };
		templates[6] = new int[][] {{1, -1, 0}, {0, 0, 0}, {-1, 0, 1} };
		templates[7] = new int[][] { {1, 0, -1}, {0, 0, 0}, {0, 1, -1}};
		templates[8] = new int[][] { {1, 0, -1},{0, 0, 0}, {-1, 1, 0}};
		
		ArrayList<int[]> list = new ArrayList<int[]>();
		int nn = 11;  //odd
		for (int x = 0; x < nn; x++) {
			for (int y = 0; y < nn; y++) {
				for (int z = 0; z < nn; z++) {
					int[] id = { x - nn / 2, y - nn / 2, z - nn / 2 };
					if (id[0] < -2 && id[1] >1 ) 
						continue;
					if (id[0] == 2 && id[1] ==-1 ) 
						continue;
					if (id[0] + id[1] + id[2] == 0)
						list.add(id);
				}
			}
		}
		P = M.toArray(list); // available cells
		_P_ = expand_hex(P); // boundary (should be void)
		println("P " + P.length + ", " + "Q " + _P_.length);

		
		int[][] mm = new int[K][];
		for (int k = 0; k < K; k++)
			mm[k] = new int[] { (0==k? 3:1), 4 }; 
		
		int[][] pairs = { { 0, 4 }, {4,0} ,{ 4, 4 } };
		int[][][] AAs = new int[pairs.length][][];
		for (int i = 0; i < pairs.length; i++)
			AAs[i] = AA(pairs[i]);

		
		IPadj ip = new IPadj(P, _P_, templates, mm);
		res = ip.optimize(null, null, AAs, pairs);
		cols = new int[P.length];
		for (int i = 0; i < P.length; i++)
			cols[i] = color(random(256), random(256), random(256));
	}

	public void draw() {
		background(255);
		translate(width/2, height/2);
		fill(240);
		for (int[] p : P)
			drawHex(pos(p)); // draw grid (for rooms)
		fill(100);
		for (int[] p : _P_) {
			float[] t = pos(p);
			ellipse(t[0] * sx, t[1] * sy, sx*1.5f, sx*1.5f); // draw boundary (be void)
		}

		// ******** draw results
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < P.length; i++) {
				if (4 == k)
					fill(255,0,0);
				else if (0 == k)
					fill(255);
				else
					fill(cols[i]);
				if (res[k][i]) {
					int[] t = P[i];
					for (int[] p : templates[k]) {
						float[] v = pos(M.add(t,p));
						drawHex(v); 
					}
				}
			}
		}

	}
	
	private void drawHex(float[] p) {
		beginShape();
		for (float[] v : hex) {
			float[] t = M.add(v, p);
			vertex(t[0] * sx, t[1] * sy);
		}
		endShape(CLOSE);
	}
	
	private float[] pos(int[] a) {
		float x = sqrt(3) * 0.5f * (a[1] - a[2]);
		float y = a[0] - 0.5f * (a[1] + a[2]);
		return new float[] { x, y };
	}

	public static int[][] expand_hex(int[][] vs) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int[] a : vs) {
			int[][] bs = {{a[0], a[1] + 1, a[2] - 1},{a[0], a[1] - 1, a[2] + 1}, {a[0] + 1, a[1], a[2] - 1}, {a[0] - 1, a[1], a[2] + 1},
					{a[0] + 1, a[1] - 1, a[2]}, {a[0] - 1, a[1] + 1, a[2]} }; // up
			for (int[] b : bs) {
				if (M.has(vs, b) || M.has(list, b))
					continue;
				list.add(b);
			}
		}
		return M.toArray(list);
	}
	
	private int[][] AA(int[] ab) { // given A[b] at (0,0)
		int ka = ab[0];
		int kb = ab[1];
		int[] mina = new int[3];
		int[] maxa = new int[3];
		int[] minb = new int[3];
		int[] maxb = new int[3];
		for (int i = 0; i < 3; i++) {
			mina[i] = M.min(templates[ka], i);
			maxa[i] = M.max(templates[ka], i);
			minb[i] = M.min(templates[kb], i);
			maxb[i] = M.max(templates[kb], i);
		}
		int[][] bound_B = expand_hex(templates[kb]);
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int i = minb[0] - maxa[0] - 1; i <= maxb[0] - mina[0] + 1; i++) {
			for (int j = minb[1] - maxa[1] - 1; j <= maxb[1] - mina[1] + 1; j++) {
				for (int k = minb[2] - maxa[2] - 1; k <= maxb[2] - mina[2] + 1; k++) {
					if (0 != i + j + k)
						continue;
					int[] ca = { i, j, k };
					int[][] patch = M.addEach(templates[ka], ca);
					if (!M.overlap(templates[kb], patch) && M.overlap(bound_B, patch))
						list.add(ca);
				}
			}
		}
//		for(int[] v:list) 
//			println(v[0]+","+ v[1]+","+ v[2]);
		return M.toArray(list);
	}

}
