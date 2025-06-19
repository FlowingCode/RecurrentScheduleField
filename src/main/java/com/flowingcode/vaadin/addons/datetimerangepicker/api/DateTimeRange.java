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

import static java.time.temporal.ChronoUnit.DAYS;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * Represents a range of dates, times and days, generating {@link TimeInterval} instances
 * based on specified constraints.
 *
 * <h5>Characteristics:</h5>
 * <ul>
 * <li>Date range: Intervals span from (inclusive) start to (exclusive) end dates.</li>
 * <li>Days: Only includes specified days of the week.</li>
 * <li>Time range: Each interval spans from (inclusive) start to (exclusive) end times.</li>
 * </ul>
 *
 * <h5>Features:</h5>
 * <ul>
 * <li>Retrieve intervals within the range.</li>
 * <li>Check if a date or date-time falls within any interval.</li>
 * <li>Get past or future intervals relative to a given date.</li>
 * <li>Calculate interval duration or the period between start and end dates.</li>
 * </ul>
 *
 * @author Flowing Code
 * @see TimeInterval
 */
public class DateTimeRange implements Serializable {

  private static final DayOfWeek[] defaultWeekDays = DayOfWeek.values();
  private static final LocalTime defaultStartTime = LocalTime.MIN;
  private static final LocalTime defaultEndTime = LocalTime.MAX;

  private final LocalDate startDate;
  private final LocalDate endDate;
  private final TreeSet<DayOfWeek> weekDays = new TreeSet<>();
  private LocalTime startTime = defaultStartTime;
  private LocalTime endTime = defaultEndTime;

  /**
   * Creates a new {@code DateTimeRange} with the specified start and end dates, and included days of the week.
   *
   * @param startDate the inclusive start date of the range
   * @param endDate the exclusive end date of the range
   * @param weekDays the set of included {@link DayOfWeek} values
   * @throws IllegalArgumentException if {@code startDate} is not before {@code endDate}, or if {@code weekDays} is null or empty
   */
  public DateTimeRange(LocalDate startDate, LocalDate endDate, Set<DayOfWeek> weekDays) {
    if (!startDate.isBefore(endDate)) {
      throw new IllegalArgumentException("startDate must be before endDate");
    }
    this.startDate = startDate;
    this.endDate = endDate;
    setWeekDays(weekDays);
  }

  /**
   * Creates a new {@code DateTimeRange} with the specified start and end dates, including all days of the week.
   *
   * @param startDate the inclusive start date of the range
   * @param endDate the exclusive end date of the range
   * @throws IllegalArgumentException if {@code startDate} is not before {@code endDate}
   */
  public DateTimeRange(LocalDate startDate, LocalDate endDate) {
    this(startDate, endDate, Set.of(defaultWeekDays));
  }

  /**
   * Creates a new {@code DateTimeRange} with the specified dates, time boundaries, and included days of the week.
   *
   * @param startDate the inclusive start date of the range
   * @param endDate the exclusive end date of the range
   * @param startTime the inclusive start time of each interval
   * @param endTime the exclusive end time of each interval
   * @param weekDays the set of included {@link DayOfWeek} values
   * @throws IllegalArgumentException if {@code startDate} is not before {@code endDate}, if {@code startTime} is not before {@code endTime}, or if {@code weekDays} is null or empty
   */
  public DateTimeRange(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
      Set<DayOfWeek> weekDays) {
    this(startDate, endDate, weekDays);
    setDayDuration(startTime, endTime);
  }

  /**
   * Creates a new {@code DateTimeRange} with the specified dates and time boundaries, including all days of the week.
   *
   * @param startDate the inclusive start date of the range
   * @param endDate the exclusive end date of the range
   * @param startTime the inclusive start time of each interval
   * @param endTime the exclusive end time of each interval
   * @throws IllegalArgumentException if {@code startDate} is not before {@code endDate}, or if {@code startTime} is not before {@code endTime}
   */
  public DateTimeRange(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
    this(startDate, endDate, startTime, endTime, Set.of(defaultWeekDays));
  }

  /**
   * Sets time boundaries for the intervals.
   *
   * @param startTime the starting point
   * @param endTime   the ending point (exclusive)
   */
  public void setDayDuration(LocalTime startTime, LocalTime endTime) {
    if (!startTime.isBefore(endTime)) {
      throw new IllegalArgumentException("startTime must be before endTime");
    }
    this.startTime = startTime;
    this.endTime = endTime;
  }

  /**
   * Defines on which days of the week intervals are defined.
   *
   * @param weekDays a list of days
   */
  public void setWeekDays(Set<DayOfWeek> weekDays) {
    if(weekDays == null || weekDays.isEmpty()) {
      throw new IllegalArgumentException("weekDays can't be null or empty");
    }
    this.weekDays.clear();
    this.weekDays.addAll(weekDays);
  }

  /**
   * Sets the interval creation to include all days of the week.
   * This is equivalent to calling {@link #setWeekDays(Set)} with all days.
   */
  public void setAllWeekDays() {
    this.setWeekDays(Set.of(DayOfWeek.values()));
  }

  /**
   * Returns the days of the week on which intervals are defined.
   *
   * @return an immutable set of {@link DayOfWeek}
   */
  public Set<DayOfWeek> getWeekDays() {
    return Set.copyOf(weekDays);
  }

/**
 * Gets the intervals that conform to the current date and time constraints.
 * Each interval represents a time range within the defined start and end dates,
 * and only includes the specified days of the week.
 *
 * @return a list of {@link TimeInterval} objects, sorted by their time range
 */
  public List<TimeInterval> getIntervals() {
    return generateIntervals(this.startDate.atTime(this.startTime), endDate);
  }

/**
 * Checks if the given {@link LocalDate} falls within any interval.
 *
 * @return {@code true} if the argument is within an interval, {@code false} otherwise
 */
  public boolean includes(LocalDate date) {
    return weekDays.contains(date.getDayOfWeek()) && insideRange(date);
  }

  /**
   * Checks if the given {@link LocalDateTime} falls within any interval.
   *
   * @return {@code true} if the argument is within an interval, {@code false} otherwise
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
   * Gets the next interval that ends after the given {@link LocalDate}.
   *
   * @return the next {@link TimeInterval} after the given date, or {@code null} if no such interval exists
   */
  public TimeInterval getNextInterval(LocalDate from) {
    LocalDateTime dateTime = LocalDateTime.of(from, this.startTime);
    return this.getNextInterval(dateTime);
  }

  /**
   * Gets the next interval that ends after the current date and time.
   *
   * @return the next {@link TimeInterval} after the current date and time, or {@code null} if no such interval exists
   */
  public TimeInterval getNextInterval() {
    return this.getNextInterval(LocalDateTime.now());
  }

  /**
   * Gets the next interval that ends after the given {@link LocalDateTime}.
   *
   * @return the next {@link TimeInterval} after the given date, or {@code null} if no such interval exists
   */
  public TimeInterval getNextInterval(LocalDateTime from) {
    LocalDate date = from.toLocalDate();
    TimeInterval interval = null;

    long offset = getStartOffset(from);
    date = date.plusDays(offset);

    if (insideRange(date)) {
      interval = new TimeInterval(
          LocalDateTime.of(date, this.startTime),
          LocalDateTime.of(date, this.endTime)
      );
    }

    return interval;
  }

  /**
   * Gets the intervals that end after the current date and time.
   *
   * @return a list of {@link TimeInterval} objects representing the remaining intervals
   */
  public List<TimeInterval> getIntervalsLeft() {
    return this.getIntervalsLeft(LocalDateTime.now());
  }

  /**
   * Gets the intervals that end after given {@link LocalDateTime}.
   *
   * @return a list of {@link TimeInterval} objects representing the remaining intervals
   */
  public List<TimeInterval> getIntervalsLeft(LocalDateTime from) {
    return generateIntervals(from, endDate);
  }

  /**
   * Gets the intervals that end after given {@link LocalDate}.
   *
   * @return a list of {@link TimeInterval} objects representing the remaining intervals
   */
  public List<TimeInterval> getIntervalsLeft(LocalDate from) {
    return this.getIntervalsLeft(from.atTime(LocalTime.MIN));
  }

  /**
   * Gets the intervals that ended before the current date and time.
   *
   * @return a list of {@link TimeInterval} objects representing the past intervals
   */
  public List<TimeInterval> getPastIntervals() {
    return this.getPastIntervals(LocalDateTime.now());
  }

  /**
   * Gets the intervals that ended before given {@link LocalDate}.
   *
   * @return a list of {@link TimeInterval} objects representing the past intervals
   */
  public List<TimeInterval> getPastIntervals(LocalDate from) {
    return this.getPastIntervals(from.atTime(LocalTime.MIN));
  }

  /**
   * Gets the intervals that ended before given {@link LocalDateTime}.
   *
   * @return a list of {@link TimeInterval} objects representing the past intervals
   */
  public List<TimeInterval> getPastIntervals(LocalDateTime from) {
    LocalTime endTime = from.toLocalTime();
    LocalDate endDate = from.toLocalDate();

    return generateIntervals(startDate.atTime(startTime), !endTime.isBefore(this.endTime) ? endDate.plusDays(1) : endDate);
  }

  /**
   * Gets the time duration (or time period) of an interval.
   * The duration is calculated as the difference between the start and (exclusive) end times.
   *
   * @return a {@link Duration} representing the length of an interval
   */
  public Duration getDayDuration() {
    return Duration.between(startTime, endTime);
  }

  /**
   * Gets the period between the start and (exclusive) end dates.
   */
  public Period getDatesPeriod() {
    return Period.between(startDate, endDate);
  }

  /**
   * Gets the start date.
   */
  public LocalDate getStartDate() {
    return this.startDate;
  }

  /**
   * Gets the end date (exclusive).
   */
  public LocalDate getEndDate() {
    return this.endDate;
  }

  /**
   * Gets the {@link LocalTime} when intervals start.
   */
  public LocalTime getStartTime() {
    return this.startTime;
  }

  /**
   * Gets the {@link LocalTime} when intervals end (exclusive).
   */
  public LocalTime getEndTime() {
    return this.endTime;
  }

  // Utils
  private DayOfWeek getNextDay(DayOfWeek previous) {
    DayOfWeek current = previous.plus(1);
    while(!weekDays.contains(current)) {
      current = current.plus(1);
    }
    return current;
  }

  // If <from> is after <to>, add a week (7) offset
  private int daysBetween(DayOfWeek from, DayOfWeek to) {
    int offset = to.getValue() - from.getValue();
    if (offset <= 0) {
      offset = 7 + offset;
    }
    return offset;
  }

  // Check if date is between start and end (exclusive)
  private boolean insideRange(LocalDate date) {
    return !this.startDate.isAfter(date) && this.endDate.isAfter(date);
  }

  // <to> is exclusive
  private List<TimeInterval> generateIntervals(LocalDateTime from, LocalDate to) {
    List<TimeInterval> entities = new ArrayList<>();
    LocalDate startDate = from.toLocalDate();

    long totalDays = getTotalDays(startDate, to);
    long startOffset = getStartOffset(from);

    while (startOffset < totalDays) {
      LocalDate current = startDate.plusDays(startOffset);
      LocalDateTime start = LocalDateTime.of(current, this.startTime);
      LocalDateTime end = LocalDateTime.of(current, this.endTime);
      TimeInterval timeInterval = new TimeInterval(start, end);
      entities.add(timeInterval);

      DayOfWeek lastDay = current.getDayOfWeek();
      DayOfWeek nextDay = getNextDay(lastDay);
      startOffset += daysBetween(lastDay, nextDay);
    }

    return entities;
  }

  // Get days constrained within dates range
  private long getTotalDays(LocalDate startDate, LocalDate endDate) {
    LocalDate start = startDate.isBefore(this.startDate) ? this.startDate : startDate;
    LocalDate end = endDate.isAfter(this.endDate) ? this.endDate : endDate;
    return DAYS.between(start, end);
  }

  // From startDate, get days till next interval
  private long getStartOffset(LocalDateTime startDate) {
    DayOfWeek firstDay = startDate.getDayOfWeek();
    // Check if it should start the next day
    if (!weekDays.contains(firstDay) || !this.endTime.isAfter(startDate.toLocalTime())) {
      DayOfWeek nextDay = getNextDay(startDate.getDayOfWeek());
      return daysBetween(firstDay, nextDay);
    }
    else return 0;
  }
}
