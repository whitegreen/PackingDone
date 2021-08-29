package ip;

import java.util.ArrayList;
import java.util.Collections;

public class M {
	
	public static double area(double[][] ps) { // signed
		double sum = 0;
		for (int i = 0; i < ps.length; i++) {
			double[] pa = ps[i];
			double[] pb = ps[(i + 1) % ps.length];
			sum += pa[1] * pb[0] - pa[0] * pb[1];
		}
		return 0.5f * sum;
	}

	public static double areaAbs(ArrayList<double[]> ps) { // signed
		double sum = 0;
		int size = ps.size();
		for (int i = 0; i < size; i++) {
			double[] pa = ps.get(i);
			double[] pb = ps.get((i + 1) % size);
			sum += pa[1] * pb[0] - pa[0] * pb[1];
		}
		return Math.abs(0.5f * sum);
	}

	public static double areaAbs(double[][] ps) {
		return Math.abs(area(ps));
	}
	
	public static double[] mean(double[][] ps){
		double[] re=new double[ps[0].length];
		for(int i=0;i<ps.length;i++){
			for(int j=0;j<re.length;j++)
				re[j]+=ps[i][j];
		}
		for(int j=0;j<re.length;j++)
			re[j]/=ps.length;
		return re;
	}
	
	public static boolean concide(double[] va, double[] vb) {
		double[] a = normalize(va);
		double[] b = normalize(vb);
		double dot = dot(a, b);
		double dd = dot * dot;
		return 0.00003 > Math.abs(1 - dd);
	}

	public static void _add(double[] a, double[] b) {
		for (int i = 0; i < a.length; i++) 
			a[i] += b[i];
	}
	public static void _sub(double[] a, double[] b) {
		for (int i = 0; i < a.length; i++) 
			a[i] -= b[i];
	}
	public static double[][] mul(double[][] a, double[][] b) {
		int row = a.length;
		int col = b[0].length;
		double[][] re = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				for (int k = 0; k < b.length; k++)
					re[i][j] += a[i][k] * b[k][j];
			}
		}
		return re;
	}

	public static void _normalize(double[] v) {
		double mag = mag(v);
		for (int i = 0; i < v.length; i++) 
			v[i] /= mag;
	}
	public static double[][] sub(double[][] a, double[][] b) {
		int row = a.length;
		int col = a[0].length;
		double[][] re = new double[row][col];
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++)
				re[i][j] = a[i][j] - b[i][j];
		return re;
	}

	public static boolean inside(double[] p, double[][] vs) {
		int i, j = vs.length - 1;
		boolean oddNodes = false;
		for (i = 0; i < vs.length; i++) {
			if ((vs[i][1] < p[1] && vs[j][1] >= p[1] || vs[j][1] < p[1] && vs[i][1] >= p[1]) && (vs[i][0] <= p[0] || vs[j][0] <= p[0])) {
				if (vs[i][0] + (p[1] - vs[i][1]) / (vs[j][1] - vs[i][1]) * (vs[j][0] - vs[i][0]) < p[0]) {
					oddNodes = !oddNodes;
				}
			}
			j = i;
		}
		return oddNodes;
	}
	
	public static double[][] scale(double[][] a, double s) {
		int row = a.length;
		int col = a[0].length;
		double[][] re = new double[row][col];
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++)
				re[i][j] = s * a[i][j];
		return re;
	}

	public static void _scale(double[][] a, double s) {
		int row = a.length;
		int col = a[0].length;
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++)
				a[i][j] *= s;
	}

	public static void _add(double[][] a, double[][] b) {
		int row = a.length;
		int col = a[0].length;
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++)
				a[i][j] += b[i][j];
	}

	public static double[][] add(double[][] a, double[][] b) {
		int row = a.length;
		int col = a[0].length;
		double[][] re = new double[row][col];
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++)
				re[i][j] = a[i][j] + b[i][j];
		return re;
	}

	public static double[][] transpose(double[][] v) {
		int row = v.length;
		int col = v[0].length;
		double[][] re = new double[col][row];
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++)
				re[j][i] = v[i][j];
		return re;
	}
	public static double[] mul(double[][] a, double[] b) {
		double[] re = new double[a.length];
		for (int i = 0; i < re.length; i++) {
			double sum = 0;
			for (int j = 0; j < b.length; j++)
				sum += a[i][j] * b[j];
			re[i] = sum;
		}
		return re;
	}
	public static double[] mul(double[][] a, int[] b) {
		double[] re = new double[a.length];
		for (int i = 0; i < re.length; i++) {
			double sum = 0;
			for (int j = 0; j < b.length; j++)
				sum += a[i][j] * b[j];
			re[i] = sum;
		}
		return re;
	}

	public static double[] square(double[] c) {
		double x = c[0];
		double y = c[1];
		return new double[] { x * x - y * y, 2 * x * y };
	}

	public static double mag_sq(double[] c) {
		double sum=0;
		for(int i=0;i<c.length;i++)
			sum+=c[i] * c[i];
		return sum;
	}
	
	public static double dist(double[] a, double[] b) {
		double sum=0;
		for(int i=0;i<a.length;i++)
			sum+=(a[i]-b[i]) * (a[i]-b[i]) ;
		return Math.sqrt(sum);
	}
	
	public static int abs_manhattan_dist(int[] a, int[] b) {
		int sum = 0;
		for (int i = 0; i < a.length; i++)
			sum += Math.abs(a[i] - b[i]);
		return sum;
	}
	public static int manhattan_dist(int[] a, int[] b) {
		int sum = 0;
		for (int i = 0; i < a.length; i++)
			sum += a[i] - b[i];
		return sum;
	}

	public static double dist_sq(double[] a, double[] b) {
		double sum=0;
		for(int i=0;i<a.length;i++)
			sum+=(a[i]-b[i]) * (a[i]-b[i]) ;
		return sum;
	}

	public static double mag(double[] c) {
		double sum=0;
		for(int i=0;i<c.length;i++)
			sum+=c[i] * c[i];
		return Math.sqrt(sum);
	}

	public static double[] cross(double[] a, double[] b) {
		double x = a[1] * b[2] - a[2] * b[1];
		double y = a[2] * b[0] - a[0] * b[2];
		double z = a[0] * b[1] - a[1] * b[0];
		return new double[] { x, y, z };
	}

	public static double dot(double[] a, double[] b) {
		double sum = 0;
		for (int i = 0; i < a.length; i++)
			sum += a[i] * b[i];
		return sum;
	}
	
	public static double[] add(double[] a,  double[] b) {
		double[] re= new double[a.length];
		for(int i=0;i<a.length;i++)
		  re[i]=a[i]+b[i];
		return re;
	}

	public static float[] add(float[] a, float[] b) {
		float[] re = new float[a.length];
		for (int i = 0; i < a.length; i++)
			re[i] = a[i] + b[i];
		return re;
	}

	public static int[] add(int[] a,  int[] b) {
		int[] re= new int[a.length];
		for(int i=0;i<a.length;i++)
		  re[i]=a[i]+b[i];
		return re;
	}

	public static double[][] addEach(double[][] ps, double[] b) {
		double[][] re = new double[ps.length][];
		for (int i = 0; i < ps.length; i++)
			re[i] = add(ps[i], b);
		return re;
	}
	
	public static int[][] addEach(int[][] ps, int[] b) {
		int[][] re = new int[ps.length][];
		for (int i = 0; i < ps.length; i++)
			re[i] = add(ps[i], b);
		return re;
	}
	
	
	public static double[] sub(double[] a,  double[] b) {
		double[] re= new double[a.length];
		for(int i=0;i<a.length;i++)
		  re[i]=a[i]-b[i];
		return re;
	}
	
	public static int[] sub(int[] a,  int[] b) {
		int[] re= new int[a.length];
		for(int i=0;i<a.length;i++)
		  re[i]=a[i]-b[i];
		return re;
	}
	
	public static int[] sub(int[] a, int[] b, Integer size) {
//		int len = null == size ? a.length : size;
//		int[] re = new int[len];
//		for (int i = 0; i < len; i++)
//			re[i] = a[i] - b[i];
		// return re;
		int[] re = new int[a.length];
		for (int i = 0; i < a.length; i++)
			re[i] = i < size ? a[i] - b[i] : b[i];
		return re;
	}
	
	public static double[] add(double[] a, double s, double[] b, double t) {
		double[] re= new double[a.length];
		for(int i=0;i<a.length;i++)
		  re[i]=a[i]*s+b[i]*t;
		return re;
	}
	
	public static int min(int[][] vs, int k) {
		int min = Integer.MAX_VALUE;
		for (int[] p : vs) {
			if (min > p[k])
				min = p[k];
		}
		return min;
	}
	public static int max(int[][] vs, int k) {
		int max = Integer.MIN_VALUE;
		for (int[] p : vs) {
			if (max < p[k])
				max = p[k];
		}
		return max;
	}
	public static double[] scale(double[] a, double s) {
		double[] re= new double[a.length];
		for(int i=0;i<a.length;i++)
		  re[i]=a[i]*s;
		return re;
	}
	
	public static void _scale(double[] a, double s) {
		for(int i=0;i<a.length;i++)
		  a[i]*=s;
	}
	
	public static double[] normalize( double[] a) {
		double[] re= new double[a.length];
		double sum=0;
		for(int i=0;i<a.length;i++)
			sum+=a[i]*a[i];
		double d= Math.sqrt(sum);
		for(int i=0;i<a.length;i++)
			re[i]=a[i]/d;
		return re;
	}

	public static boolean is_intersect(double[][] m, double[][] n) {// by QJP
		double ma = area(m[0], n[0], n[1]);
		double mb = area(m[1], n[0], n[1]);
		if (ma * mb > 0)
			return false;
		double na = area(n[0], m[0], m[1]);
		double nb = area(n[1], m[0], m[1]);
		if (na * nb > 0)
			return false;
		return true;
	}
	public static double[] segment_intersect(double[][] m, double[][] n) {// by QJP
		double ma = area(m[0], n[0], n[1]);
		double mb = area(m[1], n[0], n[1]);
		if (ma * mb > 0)
			return null;
		double na = area(n[0], m[0], m[1]);
		double nb = area(n[1], m[0], m[1]);
		if (na * nb > 0)
			return null;

		double fx;// now they must cross
		if (Math.abs(m[0][0] - m[1][0]) > Math.abs(n[0][0] - n[1][0]))
			fx = (ma * m[1][0] - mb * m[0][0]) / (ma - mb);
		else
			fx = (na * n[1][0] - nb * n[0][0]) / (na - nb);
		double fy;
		if (Math.abs(m[0][1] - m[1][1]) > Math.abs(n[0][1] - n[1][1]))
			fy = (ma * m[1][1] - mb * m[0][1]) / (ma - mb);
		else
			fy = (na * n[1][1] - nb * n[0][1]) / (na - nb);

		if (Double.isNaN(fx) || Double.isNaN(fy)) { // two lines overlap
			return null;
		}
		return new double[]{fx, fy};
	}
	public static double area(double[] p0, double[] p1, double[] p2) { // only 2d
		double a = p1[0] * p0[1] - p0[0] * p1[1] + p2[0] * p1[1] - p1[0] * p2[1] + p0[0] * p2[1] - p2[0] * p0[1];
		return 0.5 * a;
	}

	public static double[] between(double s, double[] a,  double[] b) {
		double[] re= new double[a.length];
		for(int i=0;i<a.length;i++)
		  re[i]=a[i]* (1-s)+b[i]*s;
		return re;
	}
	public static double[] sqRoot(double[] c) {
		double x = c[0];
		double y = c[1];
		double d = Math.sqrt(x * x + y * y);
		double re = Math.sqrt(0.5 * (x + d));
		double im = (y < 0 ? -1 : 1) * Math.sqrt(0.5 * (-x + d));
		return new double[] { re, im };
	}

	public static double[] mul(double[] c1, double[] c2) {
		double a = c1[0];
		double b = c1[1];
		double c = c2[0];
		double d = c2[1];
		return new double[] { a * c - b * d, b * c + a * d };
	}

	public static double[] divide(double[] c1, double[] c2) {
		double a = c1[0];
		double b = c1[1];
		double c = c2[0];
		double d = c2[1];
		double x = (a * c + b * d) / (c * c + d * d);
		double y = (b * c - a * d) / (c * c + d * d);
		return new double[] { x, y };
	}
	public static double[] rot(double cos, double sin, double[] axis, double[] v){
		double[] fst = scale(v, cos);
		double[] scn = scale(axis, (1 - cos) * M.dot(axis, v));
		double[] trd = scale(M.cross(axis, v), sin);
		return add(fst, add(scn, trd));
	}
	public static double[] rot(double ang, double[] axis, double[] v){
		double cos = Math.cos(ang);
		double sin = Math.sin(ang);
		double[] fst = scale(v, cos);
		double[] scn = scale(axis, (1 - cos) * M.dot(axis, v));
		double[] trd = scale(M.cross(axis, v), sin);
		return add(fst, add(scn, trd));
	}
	
	public static double[] ln(double[] z){
		return new double[]{ Math.log(mag(z)),   Math.atan2(z[1], z[0]) };
	}

	public static boolean equal(int[] a, int[] b) {
		for (int i = 0; i < a.length; i++)
			if (a[i] != b[i])
				return false;
		return true;
	}
	public static Integer contain(int[][] list, int[] p) {
		for (int i = 0; i < list.length; i++) {
			if (M.equal(list[i], p))
				return i;
		}
		return null;
	}

	public static boolean has(int[] list, int a) {
		for (int v : list)
			if (v == a)
				return true;
		return false;
	}

	
	public static boolean has(int[][] list, int[] p) {
		for (int[] v : list)
			if (equal(v, p))
				return true;
		return false;
	}

	public static boolean overlap(int[][] as, int[][] bs) {
		for(int[] a: as) {
			if( has(bs, a))
				return true;
		}
		return false;
	}
	
	public static boolean has(ArrayList<int[]> list, int[] p) {
		for (int[] v : list)
			if (equal(v, p))
				return true;
		return false;
	}
	
	public static int[][] toArray(ArrayList<int[]> list) {
		int len = list.size();
		int[][] re = new int[len][];
		for (int i = 0; i < len; i++)
			re[i] = list.get(i);
		return re;
	}

	public static int[] to_array(ArrayList<Integer> list) {
		int len = list.size();
		int[] re = new int[len];
		for (int i = 0; i < len; i++)
			re[i] = list.get(i);
		return re;
	}
	
	public static double[] lineIntersect(double[] p0, double[] n0, double[] p1, double[] n1) { // in 2d
		double cross_base = kross(n0, n1);
//		if (Math.abs(cross_base) < denominator_lim)// parallel
//			return null;
		double[] d = sub(p1, p0);
		double s = kross(d, n1) / cross_base;
		return new double[] { p0[0] + s * n0[0], p0[1] + s * n0[1] };
	}
	public static double kross(double[] a, double[] b) {
		return a[0] * b[1] - a[1] * b[0];
	}

//	public static double min(ArrayList<Double> as) {
//		for (int i = 0; i < as.size(); i++) {
//		
//				return i;
//		}
//		return null;
//	}

	public static ArrayList<double[]> sort(ArrayList<double[]> ps, ArrayList<Double> as) {
		int length = ps.size();
		ArrayList<double[]> re = new ArrayList<double[]>(length);
		for (int i = 0; i < length; i++) {
			int id = as.indexOf(Collections.min(as));
			as.remove(id);
			re.add(ps.remove(id));
		}
		return re;
	}
}