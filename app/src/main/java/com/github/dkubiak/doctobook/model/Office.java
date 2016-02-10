package com.github.dkubiak.doctobook.model;

import java.math.BigDecimal;

public final class Office {
    long id;
    String name;
    BigDecimal commissionPublic;
    BigDecimal commissionPrivate;
    BigDecimal nfzConversion;

    public Office(long id, String name, BigDecimal commissionPublic, BigDecimal commissionPrivate, BigDecimal nfzConversion) {
        this.id = id;
        this.name = name;
        this.commissionPublic = commissionPublic;
        this.commissionPrivate = commissionPrivate;
        this.nfzConversion = nfzConversion;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCommissionPublic() {
        return commissionPublic;
    }

    public String getCommissionPublicAsString() {
        return commissionPublic != null ? String.valueOf(commissionPublic) : "";
    }

    public BigDecimal getCommissionPrivate() {
        return commissionPrivate;
    }

    public boolean isCommissionPrivate() {
        return commissionPrivate != null;
    }

    public BigDecimal getNfzConversion() {
        return nfzConversion;
    }

    public static final class Builder {
        private long id;
        private String name;
        private BigDecimal commissionPublic;
        private BigDecimal commissionPrivate;
        private BigDecimal nfzConversion;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCommissionPublic(String commissionPublic) {
            this.commissionPublic = toBigDecimal(commissionPublic);
            return this;
        }

        public Builder setCommissionPrivate(String commissionPrivate) {
            this.commissionPrivate = toBigDecimal(commissionPrivate);
            return this;
        }

        public Builder setNfzConversion(String nfzConversion) {
            this.nfzConversion = toBigDecimal(nfzConversion);
            return this;
        }

        private BigDecimal toBigDecimal(String value) {
            if (value == null) {
                return null;
            }
            return value.length() == 0 ? null : new BigDecimal(value);
        }

        public Office createOffice() {
            return new Office(id, name, commissionPublic, commissionPrivate, nfzConversion);
        }
    }
}
