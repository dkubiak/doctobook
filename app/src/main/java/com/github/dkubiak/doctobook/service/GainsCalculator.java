package com.github.dkubiak.doctobook.service;


import com.github.dkubiak.doctobook.DatabaseHelper;
import com.github.dkubiak.doctobook.model.Visit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class GainsCalculator {

    private DatabaseHelper db;

    public GainsCalculator() {
    }

    public GainsCalculator(DatabaseHelper db) {
        this.db = db;
    }

    public BigDecimal forSingleDay(Date date) {
        List<Visit> visits = db.getVisitByDay(date);
        BigDecimal sum = BigDecimal.ZERO;
        for (Visit visit : visits) {
            sum = sum.add(forSingleVisit(visit));
        }
        return sum;
    }

    public String forSingleDayWithRound(Date date) {
        return forSingleDay(date).setScale(2, BigDecimal.ROUND_HALF_EVEN).toPlainString();
    }

    public BigDecimal forSingleVisit(Visit visit) {
        return privateAmount(visit).add(publicAmount(visit)).subtract(visit.getExtraCosts());
    }

    public String forSingleVisitWithRound(Visit visit) {
        return forSingleVisit(visit).setScale(2, BigDecimal.ROUND_HALF_EVEN).toPlainString();
    }


    private BigDecimal privateAmount(Visit visit) {
        return visit.getAmount().multiply(visit.getOffice().getCommissionPrivate().divide(BigDecimal.valueOf(100)));
    }

    private BigDecimal publicAmount(Visit visit) {
        return (visit.getOffice().getCommissionPublic().divide(BigDecimal.valueOf(100))).multiply(BigDecimal.valueOf(visit.getPoint()).multiply(visit.getOffice().getNfzConversion()));
    }
}
