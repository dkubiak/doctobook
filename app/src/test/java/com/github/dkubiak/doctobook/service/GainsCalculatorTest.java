package com.github.dkubiak.doctobook.service;

import com.github.dkubiak.doctobook.model.Office;
import com.github.dkubiak.doctobook.model.Visit;

import org.junit.Test;


import static org.assertj.core.api.Assertions.*;

public class GainsCalculatorTest {

    @Test
    public void shouldCalculateGainForSingleVisitOnlyPrivateWithExtraCost() {

        //given
        Visit visit = new Visit.Builder()
                .setAmount("123.15")
                .setPoint(0)
                .setExtraCosts("3")
                .setOffice(new Office.Builder()
                        .setCommissionPrivate("38")
                        .setCommissionPublic("50")
                        .setNfzConversion("1.3").createOffice())
                .createVisit();
        //when
        GainsCalculator gainsCalculator = new GainsCalculator();
        String singleVisit = gainsCalculator.forSingleVisitWithRound(visit);
        //then

        assertThat(singleVisit).isEqualTo("43.80");
    }

}