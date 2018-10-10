package org.lrima.network.annotations;

import org.lrima.network.supervisors.NaturalSelectionSupervisor;
import org.lrima.network.interfaces.NeuralNetworkSuperviser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AlgorithmInformation {
    String name ();
    String description();

    Class<? extends NeuralNetworkSuperviser> supervisor() default NaturalSelectionSupervisor.class;
}
