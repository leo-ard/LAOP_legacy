package org.lrima.network.functions;

import java.io.Serializable;

public interface IFunction extends Serializable {
	
	public double getValue(double x);
	public String getName();

}
