package ip;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

public class IP {
    
	public static boolean[][] optimize(int[][] P, int[][] Q, int[][][] templates, int[][] rangeType) {
		int K = templates.length;
		boolean[][] res = null;
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

			for (int k = 0; k < K; k++) {
				for (int[] q : Q) { // every point of boundary
					GRBLinExpr expr = new GRBLinExpr();
					for (int[] v : templates[k]) {
						Integer id = M.contain(P,M.sub(q, v));
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

			if (null != rangeType) {
				for (int k = 0; k < K; k++) {
					int[] range = rangeType[k];
					GRBLinExpr expr = new GRBLinExpr();
					for (int i = 0; i < P.length; i++)
						expr.addTerm(1, X[k][i]);
					model.addRange(expr, range[0], range[1], ""); // type constraints
				}
			}

			model.optimize();
			res = new boolean[K][P.length];
			for (int k = 0; k < K; k++) {
				for (int i = 0; i < P.length; i++) {
					res[k][i] = 0.5 < X[k][i].get(GRB.DoubleAttr.X);
					if(res[k][i]) {
//						int[] a= P[i];
//						System.out.println( k+" ["+ a[0]+","+a[1]+","+a[2]+"]");
						System.out.println( k+","+i);
					}
				}
			}
			model.dispose();
			
		} catch (GRBException e) {
			System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
		return res;
	}

}
