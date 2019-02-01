package jtl;

public class Sampler {
		//XML attribute id
		private int t;
		//XML element fisrtName
		private String lb;
		private int s;
		
		public int gett() {
			return t;
		}
		public void sett(int t) {
			this.t = t;
		}
		public String getlb() {
			return lb;
		}
		public void setlb(String lb) {
			this.lb = lb;
		}
		
		public int gets() {
			return s;
		}
		public void sets(int s) {
			this.s = s;
		}
				
		@Override
		public String toString() {
			return this.lb + ":" + this.t + ":" + this.s;
		}
}
