package com.github.dkubiak.doctobook.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dawid.kubiak on 10/01/16.
 */
public final class Visit {
    long id;
    String patientName;
    Date date;
    ProcedureType procedureType;
    BigDecimal amount;
    int point;

    String officeName;
    BigDecimal commissionPublic;
    BigDecimal commissionPrivate;
    BigDecimal nfzConversion;

    public Visit(long id, String patientName, Date date, ProcedureType procedureType, BigDecimal amount, int point) {
        this.id = id;
        this.patientName = patientName;
        this.date = date;
        this.procedureType = procedureType;
        this.amount = amount;
        this.point = point;
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

    public final static class Builder {
        private long id;
        private String patientName;
        private Date date;
        private ProcedureType procedureType;
        private BigDecimal amount;
        private int point;

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

        public Builder setPoint(int point) {
            this.point = point;
            return this;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }


        public Visit createVisit() {
            return new Visit(id, patientName, date, procedureType, amount, point);
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
