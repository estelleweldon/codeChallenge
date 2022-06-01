package com.mindex.challenge.data;

import java.util.List;

public class CompensationReport {
    private List<Compensation> compensationList;

    public CompensationReport() {}

    public CompensationReport(List<Compensation> compensationList) {
        this.compensationList = compensationList;
    }

    public List<Compensation> getCompensationList() {
        return compensationList;
    }

    public void setCompensationList(List<Compensation> compensationList) {
        this.compensationList = compensationList;
    }
}
