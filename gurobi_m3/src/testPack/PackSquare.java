package testPack;

import java.util.ArrayList;
import ip.IP;
import ip.M;
import processing.core.PApplet;

public class PackSquare extends PApplet {  //same results by Pack_square.py
	private int[][] P; // grid
	private int[][] _P_; // boundary, be void
	private static final int K=4; //number of templates
	private int[][][] templates= new int[K][][];  //all templates of shapes
	private boolean[][] res ;  // to be obtained from optimize()
	private int[] cols;
	private float sx = 40;
	private float sy =-sx;
	
	public void setup() {
		size(1000, 800);
		templates[0] = new int[][] { {0, 0}, {1, 0}, {0, 1} };
		templates[1] = new int[][] { {0, 0}, {-1, 0}, {0, -1} };
		templates[2] = new int[][] { {0, 1}, {0, 0}, {0, -1} };
		templates[3] = new int[][] { {1, 0}, {0, 0}, {-1, 0}, {-2, 0} };
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int x = 0; x < 10; x++)
			for (int y = 0; y < 8; y++)
				list.add(new int[] { x, y });
		list.add(new int[] { -1, 0 });
		list.add(new int[] { -1, 1 });
		list.add(new int[] { 6, -1 });
		list.add(new int[] { 6, -2 });

		P = M.toArray(list); // available cells
		_P_ = expand(P); // boundary (should be void)
		println("P "+P.length + ", " +"Q "+ _P_.length);
		
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
			rect(p[0] * sx, p[1] * sy, sx, sx); // draw grid 
		fill(100);
		for (int[] p : _P_)
			ellipse(p[0] * sx, p[1] * sy, sx - 8, sx - 8); // draw boundary (be void)
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


}
