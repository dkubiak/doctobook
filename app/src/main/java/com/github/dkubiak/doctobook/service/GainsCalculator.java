package com.github.dkubiak.doctobook.service;


import android.support.annotation.NonNull;

import com.github.dkubiak.doctobook.DatabaseHelper;
import com.github.dkubiak.doctobook.model.Office;
import com.github.dkubiak.doctobook.model.Visit;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;

public class GainsCalculator {

    private DatabaseHelper db;

    public GainsCalculator() {
    }

    public GainsCalculator(DatabaseHelper db) {
        this.db = db;
    }

    public BigDecimal forMeByDay(Office office, Date date) {
        List<Visit> visits = db.getVisitByOfficeAndDay(office, date);
        BigDecimal sum = BigDecimal.ZERO;
        for (Visit visit : visits) {
            sum = sum.add(forMeSingleVisit(visit));
        }
        return sum;
    }

    public BigDecimal forMeByMonth(Office office, Date date) {
        List<Visit> visits = db.getVisitByOfficeAndMonth(office, date);
        BigDecimal sum = BigDecimal.ZERO;
        for (Visit visit : visits) {
            sum = sum.add(forMeSingleVisit(visit));
        }
        return sum;
    }

    public BigDecimal forMeByMonth(Date date) {
        List<Visit> visits = db.getVisitByMonth(date);
        BigDecimal sum = BigDecimal.ZERO;
        for (Visit visit : visits) {
            sum = sum.add(forMeSingleVisit(visit));
        }
        return sum;
    }

    public Integer pointsForOfficeByMonth(Office office, Date date) {
        List<Visit> visits = db.getVisitByOfficeAndMonth(office, date);
        int sum = 0;
        for (Visit visit : visits) {
            sum += visit.getPoint();
        }
        return sum;
    }


    public String forMeByMonthWithRound(Office office, Date date) {
        return amountFormatter(forMeByMonth(office, date).setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    public String forMeByMonthWithRound(Date date) {
        return amountFormatter(forMeByMonth(date).setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    public String forMeByDayWithRound(Office office, Date date) {
        return amountFormatter(forMeByDay(office, date).setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @NonNull
    private String amountFormatter(Object object) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat decimalFormat = new DecimalFormat("###,###.##", symbols);

        return decimalFormat.format(object);
    }

    public BigDecimal forMeSingleVisit(Visit visit) {
        return privateAmount(visit).add(publicAmount(visit)).subtract(visit.getExtraCosts());
    }

    public String forMeSingleVisitWithRound(Visit visit) {
        return amountFormatter(forMeSingleVisit(visit).setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }


    private BigDecimal privateAmount(Visit visit) {
        return visit.getAmount().multiply(visit.getOffice().getCommissionPrivate().divide(BigDecimal.valueOf(100)));
    }

    private BigDecimal publicAmount(Visit visit) {
        return (visit.getOffice().getCommissionPublic().divide(BigDecimal.valueOf(100))).multiply(BigDecimal.valueOf(visit.getPoint()).multiply(visit.getOffice().getNfzConversion()));
    }
}
