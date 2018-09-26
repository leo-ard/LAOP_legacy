package org.lrima.network.interfaces.options;

import javax.swing.*;

public interface Option<T> {
    T getValue();
    JComponent show();
}
