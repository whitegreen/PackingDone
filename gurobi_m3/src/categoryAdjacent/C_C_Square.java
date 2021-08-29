package categoryAdjacent;

import java.util.ArrayList;
import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import ip.M;
import processing.core.PApplet;

public class C_C_Square extends PApplet { // Category (0-3) <--> Category (8-9)
	private static final int DM=2;
	private int[][] P; // grid
	private int[][] _P_; // boundary, be void
	private static final int K = 10; // number of templates
	private int[][][] templates = new int[K][][]; // template shapes
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
		_P_ = expand_single(P); // boundary (should be void)
		println("P " + P.length + ", " + "Q " + _P_.length);

		optimize();

		cols = new int[P.length];
		for (int i = 0; i < P.length; i++)
			cols[i] = color(random(256), random(256), random(256), 200);
		rectMode(CENTER);
	}

	private void optimize( ) {  
		try {
			GRBModel model = new GRBModel(new GRBEnv());
			GRBVar[][] X = new GRBVar[K][P.length];
			for (int k = 0; k < K; k++)
				for (int i = 0; i < P.length; i++)
					X[k][i] = model.addVar(0, 1, 0, GRB.BINARY, "");// whether the reference point of k-type patch sits at P[i]

			GRBLinExpr exp_obj = new GRBLinExpr();
			for (int k = 0; k < K; k++)
				for (int i = 0; i < P.length; i++)
					exp_obj.addTerm(templates[k].length, X[k][i]);
			model.setObjective(exp_obj, GRB.MAXIMIZE); // objective

			// ******************************* pack on P, void on  _P_ ************************************
			for (int k = 0; k < K; k++) {
				for (int[] q : _P_) { // every point of boundary
					GRBLinExpr expr = new GRBLinExpr();
					for (int[] v : templates[k]) {
						Integer id = M.contain(P, M.sub(q, v));
						if (null != id)
							expr.addTerm(1, X[k][id]);
					}
					model.addConstr(expr, GRB.EQUAL, 0, ""); // nothing on boundary
				}
			}

			for (int[] p : P) { // every point of grid
				GRBLinExpr expr = new GRBLinExpr();
				for (int k = 0; k < K; k++) {
					for (int[] v : templates[k]) {
						Integer id = M.contain(P, M.sub(p, v));
						if (null != id)
							expr.addTerm(1, X[k][id]); // each point is occupied by at most 1 template
					}
				}
				model.addConstr(expr, GRB.LESS_EQUAL, 1, "");
			}

			for (int k = 4; k < 8; k++) {// types other than Category (0-3) & Category (8-9)
				GRBLinExpr typepr = new GRBLinExpr();
				for (int i = 0; i < P.length; i++)
					typepr.addTerm(1, X[k][i]);
				model.addConstr(typepr, GRB.EQUAL, 2, "");// one type , tow copies
			}

			// ******************************* category-category adjacency ************************************
			for (int tj = 0; tj < 4; tj++) { // category A (0-3)
				// given a tj, there must be one adjacent from category B (8-9)
				for (int i = 0; i < P.length; i++) {
					GRBLinExpr expr = new GRBLinExpr();
					for (int ti = 8; ti < 10; ti++) { // category B (8-9)
						int[][] AA = AA(ti, tj);
						for (int[] v : AA) {
							Integer id = M.contain(P, M.add(P[i], v));
							if (null != id)
								expr.addTerm(1, X[ti][id]);
						}
					}
					model.addConstr(expr, GRB.GREATER_EQUAL, X[tj][i], "");
				}
			}

			for (int tj = 8; tj < 10; tj++) { // category category B (8-9)
				// given a tj, there must be one adjacent from category A (0-3)
				for (int i = 0; i < P.length; i++) {
					GRBLinExpr expr = new GRBLinExpr();
					for (int ti = 0; ti < 4; ti++) { // category A (0-3)
						int[][] AA = AA(ti, tj);
						for (int[] v : AA) {
							Integer id = M.contain(P, M.add(P[i], v));
							if (null != id)
								expr.addTerm(1, X[ti][id]);
						}
					}
					model.addConstr(expr, GRB.GREATER_EQUAL, X[tj][i], "");
				}
			}
			// *******************************  solve *********************************
			model.optimize();
			res = new boolean[K][P.length];
			for (int k = 0; k < K; k++) {
				for (int i = 0; i < P.length; i++) {
					res[k][i] = 0.5 < X[k][i].get(GRB.DoubleAttr.X);
					if (res[k][i]) 
						System.out.println(k + "," + i);
				}
			}
			model.dispose();

		} catch (GRBException e) {
			System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
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

	private int[][] AA(int ka, int kb) { // given A[kb] at (0,0)
		int[] mina = new int[DM];
		int[] maxa = new int[DM];
		int[] minb = new int[DM];
		int[] maxb = new int[DM];
		for (int i = 0; i < DM; i++) {
			mina[i] = M.min(templates[ka], i);
			maxa[i] = M.max(templates[ka], i);
			minb[i] = M.min(templates[kb], i);
			maxb[i] = M.max(templates[kb], i);
		}

		int[][] bound_B = expand(templates[kb]);
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int i = minb[0] - maxa[0] - 1; i <= maxb[0] - mina[0] + 1; i++) {
			for (int j = minb[1] - maxa[1] - 1; j <= maxb[1] - mina[1] + 1; j++) {
				int[] ca = { i, j };
				int[][] patch = M.addEach(templates[ka], ca);
				if (!M.overlap(templates[kb], patch) && M.overlap(bound_B, patch))
					list.add(ca);
			}
		}
		return M.toArray(list);
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
		// ******** draw results
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < P.length; i++) {
				if (8 == k || 9 == k)
					fill(255, 0, 0);
				else if (k < 4)
					fill(cols[i]);
				else
					fill(70);
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

	private int[][] expand_single(int[][] vs) {
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
