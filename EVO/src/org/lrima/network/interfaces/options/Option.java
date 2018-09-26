package org.lrima.espece.network.interfaces.options;

import javax.swing.*;

public interface Option<T> {
    T getValue();
    JComponent show();
}
