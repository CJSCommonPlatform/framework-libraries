package uk.gov.justice.services.common.jpa.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class LocalDatePersistenceConverterTest {

    private static final int DAY = 25;
    private static final Month MONTH = Month.DECEMBER;
    private static final int YEAR = 2016;

    private LocalDatePersistenceConverter localDatePersistenceConverter;

    @Before
    public void setup() {
        localDatePersistenceConverter = new LocalDatePersistenceConverter();
    }

    @Test
    public void shouldReturnValidConvertedDatabaseDate() {
        final LocalDate date = LocalDate.of(YEAR, MONTH, DAY);
        final Date convertedDate = localDatePersistenceConverter.convertToDatabaseColumn(date);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertedDate);
        assertThat(calendar.get(Calendar.DATE), equalTo(DAY));
        assertThat(calendar.get(Calendar.MONTH), equalTo(MONTH.getValue() - 1));
        assertThat(calendar.get(Calendar.YEAR), equalTo(YEAR));
    }

    @Test
    public void shouldReturnNullDatabaseValueWhenGivenNullDate() {
        assertNull(localDatePersistenceConverter.convertToDatabaseColumn(null));
    }

    @Test
    public void shouldReturnValidConvertedAttributeDate() {
        final Date date = Date.valueOf(String.format("%s-%s-%s", YEAR, MONTH.getValue() - 1, DAY));

        final LocalDate convertedDate = localDatePersistenceConverter.convertToEntityAttribute(date);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertThat(convertedDate.getDayOfMonth(), equalTo(calendar.get(Calendar.DATE)));
        assertThat(convertedDate.getMonth().getValue(), equalTo(calendar.get(Calendar.MONTH) + 1));
        assertThat(convertedDate.getYear(), equalTo(calendar.get(Calendar.YEAR)));
    }

    @Test
    public void shouldReturnEntityAttributeNullWhenGivenNullDate() {
        assertNull(localDatePersistenceConverter.convertToEntityAttribute(null));
    }
}