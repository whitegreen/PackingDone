package testPack;

import java.util.ArrayList;
import ip.IP;
import ip.M;
import processing.core.PApplet;

public class PackSquare_linear extends PApplet {
	private int[][] P; //  grid
	private int[][] _P_; //  boundary, be void
	private static final int K = 4; // number of templates
	private int[][][] templates = new int[K][][]; //  template shapes
	private boolean[][] res; // to be obtained from optimize()
	private int[] cols;
	private float sx = 40;
	private float sy = -sx;
	private final int wid = 10 + 2 + 1;
//	private final int hei = 8 + 2 + 2;
	
	public void setup() {
		size(1000, 800);
		templates[0] = new int[][] { {0}, {1}, {wid} };
		templates[1] = new int[][] { { 0 }, { -1 }, { -wid } };
		templates[2] = new int[][] { { wid }, { 0 }, { -wid } };
		templates[3] = new int[][] { { wid }, { 0 }, { -1 }, { -2 } };
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int x = 1 + 1; x < 11 + 1; x++)
			for (int y = 1 + 2; y < 9 + 2; y++)
				list.add(new int[] { x + y * wid });
		list.add(new int[] { 1 + 3 * wid }); // { -1, 0 }
		list.add(new int[] { 1 + 4 * wid }); // { -1, 1 }
		list.add(new int[] { 8 + 2 * wid }); // { 6, -1 }
		list.add(new int[] { 8 + 1 * wid }); // { 6, -2 }

		P = M.toArray(list); // available cells
		_P_ = expand(P); // boundary (should be void)
		
		int[][] mm = new int[K][];
		for (int k = 0; k < K; k++)
			mm[k] = new int[] { 1, 7 };
		res = IP.optimize(P, _P_, templates, mm);

		cols = new int[P.length];
		for (int i = 0; i < P.length; i++)
			cols[i] = color(random(256), random(256), random(256));
		rectMode(CENTER);
	}

	public void draw() {
		background(255);
		translate(300, 500);
		stroke(0);
		fill(255);
		for (int[] p : P)
			rect((p[0] % wid) * sx, (p[0] / wid) * sy, sx, sx); // draw grid (for rooms)
		fill(100);
		for (int[] p : _P_)
			rect((p[0] % wid) * sx, (p[0] / wid) * sy, sx - 8, sx - 8); // draw boundary (be void)
		// ******** draw results
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < P.length; i++) {
				fill(cols[i]);
				if (res[k][i]) {
					int[] v = P[i];
					for (int[] p : templates[k]) {
						int[] t = M.add(v, p);
						rect((t[0] % wid) * sx, (t[0] / wid) * sy, sx, sx);
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
			int[][] bs = { {p[0] - 1}, {p[0] + 1}, {p[0]- wid}, {p[0] + wid} };
			for (int[] b : bs) {
				if (M.has(vs, b) || M.has(list, b))
					continue;
				list.add(b);
			}
		}
		return M.toArray(list);
	}

}
