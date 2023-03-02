package com.company.comparator;

import com.company.operation.IOperation;

import java.util.Comparator;

public class OExtendedComparator implements Comparator<IOperation> {

    public int compare(IOperation a, IOperation b) {
        OPriorityComparator comparator = new OPriorityComparator();
        if (comparator.compare(a, b) == 0) {
            return a.getNumberOfOperationMode() - b.getNumberOfOperationMode();
        }
        return 0;
    }
}
