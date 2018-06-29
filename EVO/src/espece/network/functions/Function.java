package espece.network.functions;

public class Function {
	public static IFunction SIGMOID = new IFunction() {
		public double getValue(double x) {
			return (1/( 1 + Math.pow(Math.E,(-1*x))));
		}
		
		public String getName() {
			return "Sigmoid";
		}
	};
	
	public static IFunction TANH = new IFunction() {
		public double getValue(double x) {
			return Math.tanh(x);
		}
		
		public String getName() {
			return "Tan h";
		}
	};
	
	static IFunction[] functions = {TANH};
	
	public static IFunction getRandom() {
		return functions[(int) (Math.random()*functions.length)];
	}
	
}
