package uk.gov.justice.services.test.domain.event;

import uk.gov.justice.domain.annotation.Event;

import java.math.BigDecimal;
import java.util.UUID;

@Event("something-with-bigdecimal-happened")
public class SomethingWithBigDecimalHappened {

    private final UUID id;
    private BigDecimal bd;

    public SomethingWithBigDecimalHappened(final UUID id, BigDecimal bd) {
        this.id = id;
        this.bd = bd;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBd() {
        return bd;
    }

}
