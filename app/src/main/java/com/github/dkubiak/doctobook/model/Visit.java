package com.github.dkubiak.doctobook.model;

import java.math.BigDecimal;
import java.util.Date;

public final class Visit {
    private long id;
    private String patientName;
    private Date date;
    private ProcedureType procedureType;
    private BigDecimal amount;
    private BigDecimal extraCosts;
    private int point;
    private Office office;

    public Visit(long id, String patientName, Date date, ProcedureType procedureType, BigDecimal amount, BigDecimal extraCosts, int point, Office office) {
        this.id = id;
        this.patientName = patientName;
        this.date = date;
        this.procedureType = procedureType;
        this.amount = amount;
        this.extraCosts = extraCosts;
        this.point = point;
        this.office = office;
    }

    public String getPatientName() {
        return patientName;
    }

    public Date getDate() {
        return date;
    }

    public ProcedureType getProcedureType() {
        return procedureType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getPoint() {
        return point;
    }

    public long getId() {
        return id;
    }

    public Office getOffice() {
        return office;
    }

    public BigDecimal getExtraCosts() {
        return extraCosts;
    }

    public final static class Builder {
        private long id;
        private String patientName;
        private Date date;
        private ProcedureType procedureType;
        private BigDecimal amount;
        private int point;
        private BigDecimal extraCosts;
        private Office office = new Office.Builder().createOffice();

        public Builder setPatientName(String patientName) {
            this.patientName = patientName;
            return this;
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder setProcedureType(ProcedureType procedureType) {
            this.procedureType = procedureType;
            return this;
        }

        public Builder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder setAmount(String amount) {
            this.amount = amount.length() == 0 ? BigDecimal.ZERO : new BigDecimal(amount);
            return this;
        }

        public Builder setExtraCosts(String extraCosts) {
            this.extraCosts = extraCosts.length() == 0 ? BigDecimal.ZERO : new BigDecimal(extraCosts);
            return this;
        }

        public Builder setPoint(int point) {
            this.point = point;
            return this;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setOffice(Office office) {
            this.office = office;
            return this;
        }


        public Visit createVisit() {
            return new Visit(id, patientName, date, procedureType, amount, extraCosts, point, office);
        }
    }

    public final static class ProcedureType {
        boolean prosthetics = false;
        boolean endodontics = false;
        boolean conservative = false;


        public ProcedureType(boolean prosthetics, boolean endodontics, boolean conservative) {
            this.prosthetics = prosthetics;
            this.endodontics = endodontics;
            this.conservative = conservative;
        }

        public boolean isProsthetics() {
            return prosthetics;
        }

        public boolean isEndodontics() {
            return endodontics;
        }

        public boolean isConservative() {
            return conservative;
        }

        public final static class Builder {
            private boolean prosthetics;
            private boolean endodontics;
            private boolean conservative;

            public Builder isProsthetics() {
                this.prosthetics = true;
                return this;
            }

            public Builder isEndodontics() {
                this.endodontics = true;
                return this;
            }

            public Builder isConservative() {
                this.conservative = true;
                return this;
            }

            public Visit.ProcedureType createProcedureType() {
                return new Visit.ProcedureType(prosthetics, endodontics, conservative);
            }
        }
    }
}
