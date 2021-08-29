package quasi;

import java.util.ArrayList;
import ip.M;

public class AmmBeen  {
	public static final int DM=4; //dimension of index
	private static final double k = 0.5 * Math.sqrt(2);
	private static final double[][] mat = { { k, 0.5, 0, -0.5 }, { 0, 0.5, k, 0.5 }, { k, -0.5, 0, 0.5 }, { 0, 0.5, -k, 0.5 } };
	public static final double[][] m_para = { mat[0], mat[1] }; // parallel operator (physical space)
	public static final double[][] m_orth = { mat[2], mat[3] }; // perpendicular operator
	public double[][] vs;
	public int[][] neigh_ids;   // [ps.length][ 8 ]
	private ArrayList[] geo_neigh;
	public int[][] P; //site grid, for rooms

	public AmmBeen(int en) {
		double[][] oct_bound = orth_bound();
		ArrayList<int[]> list = new ArrayList<int[]>();
		int s = en / 2;
		for (int i = 0; i < en; i++) {
			for (int j = 0; j < en; j++) {
				for (int m = 0; m < en; m++) {
					for (int n = 0; n < en; n++) {
						int[] id = { i - s, j - s, m - s, n - s };
						double[] v = M.mul(m_orth, id);
						if (M.inside(v, oct_bound))
							list.add(id);
					}
				}
			}
		}
		P = new int[list.size()][];
		for (int i = 0; i < P.length; i++)
			P[i] = list.get(i);
		// ***************************************************
//		full_neigh = new boolean[P.length][];
		neigh_ids = new int[P.length][];
		for (int i = 0; i < P.length; i++) {
			int[][] neis = neighbor(P[i]);
			boolean[] ava = new boolean[neis.length];
			ArrayList<Integer> ist = new ArrayList<Integer>();
			for (int j = 0; j < ava.length; j++) {
				ava[j] = M.has(P, neis[j]);
				if (ava[j])
					ist.add(j);
			}
//			full_neigh[i] = ava;
			neigh_ids[i] = M.to_array(ist);
		}
		// ***************************************************
		vs=new double[ P.length][];
		for (int i = 0; i < P.length; i++)
			vs[i] = M.mul(m_para, P[i]);
		geo_neigh = new ArrayList[P.length];
		for (int i = 0; i < P.length; i++) {
			ArrayList<double[]> st = new ArrayList<double[]>();
			ArrayList<Double> as = new ArrayList<Double>();
			for (int j = 0; j < P.length; j++) {
				if (i == j)
					continue;
				if (M.dist(vs[i], vs[j]) < 0.99) {
					st.add(vs[j]);
					as.add( Math.atan2( vs[j][1] -vs[i][1], vs[j][0] -vs[i][0]));
				}
			}
//			println(st.size());
			if (5 > st.size())
				geo_neigh[i] = new ArrayList();
			else
				geo_neigh[i] = M.sort(st, as);
		}
	}

	private double[][] orth_bound() {
		double[][] oct_bound = new double[8][];
		double[][] cube_ps = new double[16][];
		int count = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int m = 0; m < 2; m++) {
					for (int n = 0; n < 2; n++) {
						cube_ps[count] = new double[] { i - 0.5, j - 0.5, m - 0.5, n - 0.5 };
						count++;
					}
				}
			}
		}
		int[] ids = { 9, 13, 5, 4, 6, 2, 10, 11 }; // see AmmBeen2
		for (int i = 0; i < oct_bound.length; i++) {
			int id = ids[i];
			oct_bound[i] = M.mul(m_orth, cube_ps[id]);
		}
		return oct_bound;
	}

	public static int[][] neighbor(int[] a) {
		int[][] neis = { { a[0] +1, a[1], a[2], a[3] }, { a[0], a[1] + 1, a[2], a[3] }, { a[0], a[1], a[2] + 1, a[3] },  { a[0], a[1], a[2], a[3] + 1 },
				{ a[0] - 1, a[1], a[2], a[3] },  { a[0], a[1] - 1, a[2], a[3] }, { a[0], a[1], a[2] - 1, a[3] },  { a[0], a[1], a[2], a[3] - 1 } };
		return neis;
	}

	public static int[][] expand(int[][] vs) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int[] p : vs) {
			int[][] bs=neighbor(p);
			for (int[] b : bs) {
				if (M.has(vs, b) || M.has(list, b))
					continue;
				list.add(b);
			}
		}
		return M.toArray(list);
	}

	public double[][] poly_cell(int i, double ss) {
        double[] p=vs[i];
        ArrayList<double[]> nps=  geo_neigh[i];
        int length=nps.size();
        double[][] re=new double[length][];
		for (int j = 0; j < length; j++) {
			int k= (j+1)% length;
			double[] aj = M.sub(nps.get(j), p);
			double[] _aj= { aj[1], -  aj[0]};
			double[] mj= M.between(ss, p , nps.get(j));
			
			double[] ak= M.sub(nps.get(k), p);
			double[] _ak= { ak[1], -  ak[0]};
			double[] mk= M.between(ss, p ,nps.get(k));
			
			re[j]= M.lineIntersect(mj, _aj, mk, _ak);
		}
		return re;
	}

}
