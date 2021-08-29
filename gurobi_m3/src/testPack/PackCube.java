package testPack;

import java.util.ArrayList;
import ip.IP;
import ip.M;
import peasy.PeasyCam;
import processing.core.PApplet;

public class PackCube extends PApplet { // same results Pack_cube.py
	private static final int DM = 3;
	private int[][] P; // grid
	private int[][] _P_; // boundary, be void
	private static final int K = 14; // number of templates
	private int[][][] templates = new int[K][][]; // template shapes
	private boolean[][] res; // to be obtained from optimize()
	private int[] cols;

	public void setup() {
		size(800, 800,P3D);
		new PeasyCam(this,300);
		templates[0] = new int[][] { { 0, 0, 0 }, { 1, 0, 0 }, { 0, 1, 0 } };
		templates[1] = new int[][] { { 0, 0, 0 }, { -1, 0, 0 }, { 0, -1, 0 } };
		templates[2] = new int[][] { { 0, 1, 0 }, { 0, 0, 0 }, { 0, -1, 0 } };
		templates[3] = new int[][] { { 1, 0, 0 }, { 0, 0, 0 }, { -1, 0, 0 } };

		templates[4] = new int[][] { { 0, 0, 0 }, { 1, 0, 0 }, { 0, 0, 1 } };
		templates[5] = new int[][] { { 0, 0, 0 }, { 1, 0, 0 }, { 0, 0, -1 } };
		templates[6] = new int[][] { { 0, 0, 0 }, { -1, 0, 0 }, { 0, 0, 1 } };
		templates[7] = new int[][] { { 0, 0, 0 }, { -1, 0, 0 }, { 0, 0, -1 } };

		templates[8] = new int[][] { { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
		templates[9] = new int[][] { { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, -1 } };
		templates[10] = new int[][] { { 0, 0, 0 }, { 0, -1, 0 }, { 0, 0, 1 } };
		templates[11] = new int[][] { { 0, 0, 0 }, { 0, -1, 0 }, { 0, 0, -1 } };

		templates[12] = new int[][] { { 0, 0, -1 }, { 0, 0, 0 }, { 0, 0, 1 } };
		templates[13] = new int[27][];
		int count = 0;
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				for (int z = 0; z < 3; z++)
					templates[13][count++] = new int[] { x, y, z };

		ArrayList<int[]> list = new ArrayList<int[]>();
		int mx = 8;
		int my = 6;
		int mz = 14;
		int xx = 6;
		int yy = 3;
		int zz = 8;
		for (int x = 0; x < xx; x++) {
			for (int y = 0; y < my; y++) {
				for (int z = 0; z < mz; z++) {
					if (z > zz - 1 && x > 3 && y < yy)
						continue;
					list.add(new int[] { x, y, z });
				}
			}
		}
		for (int x = xx; x < mx; x++)
			for (int y = yy; y < my; y++)
				for (int z = zz; z < mz; z++)
					list.add( new int[] { x, y, z } );

		P = M.toArray(list); // grid
		_P_ = expand(P); // boundary (should be void)
		println("P " + P.length + ", " + "Q " + _P_.length);
		res = IP.optimize(P, _P_, templates, null);

		cols = new int[P.length];
		for (int i = 0; i < P.length; i++)
			cols[i] = color(random(256), random(256), random(256));
	}

	public void draw() {
		background(255);
		rotateX(PI/2);
		scale(1, -1);
		float u = 20;
		translate(-2*u,0,-6*u);
		noFill();
		stroke(0);
		for (int[] p : P)
			box(p, u); // draw grid 
//		 stroke(0,255,0);
//		 for (int[] p : _P_) 
//			 box( p, u); //draw boundary (be void)

		// ******** draw results
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < P.length; i++) {
				fill(cols[i]);
				if (res[k][i]) {
					int[] v = P[i];
					for (int[] p : templates[k])
						box(M.add(v, p), u);
				}
			}
		}
	}

	private void box(int[] p, float u) {
		pushMatrix();
		translate(p[0] * u, p[1] * u, p[2] * u);
		box(u);
		popMatrix();
	}

	private static int[][] neighbor(int[] a) {
		int[][] neis = new int[2 * DM][];
		for (int i = 0; i < neis.length; i++)
			neis[i] = a.clone();
		for (int i = 0; i < DM; i++) {
			neis[i][i] += 1;
			neis[DM + i][i] -= 1;
		}
		return neis;
	}

	private static int[][] expand(int[][] vs) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int[] p : vs) {
			int[][] bs = neighbor(p);
			for (int[] b : bs) {
				if (M.has(vs, b) || M.has(list, b))
					continue;
				list.add(b);
			}
		}
		return M.toArray(list);
	}

}
