package quasi;

import java.util.ArrayList;
import ip.M;

public class Penrose {
	public static final int DM=5; //dimension of index
	private double[][] basis = new double[DM][DM];
	public double[][] m_orth, m_para;
	public double[][] vs;
	public int[][] neigh_ids;   // [P.length][ 10 ]
	public boolean[] istri;
	private ArrayList[] geo_neigh;
	public int[][] P; 
	
	public Penrose(int en) {
		double[][] mb= new double[DM][DM];
		for (int i = 0; i < DM; i++) {
			double a = Math.PI * 0.4 * i;
			mb[i] = new double[] { Math.cos(a), Math.sin(a), Math.cos(2 * a), Math.sin(2 * a), Math.sqrt(0.5)};
		}
		basis = M.transpose(mb);
		m_para =  new double[][]{ basis[0], basis[1] }; // parallel operator (physical space)
		m_orth =  new double[][]{ basis[2], basis[3], basis[4] }; 
		window(en);  //create P
		// ***************************************************
		neigh_ids = new int[P.length][];
		for (int i = 0; i < P.length; i++) {
			int[][] neis = neighbor(P[i]);  //length =10
			boolean[] ava = new boolean[neis.length];//10
			ArrayList<Integer> ist = new ArrayList<Integer>();
			ArrayList<Integer> prenon = new ArrayList<Integer>();
			ArrayList<Integer> aftnon = new ArrayList<Integer>();
			for (int j = 0; j < ava.length; j++) {//10
				ava[j] = M.has(P, neis[j]);
				if (ava[j]) {
					ist.add(j);
				}else {
					if(j<5)
					  prenon.add(j);
					else
						aftnon.add(j);
				}
			}
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
				if (M.dist(vs[i], vs[j]) < 1.2) { // 1.175-1.176
					st.add(vs[j]);
					as.add(Math.atan2(vs[j][1] - vs[i][1], vs[j][0] - vs[i][0]));
				}
			}
//			System.out.println(st.size());
			if (5 > st.size())
				geo_neigh[i] = new ArrayList();
			else
				geo_neigh[i] = M.sort(st, as);
		}
	}

	private void window(int en) {
		int sn = 32;
		double[][] cube_ps = new double[sn][];
		int count = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int m = 0; m < 2; m++) {
					for (int n = 0; n < 2; n++) {
						for (int l = 0; l < 2; l++) {
							double[] tp =  { i - 0.5, j - 0.5, m - 0.5, n - 0.5, l - 0.5 };
							cube_ps[count] =tp;
							count++;
						}
					}
				}
			}
		}
		double[][] pps = new double[sn][];
		for (int i = 0; i < sn; i++)
			pps[i] = M.mul(m_orth, cube_ps[i]);
		int[][] faces = polytope3D();
		double[][] face_ps = new double[faces.length][];
		double[][] face_ns = new double[faces.length][];
		for (int i = 0; i < faces.length; i++) {
			int[] ids = faces[i];
			double[] p0 = pps[ids[0]];
			double[] p1 = pps[ids[1]];
			double[] p2 = pps[ids[2]];
			face_ps[i] = p1;
			double[] nor = M.cross(M.sub(p0, p1), M.sub(p2, p1));
			M._normalize(nor);
			if (M.dot(p1, nor) > 0)
				M._scale(nor, -1); // inward
			face_ns[i] = nor;
		}
		int s = en / 2;
		ArrayList<int[]> list = new ArrayList<int[]>();
		double[] shift=M.scale(basis[4], Math.sqrt(2.0)/4);
		for (int i = 0; i < en; i++) {
			for (int j = 0; j < en; j++) {
				for (int m = 0; m < en; m++) {
					for (int n = 0; n < en; n++) {
						for (int l = 0; l < en; l++) {
							double[] tt=  {i - s, j - s, m - s, n - s, l - s };
							double[] tp = M.add(tt, shift);
							if (insidePolytope(M.mul(m_orth, tp), faces, face_ps, face_ns))
								list.add(new int[] { i - s, j - s, m - s, n - s, l - s });
						}
					}
				}
			}
		}
		P = new int[list.size()][];
		for (int i = 0; i < P.length; i++)
			P[i] = list.get(i);
	}
	private static boolean insidePolytope(double[] p,  int[][] faces, double[][] face_ps, double[][] face_ns) {
		for (int i = 0; i < faces.length; i++) {
			double[] v = M.sub(p, face_ps[i]);
			double t = M.dot(v, face_ns[i]); // normal are inward
			if (t < 0)
				return false;
		}
		return true;
	}
	private int[][] polytope3D() {
		int[] c0 = { 27, 30, 23, 29, 15 }; // for 31
		int[] c1 = { 1, 4, 16, 2, 8 }; // for 0
		int[] c2 = { 26, 18, 22, 20, 21, 5, 13, 9, 11, 10 }; // circular
		int[][] fs = new int[20][];
		for (int i = 0; i < DM; i++) {
			fs[i] = new int[] { 31, c0[i], c0[(i + 1) % 5] };
			fs[DM + i] = new int[] { 0, c1[i], c1[(i + 1) % 5] };
		}
		for (int i = 0; i < 10; i++) {
			fs[10 + i] = new int[] { c2[i], c2[(i + 1) % 10], c2[(i + 2) % 10] };
		}
		return fs;
	}
	
	public static int[][] neighbor(int[] a) {
		int[][] neis = new int[2*DM][];
		for (int i = 0; i < neis.length; i++) 
			neis[i] =  a.clone();
		for (int i = 0; i < DM; i++) {
			neis[i][i] +=1;
			neis[DM+i][i] -=1;
		}
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
		cornerMerge(re);
		return  re;
	}

	private static void cornerMerge(double[][] ps) {
		if (4 > ps.length)
			return;
		for (int i = 0; i < ps.length; i++) {
			double[] pa = ps[(i - 1 + ps.length) % ps.length];
			double[] pb = ps[i];
			double[] pc = ps[(i + 1 + ps.length) % ps.length];
			double[] pd = ps[(i + 2 + ps.length) % ps.length];
			double[] it = M.segment_intersect(new double[][] { pa, pb }, new double[][] { pc, pd });
			if (null != it) {
				pb[0] = pc[0] = it[0];
				pb[1] = pc[1] = it[1];
				return;
			}
		}
	}

}
