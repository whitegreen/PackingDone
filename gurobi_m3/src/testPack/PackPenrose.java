package testPack;

import ip.IP;
import ip.M;
import processing.core.PApplet;
import quasi.Penrose;

public class PackPenrose extends PApplet { //same results by Pack_square.py
	private static final int en =5;  // 5-P.length=131
	private Penrose pen;
	private int[][] P; //  grid
	private int[][] _P_; //  boundary
	private double[][][] nicePolys; //length == P.length
	private static final int K = 5+20; // number of templates
	private int[][][] templates = new int[K][][]; //  template  shapes
	private boolean[][] res; // to be obtained from optimize()
	private int[] cols;
	private float sx = 66;  
	private float sy = -sx;

	public void setup() {
		size(900, 900);
		pen = new Penrose(en);
		P = pen.P;
		_P_ = Penrose.expand(P); // boundary (should be void)
		println("P "+P.length + ", " +"Q "+ _P_.length);
		
		Penrose extendpen= new Penrose(en+1);
		nicePolys =new double[P.length][][];
		for (int i = 0; i < P.length; i++) {
			Integer eid= M.contain(extendpen.P, P[i]);
			if (null==eid)
				continue;
			nicePolys[i] = extendpen.poly_cell( eid, 0.5);
		}
		int DM=Penrose.DM;
		for (int i = 0; i < DM; i++) {
			templates[i] = new int[3][DM] ;   //3-straight
			templates[i][0][i] =-1; 
			templates[i][2][i] =1; 
		}
		for (int i = 0; i < DM; i++)
			for (int j = 0; j < DM-1; j++)
				templates[DM + i * (DM-1) + j] = new int[][] { templates[i][0], new int[DM], templates[(i + 1) % DM][2] }; // L shape
//		for (int[] p : P) {
//			String s = " (";
//			for (int i=0;i<p.length;i++)
//				  s += p[i] +  (i<p.length-1? "," : "");
//			s += "),";
//			print(s);
//		}
//		println();

		int[][] mm = new int[K][];
		for (int k = 0; k <K; k++)
			mm[k] = new int[] { 1, 40 };
		res = IP.optimize(P, _P_, templates, mm);

		cols = new int[P.length];
		for (int i = 0; i < P.length; i++)
			cols[i] = color(random(256), random(256), random(256));
	}
	
	public void draw() {
		background(255);
		translate(width / 2, height / 2);
		// ******** draw background cells ******** ******** ******** ******** ********
		fill(240,100);
		stroke(0);
		for (int i = 0; i < P.length; i++) {
			fill(240,100);
			beginShape();
			for (double[] tp : nicePolys[i])
				vertex((float) (tp[0] * sx), (float) (tp[1] * sy));
			endShape(CLOSE);
		}
		// ******** draw resultant templates ******** ******** ******** ******** ********
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < P.length; i++) {
				fill(cols[i]);
				if (res[k][i]) {
					int[] t = P[i];
					for (int[] p : templates[k]) {
						Integer id = M.contain(P,  M.add(t,p));
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
			double[] p = pen.vs[i];
			int[][] neis = Penrose.neighbor(P[i]);
			for (int nid : pen.neigh_ids[i]) {
				double[] tp = M.mul(pen.m_para, neis[nid]);
				line((float) (p[0] * sx), (float) (p[1] * sy), (float) (tp[0] * sx), (float) (tp[1] * sy));
			}
		}
		// draw Q dots ******** ******** ******** ******** ******** ******** ********
//		stroke(255, 0, 0);
//		noFill();
//		for (int i = 0; i < _P_.length; i++) {
//			double[] p = M.mul(pen.m_para, _P_[i]);
//			ellipse((float) (p[0] * sx), (float) (p[1] * sy), 5, 5);
//		}

	}

}
