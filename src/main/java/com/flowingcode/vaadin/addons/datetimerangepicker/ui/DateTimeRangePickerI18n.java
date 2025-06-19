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
package com.flowingcode.vaadin.addons.datetimerangepicker.ui;

import com.vaadin.flow.function.SerializableRunnable;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides internationalization support for {@link DateTimeRangePicker}.
 *
 * <h5>Features:</h5>
 * <ul>
 * <li>Set and retrieve titles for date, day, and time pickers.</li>
 * <li>Configure placeholders for date and time pickers.</li>
 * <li>Customize day initials and filter chip texts for days and times.</li>
 * </ul>
 *
 * @author Flowing Code
 * @see DateTimeRangePicker
 */
public class DateTimeRangePickerI18n implements Serializable {

  private DateTimeRangePicker component;
  private final Map<String, SerializableRunnable> actions = new HashMap<>();

  void attachComponent(DateTimeRangePicker component) {
    this.component = component;
  }

  Collection<SerializableRunnable> getPendingActions() {
    return actions.values();
  }

  private void addAction(SerializableRunnable action, String function) {
    actions.put(function, action);
    if (component != null) {
      action.run();
    }
  }

  /**
   * Sets the date pickers' title.
   *
   * @param text   title for the pickers
   */
  public DateTimeRangePickerI18n setDatesTitle(String text) {
    addAction(() -> component.getDatesTitle().setText(text), "setDatesTitle");
    return this;
  }

  /**
   * Gets current date pickers' title.
   *
   * @return date pickers' title or {@code null} if this object is not attached to a {@code DateTimeRangePicker} instance
   */
  public String getDatesTitle() {
    return component != null ? component.getDatesTitle().getText() : null;
  }

  /**
   * Sets the days picker's title.
   *
   * @param text   title for the days picker
   */
  public DateTimeRangePickerI18n setDaysTitle(String text) {
    addAction(() -> component.getDaysTitle().setText(text), "setDaysTitle");
    return this;
  }

  /**
   * Gets current days picker's title.
   *
   * @return days picker's title or {@code null} if this object is not attached to a {@code DateTimeRangePicker} instance
   */
  public String getDaysTitle() {
    return component != null ? component.getDaysTitle().getText() : null;
  }

  /**
   * Sets the time pickers' title.
   *
   * @param text   title for the pickers
   */
  public DateTimeRangePickerI18n setTimesTitle(String text) {
    addAction(() -> component.getTimesTitle().setText(text), "setTimesTitle");
    return this;
  }

  /**
   * Gets current time pickers' title.
   *
   * @return time pickers' title or {@code null} if this object is not attached to a {@code DateTimeRangePicker} instance
   */
  public String getTimesTitle() {
    return component != null ? component.getTimesTitle().getText() : null;
  }

  /**
   * Sets the time pickers' placeholder.
   *
   * @param startTime   placeholder for the start-time picker
   * @param endTime     placeholder for the end-time picker
   */
  public DateTimeRangePickerI18n setTimesPlaceholder(String startTime, String endTime) {
    addAction(() -> {
      component.getStartTimePicker().setPlaceholder(startTime);
      component.getEndTimePicker().setPlaceholder(endTime);
    }, "setTimesPlaceholder");
    return this;
  }

  /**
   * Gets current time pickers' placeholder.
   *
   * @return
   * a list where the first element corresponds to the start-time picker's placeholder and the second to the end-time picker's placeholder
   * <br><br>{@code null} if this object is not attached to a {@code DateTimeRangePicker} instance
   */
  public List<String> getTimesPlaceholder() {
    return component != null ? List.of(
        component.getStartTimePicker().getPlaceholder(),
        component.getEndTimePicker().getPlaceholder()
    ) : null;
  }

  /**
   * Sets the date pickers' placeholder.
   *
   * @param startDate   placeholder for the start-date picker
   * @param endDate     placeholder for the end-date picker
   */
  public DateTimeRangePickerI18n setDatesPlaceholder(String startDate, String endDate) {
    addAction(() -> {
      component.getStartDatePicker().setPlaceholder(startDate);
      component.getEndDatePicker().setPlaceholder(endDate);
    }, "setDatesPlaceholder");
    return this;
  }

  /**
   * Gets current date pickers' placeholder.
   *
   * @return
   * a list where the first element corresponds to the start-date picker's placeholder and the second to the end-date picker's placeholder
   * <br><br>{@code null} if this object is not attached to a {@code DateTimeRangePicker} instance
   */
  public List<String> getDatesPlaceholder() {
    return component != null ? List.of(
        component.getStartDatePicker().getPlaceholder(),
        component.getEndDatePicker().getPlaceholder()
    ) : null;
  }

  /**
   * Sets the week days picker's initials.
   *
   * @param initials   a list of initials for the 7 days of the week
   *                   <br>The order of each depends on the order set on the picker
   * @see DateTimeRangePicker#setFirstWeekDay(DayOfWeek)
   */
  public DateTimeRangePickerI18n setDayInitials(List<String> initials) {
    if (initials == null || initials.size() != 7) {
      throw new IllegalArgumentException("Exactly 7 day initials are required");
    }
    addAction(() -> {
      component.setDaysInitials(initials);
      component.getWeekDaySelector().setWeekDaysShort(initials);
    }, "setDayInitials");
    return this;
  }

  /**
   * Gets current week days picker's initials.
   *
   * @return
   * a list of initials for the 7 days of the week. The order of each depends on the order set on the picker
   * <br><br>{@code null} if this object is not attached to a {@code DateTimeRangePicker} instance
   * @see DateTimeRangePicker#setFirstWeekDay(DayOfWeek)
   */
  public List<String> getDayInitials() {
    return component != null ? component.getDaysInitials() : null;
  }

  /**
   * Sets the time filter chips' text.
   *
   * @param morning       text for the morning-only chip
   * @param afternoon     text for the afternoon-only chip
   * @param all           text for the all-day chip
   */
  public DateTimeRangePickerI18n setTimeChipsText(String morning, String afternoon, String all) {
    addAction(() -> {
      component.getMorningChip().setText(morning);
      component.getAfterNoonChip().setText(afternoon);
      component.getAllTimeChip().setText(all);
    }, "setTimeChipsText");
    return this;
  }

  /**
   * Gets current time filter chips' text.
   *
   * @return
   * a list where the first element corresponds to the morning-only chip's text,
   * the second to the afternoon-only chip's text and the third to the all-day chip's text
   *
   * <br><br>{@code null} if this object is not attached to a {@code DateTimeRangePicker} instance
   */
  public List<String> getTimeChipsText() {
    return component != null ? List.of(
        component.getMorningChip().getText(),
        component.getAfterNoonChip().getText(),
        component.getAllTimeChip().getText()
    ) : null;
  }

  /**
   * Sets the days filter chips' text.
   *
   * @param weekdays      text for the monday-to-friday chip
   * @param weekend       text for the weekends-only chip
   * @param all           text for the all days chip
   */
  public DateTimeRangePickerI18n setDaysChipsText(String weekend, String weekdays, String all) {
    addAction(() -> {
      component.getWeekdaysChip().setText(weekdays);
      component.getWeekendChip().setText(weekend);
      component.getAllDaysChip().setText(all);
    }, "setDaysChipsText");
    return this;
  }

  /**
   * Gets current days filter chips' text.
   *
   * @return
   * a list where the first element corresponds to the monday-to-friday chip's text,
   * the second to the weekends-only chip's text and the third to the all-days chip's text
   *
   * <br><br>{@code null} if this object is not attached to a {@code DateTimeRangePicker} instance
   */
  public List<String> getDaysChipsText() {
    return component != null ? List.of(
        component.getWeekdaysChip().getText(),
        component.getWeekendChip().getText(),
        component.getAllDaysChip().getText()
    ) : null;
  }
}
