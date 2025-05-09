/*-
 * #%L
 * DateTimeRangePicker Add-on
 * %%
 * Copyright (C) 2025 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.flowingcode.vaadin.addons.datetimerangepicker.api;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class to operate {@link TimeInterval} instances based on defined date and time constraints
 *
 * @author Izaguirre, Ezequiel
 * @see TimeInterval
 */
public class DateTimeRange implements Serializable {

  private static final DayOfWeek[] defaultWeekDays = DayOfWeek.values();
  private static final LocalTime defaultStartTime = LocalTime.MIN;
  private static final LocalTime defaultEndTime = LocalTime.MAX;

  private final LocalDate startDate;
  private final LocalDate endDate;
  private final DayOfWeek[] weekDays = {
      null, null, null, null, null, null, null
  };
  private LocalTime startTime = defaultStartTime;
  private LocalTime endTime = defaultEndTime;


  public DateTimeRange(LocalDate startDate, LocalDate endDate, Set<DayOfWeek> weekDays) {
    if (!startDate.isBefore(endDate)) {
      throw new IllegalArgumentException("startDate must be before endDate");
    }
    this.startDate = startDate;
    this.endDate = endDate;
    setWeekDays(weekDays);
  }

  public DateTimeRange(LocalDate startDate, LocalDate endDate) {
    this(startDate, endDate, Set.of(defaultWeekDays));
  }

  public DateTimeRange(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
      Set<DayOfWeek> weekDays) {
    this(startDate, endDate, weekDays);
    setDayDuration(startTime, endTime);
  }

  public DateTimeRange(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
    this(startDate, endDate, startTime, endTime, Set.of(defaultWeekDays));
  }

  /**
   * Sets time boundaries for the intervals
   *
   * @param startTime the starting point
   * @param endTime   the ending point (exclusive).
   */
  public void setDayDuration(LocalTime startTime, LocalTime endTime) {
    if (!startTime.isBefore(endTime)) {
      throw new IllegalArgumentException("startTime must be before endTime");
    }
    this.startTime = startTime;
    this.endTime = endTime;
  }

  /**
   * Defines on which days of the week an interval should exist
   *
   * @param weekDays a list of days
   */
  public void setWeekDays(Set<DayOfWeek> weekDays) {
    if(weekDays == null) {
      throw new IllegalArgumentException("weekDays can't be null");
    }
    DayOfWeek[] allWeekDays = DayOfWeek.values();

    for (int i = 0; i < allWeekDays.length; i++) {
      if (weekDays.contains(allWeekDays[i])) {
        this.weekDays[i] = allWeekDays[i];
      } else {
        this.weekDays[i] = null;
      }
    }
  }

  /**
   * Shorthand for <code>setWeekDays</code> with the entire week set
   */
  public void setAllWeekDays() {
    this.setWeekDays(Set.of(DayOfWeek.values()));
  }

  /**
   * Returns the days of the week when intervals exist
   */
  public Set<DayOfWeek> getWeekDays() {
    Set<DayOfWeek> days = new HashSet<>();
    for (DayOfWeek day : this.weekDays) {
      if (day != null) {
        days.add(day);
      }
    }
    return days;
  }

  /**
   * Gets the intervals that conform to current constraints
   */
  public List<TimeInterval> getIntervals() {
    return generateIntervals(this.startDate.atTime(this.startTime), this.endDate.atTime(this.endTime));
  }

  /**
   * Checks if the given {@link LocalDate} is between any interval
   */
  public boolean includes(LocalDate date) {
    int index = date.getDayOfWeek().getValue() - 1;
    return this.weekDays[index] != null && insideRange(date);
  }

  /**
   * Checks if the given {@link LocalDateTime} is between any interval
   */
  public boolean includes(LocalDateTime dateTime) {
    boolean contains = false;
    LocalDate date = dateTime.toLocalDate();
    if (this.includes(date)) {
      TimeInterval interval = new TimeInterval(
          date.atTime(startTime),
          date.atTime(endTime)
      );
      contains = interval.includes(dateTime);
    }

    return contains;
  }

  /**
   * Gets the next interval that ends after given {@link LocalDate}
   */
  public TimeInterval getNextInterval(LocalDate from) {
    LocalDateTime dateTime = LocalDateTime.of(from, this.startTime);
    return this.getNextInterval(dateTime);
  }

  /**
   * Gets the next interval that ends after current time
   */
  public TimeInterval getNextInterval() {
    return this.getNextInterval(LocalDateTime.now());
  }

  /**
   * Gets the next interval that ends after given {@link LocalDateTime}
   */
  public TimeInterval getNextInterval(LocalDateTime from) {
    LocalDate date = from.toLocalDate();
    LocalTime time = from.toLocalTime();
    TimeInterval interval = null;

    int index = date.getDayOfWeek().getValue() - 1;
    if (this.weekDays[index] == null) {
      DayOfWeek nextWeekDay = getNextDay(date.getDayOfWeek());
      date = date.plusDays(daysBetween(nextWeekDay, date.getDayOfWeek()));
    }
    if (insideRange(date)) {
      if (this.endTime.isAfter(time)) {
        interval = new TimeInterval(
            LocalDateTime.of(date, this.startTime),
            LocalDateTime.of(date, this.endTime)
        );
      } else {
        DayOfWeek nextWeekDay = getNextDay(date.getDayOfWeek());
        LocalDate nextDay = date.plusDays(daysBetween(nextWeekDay, date.getDayOfWeek()));
        if (insideRange(nextDay)) {
          interval = new TimeInterval(
              LocalDateTime.of(nextDay, this.startTime),
              LocalDateTime.of(nextDay, this.endTime)
          );
        }
      }
    }

    return interval;
  }

  /**
   * Gets the intervals that end after current time
   */
  public List<TimeInterval> getIntervalsLeft() {
    return this.getIntervalsLeft(LocalDateTime.now());
  }

  /**
   * Gets the intervals that end after {@link LocalDateTime}
   */
  public List<TimeInterval> getIntervalsLeft(LocalDateTime from) {
    List<TimeInterval> result = new ArrayList<>();
    TimeInterval nextInterval = getNextInterval(from);

    if (nextInterval != null) {
      result.addAll(generateIntervals(nextInterval.getStartDate(), this.endDate.atTime(LocalTime.MAX)));
    }

    return result;
  }

  /**
   * Gets the intervals that end after given {@link LocalDate}
   */
  public List<TimeInterval> getIntervalsLeft(LocalDate from) {
    return this.getIntervalsLeft(from.atTime(LocalTime.MIN));
  }

  /**
   * Gets the intervals that ended before current time
   */
  public List<TimeInterval> getPastIntervals() {
    return this.getPastIntervals(LocalDateTime.now());
  }

  /**
   * Gets the intervals that ended before given {@link LocalDate}
   */
  public List<TimeInterval> getPastIntervals(LocalDate from) {
    return this.getPastIntervals(from.atTime(LocalTime.MIN));
  }

  /**
   * Gets the intervals that ended before given {@link LocalDateTime}
   */
  public List<TimeInterval> getPastIntervals(LocalDateTime from) {
    TimeInterval nextInterval = getNextInterval(from);

    from = nextInterval == null ? this.getEndDate().atTime(LocalTime.MAX) : nextInterval.getStartDate();

    return new ArrayList<>(generateIntervals(this.startDate.atTime(LocalTime.MIN), from));
  }

  /**
   * Gets the time interval duration (or time period)
   */
  public Duration getDayDuration() {
    return Duration.between(startTime, endTime);
  }

  /**
   * Gets the period between the start and (exclusive) end dates
   */
  public Period getDatesPeriod() {
    return Period.between(startDate, endDate);
  }

  /**
   * Gets the start date
   */
  public LocalDate getStartDate() {
    return this.startDate;
  }

  /**
   * Gets the end date (exclusive)
   */
  public LocalDate getEndDate() {
    return this.endDate;
  }

  /**
   * Gets the {@link LocalTime} when intervals start
   */
  public LocalTime getStartTime() {
    return this.startTime;
  }

  /**
   * Gets the {@link LocalTime} when intervals end (exclusive)
   */
  public LocalTime getEndTime() {
    return this.endTime;
  }

  // Utils
  private DayOfWeek getNextDay(DayOfWeek previous) {
    int index = previous.getValue() - 1;
    DayOfWeek[] allWeekDays = DayOfWeek.values();
    DayOfWeek next;
    do {
      index = (index + 1) % allWeekDays.length;
    }
    while ((next = this.weekDays[index]) == null);
    return next;
  }

  private int daysBetween(DayOfWeek to, DayOfWeek from) {
    int offset = to.getValue() - from.getValue();
    if (offset <= 0) {
      offset = 7 + offset;
    }
    return offset;
  }

  private List<TimeInterval> generateIntervals(LocalDateTime from, LocalDateTime to) {
    LocalDate startDate = from.toLocalDate();
    LocalTime startTime = from.toLocalTime();
    LocalDate endDate = to.toLocalDate();
    long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    long i = 0;
    List<TimeInterval> entities = new ArrayList<>();
    DayOfWeek firstDay = startDate.getDayOfWeek();
    if (this.weekDays[firstDay.getValue() - 1] == null || !this.endTime.isAfter(startTime)) {
      DayOfWeek nextDay = getNextDay(startDate.getDayOfWeek());
      i = daysBetween(nextDay, firstDay);
    }

    while (i < days) {
      LocalDate current = startDate.plusDays(i);
      LocalDateTime start = LocalDateTime.of(current, this.startTime);
      LocalDateTime end = LocalDateTime.of(current, this.endTime);
      TimeInterval timeInterval = new TimeInterval(start, end);
      entities.add(timeInterval);

      DayOfWeek lastDay = current.getDayOfWeek();
      DayOfWeek nextDay = getNextDay(current.getDayOfWeek());
      i += daysBetween(nextDay, lastDay);
    }

    return entities;
  }

  private boolean insideRange(LocalDate date) {
    return (this.startDate.isEqual(date) || this.startDate.isBefore(date))
        &&
        (this.endDate.isAfter(date));
  }
}
