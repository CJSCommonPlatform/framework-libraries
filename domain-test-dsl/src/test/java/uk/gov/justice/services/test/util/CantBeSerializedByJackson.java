package uk.gov.justice.services.test.util;

import uk.gov.justice.domain.annotation.Event;

/*
 Jackson can't serialise this class as its attributes are all private, results in an error which
 we need for testing
 */
public class CantBeSerializedByJackson {

    private String attrOne;
    private String attrTwo;
    private String attrThree;

    public CantBeSerializedByJackson(String attrOne, String attrTwo, String attrThree) {
        this.attrOne = attrOne;
        this.attrTwo = attrTwo;
        this.attrThree = attrThree;
    }


}
