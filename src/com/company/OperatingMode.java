package com.company;

public enum OperatingMode {
    canBeInterrupted,
    canNotBeInterrupted;

    public static OperatingMode modeSelection(int currentOperatingMode)
    {
        switch (currentOperatingMode)
        {
            case 0:
                return OperatingMode.canNotBeInterrupted;
            default:
                return OperatingMode.canBeInterrupted;
        }
    }
}
