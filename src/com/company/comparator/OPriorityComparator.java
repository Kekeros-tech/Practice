package com.company.comparator;

import com.company.operation.IOperation;
import com.company.operation.O_Priority;

import java.util.Comparator;

public class OPriorityComparator implements Comparator<IOperation> {
    public int compare(IOperation a, IOperation b) {
        if (((O_Priority) a).getPriority() == ((O_Priority) b).getPriority()) return 0;
        if (((O_Priority) a).getPriority() > ((O_Priority) b).getPriority()) return -1;
        return 1;
    }
}
