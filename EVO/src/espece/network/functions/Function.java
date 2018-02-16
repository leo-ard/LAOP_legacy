package espece.network.functions;

public class Function{
	public static IFunction SIGMOID = new IFunction() {
		public double getValue(double x) {
			return (1/( 1 + Math.pow(Math.E,(-1*x))));
		}
		
		public String getName() {
			return "Sigmoid";
		}
	};
	
	static IFunction[] functions = {SIGMOID};
	
	public static IFunction getRandom() {
		return functions[(int) (Math.random()*functions.length)];
	}
	
}
