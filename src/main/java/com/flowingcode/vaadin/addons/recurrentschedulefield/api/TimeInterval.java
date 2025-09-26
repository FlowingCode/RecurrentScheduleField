/*-
 * #%L
 * RecurrentScheduleField Add-on
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
package com.flowingcode.vaadin.addons.recurrentschedulefield.api;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A class that represents a time interval (<em>a time period defined by start and end points</em>) according to
 * <a href="https://en.wikipedia.org/wiki/ISO_8601#Time_intervals">ISO 8601</a>.
 *
 * @author Flowing Code
 * @see DateTimeRange
 */
public class TimeInterval implements Serializable, Comparable<TimeInterval> {

  private final LocalDateTime startDate;
  private final LocalDateTime endDate;

  /**
   * Creates a new {@code TimeInterval} with the specified start and end date-times.
   *
   * @param start the inclusive start date-time of the interval
   * @param end the exclusive end date-time of the interval
   * @throws IllegalArgumentException if {@code start} is not before {@code end}
   */
  public TimeInterval(LocalDateTime start, LocalDateTime end) {
    if (!start.isBefore(end)) {
      throw new IllegalArgumentException("Start time must be before end time");
    }
    this.startDate = start;
    this.endDate = end;
  }

  /**
   * Returns the starting point of this interval.
   */
  public LocalDateTime getStartDate() {
    return startDate;
  }

  /**
   * Returns the (exclusive) ending point of this interval.
   */
  public LocalDateTime getEndDate() {
    return endDate;
  }

  /**
   * Checks whether the given {@link LocalDateTime} falls within this interval.
   *
   * @return {@code true} if the argument is within this interval, {@code false} otherwise
   */
  public boolean includes(LocalDateTime date) {
    boolean startCheck = startDate.isBefore(date) || startDate.equals(date);
    boolean endCheck = endDate.isAfter(date);
    return startCheck && endCheck;
  }

  /**
   * Checks whether this interval falls before the specified {@link LocalDateTime}.
   *
   * @return {@code true} if this interval is before, {@code false} otherwise
   */
  public boolean isBefore(LocalDateTime date) {
    return endDate.isBefore(date) || endDate.equals(date);
  }

  /**
   * Checks whether this interval falls after the specified {@link LocalDateTime}.
   *
   * @return {@code true} if this interval is after, {@code false} otherwise
   */
  public boolean isAfter(LocalDateTime date) {
    return startDate.isAfter(date);
  }

  /**
   * Gets the time duration (or time period) of this interval.
   * The duration is calculated as the difference between the start and (exclusive) end times.
   *
   * @return a {@link Duration} representing the length of an interval
   */
  public Duration getDuration() {
    return Duration.between(startDate, endDate);
  }

  @Override
  public int compareTo(TimeInterval o) {
    return this.startDate.compareTo(o.getStartDate());
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof TimeInterval that)) {
      return false;
    }
    return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDate, endDate);
  }
}
