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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A class that represents a time interval as defined by ISO 8601
 *
 * @author Izaguirre, Ezequiel
 */
public class TimeInterval implements Serializable, Comparable<TimeInterval> {

  private final LocalDateTime startDate;
  private final LocalDateTime endDate;

  public TimeInterval(LocalDateTime start, LocalDateTime end) {
    this.startDate = start;
    this.endDate = end;
  }

  /**
   * Returns the starting point of this interval
   */
  public LocalDateTime getStartDate() {
    return startDate;
  }

  /**
   * Returns the (exclusive) ending point of this interval
   */
  public LocalDateTime getEndDate() {
    return endDate;
  }

  /**
   * Checks whether the given {@link LocalDateTime} falls within this time interval
   */
  public boolean includes(LocalDateTime date) {
    boolean startCheck = startDate.isBefore(date) || startDate.equals(date);
    boolean endCheck = endDate.isAfter(date);
    return startCheck && endCheck;
  }

  /**
   * Checks whether this interval happens before the specified {@link LocalDateTime}
   */
  public boolean isBefore(LocalDateTime date) {
    return endDate.isBefore(date) || endDate.equals(date);
  }

  /**
   * Checks whether this interval happens after the specified {@link LocalDateTime}
   */
  public boolean isAfter(LocalDateTime date) {
    return startDate.isAfter(date);
  }

  /**
   * Returns the time duration (or time period) of this interval
   */
  public Duration getDuration() {
    return Duration.between(startDate.toLocalTime(), endDate.toLocalTime());
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
