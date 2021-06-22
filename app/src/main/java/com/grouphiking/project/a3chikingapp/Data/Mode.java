package com.grouphiking.project.a3chikingapp.Data;

public enum Mode {
    DAY(true),
    NIGHT(false);

    private final boolean value;

    private Mode(boolean value){
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }
}
