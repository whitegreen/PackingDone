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
import quasi.AmmBeen;

//same results by C_C_AmmBeen.py
public class C_C_AmmBeen extends PApplet {   // Category (0-3)  <-->  Category (4-15)
	private static final int en =7 ;
	private static final int DM=AmmBeen.DM;
	private AmmBeen amm;
	private int[][] P; // grid
	private int[][] _P_; //  boundary, be void
	private double[][][] nicePolys; //length == P.length
	private final int K = 4+12+4; // number of templates
//	private static final int keyType=0;  //0 hori, 1 slash, 2 vertical, 3 backslash
	private int[][][] templates = new int[K][][]; // template shapes
	private boolean[][] res; // to be obtained from optimize()
	
	private int[] cols;
	private float sx = 80; //72,80 , 5-92
	private float sy = -sx;

	public void setup() {
		size(900, 900);
		amm = new AmmBeen(en);
		P = amm.P;
		AmmBeen extendamm = new AmmBeen(en + 2);
		nicePolys = new double[P.length][][];
		for (int i = 0; i < P.length; i++) {
			Integer eid = M.contain(extendamm.P, P[i]);
			if (null == eid)
				continue;
			nicePolys[i] = extendamm.poly_cell(eid, 0.5);
		}

		for (int i = 0; i < DM; i++) {
			templates[i] = new int[3][DM]; // 3-straight
			templates[i][0][i] = -1;
			templates[i][2][i] = 1;
		}
		for (int i = 0; i < DM; i++)
			for (int j = 0; j < DM - 1; j++)
				templates[DM + i * (DM - 1) + j] = new int[][] { templates[i][0], new int[DM], templates[(i +j+ 1) % DM][2] }; // L shape

		templates[16] = new int[][] { { 0, 0, 0, 0 }, { 1, 0, 0, 0 } }; // 2-straight
		templates[17] = new int[][] { { 0, 0, 0, 0 }, { 0, 1, 0, 0 } };
		templates[18] = new int[][] { { 0, 0, 0, 0 }, { 0, 0, 1, 0 } };
		templates[19] = new int[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 1 } };

		_P_ = AmmBeen.expand(P); // boundary (should be void)
		println("P " + P.length + ", " + "Q " + _P_.length);

		optimize();

		cols = new int[P.length];
		for (int i = 0; i < P.length; i++)
			cols[i] = color(random(256), random(256), random(256), 200);
	}

	private void optimize( ) {  
		try {
			GRBModel model = new GRBModel(new GRBEnv());
			GRBVar[][] X = new GRBVar[K][P.length];
			for (int k = 0; k < K; k++)
				for (int i = 0; i < P.length; i++)
					X[k][i] = model.addVar(0, 1, 0, GRB.BINARY, "");

			GRBLinExpr exp_obj = new GRBLinExpr();
			for (int k = 0; k < K; k++)
				for (int i = 0; i < P.length; i++)
					exp_obj.addTerm(templates[k].length, X[k][i]);
			model.setObjective(exp_obj, GRB.MAXIMIZE); // objective

			for (int k = 0; k < K; k++) {
				for (int[] q : _P_) { // every point of boundary
					GRBLinExpr expr = new GRBLinExpr();
					for (int[] v : templates[k]) {
						Integer id = M.contain(P,M.sub(q, v));
						if (null != id)
							expr.addTerm(1, X[k][id]);
					}
					model.addConstr(expr, GRB.EQUAL, 0, ""); // nothing on boundary
				}
			}

			for (int[] p : P) { // every point of site points
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

			GRBLinExpr typepr = new GRBLinExpr();
			for (int k = 16; k < 20; k++)
				for (int i = 0; i < P.length; i++)
					typepr.addTerm(1, X[k][i]);
			model.addConstr(typepr, GRB.LESS_EQUAL, 20, "");// sum of type 16-19 <= threshold

			// *********************************** category-category adjacency***************************************
			for (int tj = 0; tj < 4; tj++) {  //category A (0-3)
				// given a tj, there must be one adjacent from category B (4-15)
				for (int i = 0; i < P.length; i++) {
					GRBLinExpr expr = new GRBLinExpr();
					for (int ti = 4; ti < 16; ti++) { // category B (4-15)
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

			for (int tj = 4; tj < 16; tj++) { // category category B (4-15)
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
					if (res[k][i]) {
						// int[] a= P[i];
						// System.out.println( k+" ["+ a[0]+","+a[1]+"]");
						System.out.println(k + "," + i);
					}
				}
			}
			model.dispose();

		} catch (GRBException e) {
			System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
	}
	
	private int[][] AA( int ka, int kb) { // given A[kb] at (0,0)
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
		int[][] bound_B = AmmBeen.expand(templates[kb]);
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int i = minb[0] - maxa[0] - 1; i <= maxb[0] - mina[0] + 1; i++) {
			for (int j = minb[1] - maxa[1] - 1; j <= maxb[1] - mina[1] + 1; j++) {
				for (int k = minb[2] - maxa[2] - 1; k <= maxb[2] - mina[2] + 1; k++) {
					for (int l = minb[3] - maxa[3] - 1; l <= maxb[3] - mina[3] + 1; l++) {
						int[] ca = { i, j, k, l };
						int[][] patch = M.addEach(templates[ka], ca);
						if (!M.overlap(templates[kb], patch) && M.overlap(bound_B, patch))
							list.add(ca);
					}
				}
			}
		}
		return M.toArray(list);
	}

	public void draw() {
		background(255);
		translate(width / 2, height / 2);
		// ******** draw background cells ******** ******** ******** ******** ********
		fill(0);
		stroke(0);
		for (int i = 0; i < P.length; i++) {
			beginShape();
			for (double[] tp : nicePolys[i])
				vertex((float) (tp[0] * sx), (float) (tp[1] * sy));
			endShape(CLOSE);
		}
		// ******** draw resultant templates ******** ******** ******** ******** ********
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < P.length; i++) {
                if (k < DM)
					fill(255, 0,0);
				else if (k < 16)
					fill(cols[i]);
				else
					fill(255);
				
				if (res[k][i]) {
					int[] t = P[i];
					for (int[] p : templates[k]) {
						Integer id = M.contain(P, M.add(t, p));
						beginShape();
						for (double[] tp : nicePolys[id])
							vertex((float) (tp[0] * sx), (float) (tp[1] * sy));
						endShape(CLOSE);
					}
				}
			}
		}
		// draw lines ******** ******** ******** ******** ******** ******** ********
		stroke(200,100);
		for (int i = 0; i < P.length; i++) {
			double[] p = amm.vs[i];
			int[][] neis = AmmBeen.neighbor(P[i]);
			for (int nid : amm.neigh_ids[i]) {
				double[] tp = M.mul(AmmBeen.m_para, neis[nid]);
				line((float) (p[0] * sx), (float) (p[1] * sy), (float) (tp[0] * sx), (float) (tp[1] * sy));
			}
		}
		// draw Q dots ******** ******** ******** ******** ******** ******** ********
//		stroke(255, 0, 0);
//		noFill();
//		for (int i = 0; i < _P_.length; i++) {
//			double[] p = M.mul(AmmBeen.m_para, _P_[i]);
//			ellipse((float) (p[0] * sx), (float) (p[1] * sy), 5, 5);
//		}

	}

}
