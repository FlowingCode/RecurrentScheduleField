package com.flowingcode.vaadin.addons.datetimerangepicker.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.flowingcode.vaadin.addons.datetimerangepicker.api.DateTimeRange;
import com.flowingcode.vaadin.addons.datetimerangepicker.api.TimeInterval;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import org.junit.Test;

public class DateTimeRangeTest {

  @Test
  public void testRanges() {
    // Monday 7 to Monday 21 (exclusive) - 12:00 to 20:30 (exclusive)
    LocalDate startDate = LocalDate.of(2025, 4, 7);
    LocalDate endDate = LocalDate.of(2025, 4, 21);
    LocalTime startTime = LocalTime.NOON;
    LocalTime endTime = LocalTime.of(20, 30);

    DateTimeRange dtr = new DateTimeRange(
        startDate,
        endDate,
        startTime,
        endTime,
        Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
    );

    assertThat(dtr.includes(startDate), equalTo(true));
    assertThat(dtr.includes(startDate.plusDays(4)), equalTo(true));
    assertThat(dtr.includes(startDate.plusDays(7)), equalTo(true));
    assertThat(dtr.includes(startDate.plusDays(11)), equalTo(true));
    assertThat(dtr.includes(startDate.plusDays(14)), equalTo(false));
    assertThat(dtr.includes(startDate.plusDays(18)), equalTo(false));
    assertThat(dtr.includes(startDate.plusDays(3)), equalTo(false));
    assertThat(dtr.includes(endDate), equalTo(false));

    assertThat(dtr.includes(startDate.atTime(startTime)), equalTo(true));
    assertThat(dtr.includes(startDate.atTime(LocalTime.of(13, 45))), equalTo(true));
    assertThat(dtr.includes(startDate.atTime(LocalTime.MIN)), equalTo(false));
    assertThat(dtr.includes(startDate.atTime(LocalTime.MAX)), equalTo(false));
    assertThat(dtr.includes(startDate.atTime(LocalTime.of(20, 30))), equalTo(false));

    assertThat(dtr.getWeekDays().contains(DayOfWeek.MONDAY), equalTo(true));
    assertThat(dtr.getWeekDays().contains(DayOfWeek.FRIDAY), equalTo(true));
    assertThat(dtr.getWeekDays().contains(DayOfWeek.THURSDAY), equalTo(false));
    assertThat(dtr.getWeekDays().contains(DayOfWeek.SATURDAY), equalTo(false));
  }

  @Test
  public void testIntervals() {
    // Monday 7 to Monday 21 (exclusive) - 12:00 to 20:30 (exclusive)
    LocalDate startDate = LocalDate.of(2025, 4, 7);
    LocalDate endDate = LocalDate.of(2025, 4, 21);
    LocalTime startTime = LocalTime.NOON;
    LocalTime endTime = LocalTime.of(20, 30);

    DateTimeRange dtr = new DateTimeRange(
        startDate,
        endDate,
        startTime,
        endTime,
        Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
    );

    assertThat(dtr.getIntervals().size(), equalTo(4));
    assertThat(dtr.getNextInterval(startDate), equalTo(new TimeInterval(
            startDate.atTime(startTime),
            startDate.atTime(endTime))
        )
    );
    assertThat(dtr.getNextInterval(startDate.atTime(LocalTime.MIN)), equalTo(new TimeInterval(
            startDate.atTime(startTime),
            startDate.atTime(endTime))
        )
    );
    assertThat(dtr.getNextInterval(startDate.atTime(LocalTime.NOON)), equalTo(new TimeInterval(
            startDate.atTime(startTime),
            startDate.atTime(endTime))
        )
    );
    assertThat(dtr.getNextInterval(startDate.atTime(endTime)), equalTo(new TimeInterval(
            startDate.plusDays(4).atTime(startTime),
            startDate.plusDays(4).atTime(endTime))
        )
    );
    assertThat(dtr.getNextInterval(startDate.atTime(LocalTime.MAX)), equalTo(new TimeInterval(
            startDate.plusDays(4).atTime(startTime),
            startDate.plusDays(4).atTime(endTime))
        )
    );
    assertThat(dtr.getNextInterval(endDate), equalTo(null));
    assertThat(dtr.getNextInterval(startDate.plusDays(19)), equalTo(null));

    assertThat(dtr.getIntervalsLeft(startDate).size(), equalTo(4));
    assertThat(dtr.getIntervalsLeft(startDate.atTime(endTime)).size(), equalTo(3));
    assertThat(dtr.getIntervalsLeft(startDate.plusDays(5)).size(), equalTo(2));
    assertThat(dtr.getIntervalsLeft(startDate.plusDays(19)).size(), equalTo(0));

    assertThat(dtr.getPastIntervals(startDate).size(), equalTo(0));
    assertThat(dtr.getPastIntervals(startDate.atTime(endTime)).size(), equalTo(1));
    assertThat(dtr.getPastIntervals(startDate.plusDays(5)).size(), equalTo(2));
    assertThat(dtr.getPastIntervals(startDate.plusDays(19)).size(), equalTo(4));
  }

}
