package com.mindex.challenge.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class Compensation {

    private String compensationId;
    @NotNull(message = "Employee is a mandatory field")
    private Employee employee;
    @NotNull(message = "Salary is a mandatory field")
    private Float salary;
    @NotNull(message = "Currency unit is a mandatory field")
    @NotBlank(message = "Currency unit must not be blank")
    private String currencyUnit;
    @NotNull(message = "Effective date is a mandatory field")
    private Date effectiveDate;

    public Compensation() {}

    public Compensation(Employee employee, Float salary, String currencyUnit, Date effectiveDate) {
        this.employee = employee;
        this.salary = salary;
        this.currencyUnit = currencyUnit;
        this.effectiveDate = effectiveDate;
    }

    public String getCompensationId() {
        return compensationId;
    }

    public void setCompensationId(String compensationId) {
        this.compensationId = compensationId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
