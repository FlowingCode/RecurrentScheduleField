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

import com.flowingcode.vaadin.addons.datetimerangepicker.api.DateTimeRange;
import com.flowingcode.vaadin.addons.datetimerangepicker.ui.ChipGroup.Chip;
import com.flowingcode.vaadin.addons.dayofweekselector.DayOfWeekSelector;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.HasValidator;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignSelf;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding.Bottom;
import com.vaadin.flow.theme.lumo.LumoUtility.Position;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A component to create <a href="https://en.wikipedia.org/wiki/ISO_8601#Time_intervals">Time Intervals</a> based on date and time constraints
 *
 * @author Izaguirre, Ezequiel
 */
public class DateTimeRangePicker
    extends CustomField<DateTimeRange> implements HasValidator<DateTimeRange> {

  static final String defaultErrorMessage = "Invalid or incomplete fields remaining";

  // Mandatory attributes for validation
  DatePicker startDate;
  DatePicker endDate;
  TimePicker startTime;
  TimePicker endTime;
  DayOfWeekSelector weekDays;

  // UI-only validation attributes (should change after validation check)
  SpanLine daysDivider;
  SpanLine timeDivider;
  Circle dateCircle;
  Circle timeCircle;
  Circle daysCircle;

  // Others
  H5 datesTitle;
  H5 daysTitle;
  H5 timesTitle;
  Component dateSelector;
  Component daysSelector;
  Component timeSelector;
  Div verticalLine;
  ChipGroup daysChipGroup;
  Chip weekdaysChip;
  Chip weekendChip;
  Chip allDaysChip;
  ChipGroup timeChipGroup;
  Chip morningChip;
  Chip afterNoonChip;
  Chip allTimeChip;
  List<String> daysInitials;

  private void setUI() {
    addClassNames(
        Width.AUTO,
        Display.INLINE,
        Margin.NONE,
        Padding.SMALL
    );

    HorizontalLayout rootLayout = new HorizontalLayout();
    rootLayout.addClassNames(
        Padding.SMALL,
        Width.FULL,
        Height.FULL,
        AlignItems.STRETCH,
        Gap.MEDIUM
    );

    VerticalLayout mainLayout = new VerticalLayout();
    dateSelector = getDateSelectors();
    daysSelector = getDaysSelector();
    timeSelector = getTimeSelectors();

    verticalLine = new Div();
    verticalLine.getStyle().setBackgroundColor("var(--lumo-contrast-10pct)");
    verticalLine.setMinWidth("1px");
    verticalLine.setMaxWidth("1px");
    verticalLine.setMinHeight("100%");
    verticalLine.setMaxHeight("100%");

    mainLayout.addClassNames(
        Gap.MEDIUM,
        Display.INLINE_FLEX,
        AlignItems.STRETCH,
        Padding.NONE,
        Flex.GROW
    );

    mainLayout.add(dateSelector, daysSelector, timeSelector);
    rootLayout.add(verticalLine, mainLayout);
    add(rootLayout);
  }

  void refreshUI() {
    if (this.startDate.getValue() != null && this.endDate.getValue() != null) {
      Period distance = Period.between(this.startDate.getValue(), this.endDate.getValue());
      this.daysDivider.setText(formatPeriod(distance));
    } else {
      this.daysDivider.setEmptyText();
    }
    if (this.startTime.getValue() != null && this.endTime.getValue() != null) {
      Duration duration = Duration.between(this.startTime.getValue(), this.endTime.getValue());
      this.timeDivider.setText(formatDuration(duration));
    } else {
      this.timeDivider.setEmptyText();
    }
  }

  // UI Components
  private Component getDateSelectors() {
    VerticalLayout layout = new VerticalLayout();
    layout.addClassNames(Gap.SMALL, Padding.NONE, Gap.XSMALL);

    Div headerWrapper = new Div();
    headerWrapper.addClassNames(
        Display.FLEX,
        AlignItems.CENTER,
        Gap.SMALL,
        Position.RELATIVE, // Required for the circle
        Bottom.SMALL
    );

    datesTitle = new H5("Select dates range");

    this.dateCircle = new Circle();

    headerWrapper.add(this.dateCircle, datesTitle);

    this.startDate = new DatePicker();
    this.startDate.setPlaceholder("Start date");
    this.startDate.setClearButtonVisible(true);

    this.endDate = new DatePicker();
    this.endDate.setPlaceholder("End date");
    this.endDate.setClearButtonVisible(true);

    this.daysDivider = new SpanLine();

    HorizontalLayout selectorLayout = new HorizontalLayout();
    selectorLayout.addClassNames(Gap.SMALL);
    selectorLayout.add(this.startDate, this.daysDivider, this.endDate);

    layout.add(headerWrapper, selectorLayout);

    return layout;

  }

  private Component getDaysSelector() {
    VerticalLayout layout = new VerticalLayout();
    layout.addClassNames(AlignItems.STRETCH, Bottom.NONE, Padding.NONE, Gap.XSMALL);

    daysTitle = new H5("Select days");

    HorizontalLayout headerLayout = new HorizontalLayout();
    headerLayout.addClassNames(
        AlignItems.CENTER,
        Position.RELATIVE,
        JustifyContent.BETWEEN
    );

    weekendChip = new Chip("Weekend");
    weekdaysChip = new Chip("Weekdays");
    allDaysChip = new Chip("All");

    daysChipGroup = new ChipGroup(
        weekendChip,
        weekdaysChip,
        allDaysChip
    );

    this.daysCircle = new Circle();

    headerLayout.add(this.daysCircle, daysTitle, daysChipGroup);

    this.weekDays = new DayOfWeekSelector();
    this.weekDays.getChildren().forEach(e -> {
          e.getStyle().setScale("1.15");
        }
    );
    this.weekDays.addValueChangeListener(ev -> revalidate());
    this.weekDays.setFirstDayOfWeek(DayOfWeek.SUNDAY);
    this.weekDays.addClassNames(
        AlignSelf.CENTER,
        Padding.NONE,
        Margin.NONE
    );
    this.daysInitials = List.of("S","M","T","W","T","F","S");
    this.weekDays.setWeekDaysShort(this.daysInitials);

    weekendChip.onPress(checked -> {
      if (checked) {
        this.weekDays.setValue(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY);
      }
      this.weekDays.setReadOnly(checked);
      revalidate();
    });

    weekdaysChip.onPress(checked -> {
      if (checked) {
        this.weekDays.setValue(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        );
      }
      this.weekDays.setReadOnly(checked);
      revalidate();
    });

    allDaysChip.onPress(checked -> {
      if (checked) {
        this.weekDays.setValue(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        );
      }
      this.weekDays.setReadOnly(checked);
      revalidate();
    });

    layout.add(headerLayout, this.weekDays);

    return layout;
  }

  private Component getTimeSelectors() {
    VerticalLayout layout = new VerticalLayout();
    layout.addClassNames(AlignItems.STRETCH, Padding.NONE, Gap.XSMALL);

    timesTitle = new H5("Select times range");

    morningChip = new Chip("Morning");
    afterNoonChip = new Chip("Afternoon");
    allTimeChip = new Chip("All");
    timeChipGroup = new ChipGroup(
        morningChip,
        afterNoonChip,
        allTimeChip
    );

    this.startTime = new TimePicker();
    this.startTime.setPlaceholder("Start time");
    this.startTime.setStep(Duration.ofMinutes(30));
    // Sets the TimePicker to use 24-hours format
    this.startTime.setLocale(Locale.FRENCH);
    this.startTime.setClearButtonVisible(true);

    this.endTime = new TimePicker();
    this.endTime.setPlaceholder("End time");
    this.endTime.setStep(Duration.ofMinutes(30));
    this.endTime.setLocale(Locale.FRENCH);
    this.endTime.setClearButtonVisible(true);

    morningChip.onPress(checked -> {
      if (checked) {
        LocalTime minDate = this.startTime.getMin() != null ? this.startTime.getMin() : LocalTime.MIN;
        LocalTime maxDate = this.endTime.getMax() != null ? this.startTime.getMax() : LocalTime.MAX;
        this.startTime.setValue(minDate);
        this.endTime.setValue(maxDate.isBefore(LocalTime.NOON) ? maxDate : LocalTime.NOON);
        this.startTime.setReadOnly(true);
        this.endTime.setReadOnly(true);
        revalidate();
      }
      this.startTime.setReadOnly(checked);
      this.endTime.setReadOnly(checked);
    });

    afterNoonChip.onPress(checked -> {
      if (checked) {
        LocalTime minDate = this.startTime.getMin() != null ? this.startTime.getMin() : LocalTime.MIN;
        LocalTime maxDate = this.endTime.getMax() != null ? this.startTime.getMax() : LocalTime.MAX;
        this.startTime.setValue(minDate.isAfter(LocalTime.NOON) ? minDate : LocalTime.NOON);
        this.endTime.setValue(maxDate.isBefore(LocalTime.NOON) ? maxDate : LocalTime.NOON);
        this.endTime.setValue(maxDate);
        revalidate();
      }
      this.startTime.setReadOnly(checked);
      this.endTime.setReadOnly(checked);
    });

    allTimeChip.onPress(checked -> {
      if (checked) {
        LocalTime minDate = this.startTime.getMin() != null ? this.startTime.getMin() : LocalTime.MIN;
        LocalTime maxDate = this.endTime.getMax() != null ? this.startTime.getMax() : LocalTime.MAX;
        this.startTime.setValue(minDate);
        this.endTime.setValue(maxDate);
        revalidate();
      }
      this.startTime.setReadOnly(checked);
      this.endTime.setReadOnly(checked);
    });

    this.timeDivider = new SpanLine();

    HorizontalLayout headerLayout = new HorizontalLayout();
    headerLayout.addClassNames(AlignItems.CENTER, Position.RELATIVE, JustifyContent.BETWEEN);

    this.timeCircle = new Circle();

    headerLayout.add(this.timeCircle, timesTitle, timeChipGroup);

    HorizontalLayout selectorLayout = new HorizontalLayout();
    selectorLayout.addClassNames(Gap.SMALL);
    selectorLayout.add(this.startTime, this.timeDivider, this.endTime);

    layout.add(headerLayout, selectorLayout);

    return layout;
  }

  // Fires a revalidation when data changes
  private void revalidate() {
    getElement().executeJs("this.dispatchEvent(new Event('change'))");
  }

  // Custom field
  @Override
  protected DateTimeRange generateModelValue() {
    return new DateTimeRange(
        startDate.getValue(),
        endDate.getValue(),
        startTime.getValue(),
        endTime.getValue(),
        weekDays.getValue()
    );
  }

  @Override
  protected void setPresentationValue(DateTimeRange dateTimeRange) {

    startDate.setValue(dateTimeRange.getStartDate());
    endDate.setValue(dateTimeRange.getEndDate());
    startTime.setValue(dateTimeRange.getStartTime());
    endTime.setValue(dateTimeRange.getEndTime());
    weekDays.setValue(dateTimeRange.getWeekDays());

    daysDivider.setText(formatPeriod(dateTimeRange.getDaysSpan()));
    Duration timeDuration = dateTimeRange.getDayDuration();
    timeDivider.setText(formatDuration(timeDuration));
  }

  @Override
  public Validator<DateTimeRange> getDefaultValidator() {
    return new DateTimeRangePickerValidator(this);
  }

  // api

  /**
   * Changes this component's visibility state
   *
   * @param visible whether this component should be visible
   */
  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    this.setDatesVisible(visible);
    this.setDaysVisible(visible);
    this.setTimesVisible(visible);
  }

  /**
   * Changes this component's read-only state
   *
   * @param readOnly whether this component should be read-only
   */
  @Override
  public void setReadOnly(boolean readOnly) {
    getElement().setProperty("readonly", readOnly);
    this.setDaysReadOnly(readOnly);
    this.setTimesReadOnly(readOnly);
    this.setDatesReadOnly(readOnly);
    this.timeChipGroup.setReadOnly(readOnly);
    this.daysChipGroup.setReadOnly(readOnly);
  }

  /**
   * Sets the maximum days distance between start and end dates
   *
   * @param max the maximum distance measured in days
   */
  public void setMaxDaysSpan(int max) {

    this.startDate.addValueChangeListener(it -> {
      LocalDate current = it.getValue();
      LocalDate maxDate = current != null ? current.plusDays(max) : null;
      if (max > 0) {
        this.endDate.setMax(maxDate);
      }
    });

    this.endDate.addValueChangeListener(it -> {
          LocalDate current = it.getValue();
          LocalDate minDate = current != null ? current.minusDays(max) : null;
          if (max > 0) {
            this.startDate.setMin(minDate);
          }
        }
    );
  }

  /**
   * Sets the minimum start date
   *
   * @param date the minimum date that can be selected
   */
  public void setMinDate(LocalDate date) {
    this.startDate.setMin(date);
    this.endDate.setMin(date);
  }

  /**
   * Sets the maximum end date
   *
   * @param date the maximum date that can be selected
   */
  public void setMaxDate(LocalDate date) {
    this.endDate.setMax(date);
    this.startDate.setMax(date);
  }

  /**
   * Sets the minimum start time
   *
   * @param time the minimum time that can be selected
   */
  public void setMinTime(LocalTime time) {
    this.startTime.setMin(time);
    this.endTime.setMin(time);
    morningChip.setVisible(time.isBefore(LocalTime.NOON));
  }

  /**
   * Sets the maximum end time
   *
   * @param time the maximum time that can be selected
   */
  public void setMaxTime(LocalTime time) {
    this.endTime.setMax(time);
    this.startTime.setMax(time);
    afterNoonChip.setVisible(time.isAfter(LocalTime.NOON));
  }

  /**
   * Sets the selected week days
   *
   * @param weekDays the days that will be selected
   * <br>note that days not included will be deselected
   */
  public void setWeekDays(DayOfWeek... weekDays) {
    this.weekDays.setValue(Set.of(weekDays));
  }

  /**
   * Sets which day should be placed at the starting or left-most position
   *
   * @param weekDay the starting or left-most day
   */
  public void setFirstWeekDay(DayOfWeek weekDay) {
    this.weekDays.setFirstDayOfWeek(weekDay);
  }

  /**
   * Changes the date pickers' read-only state
   *
   * @param readOnly whether the date pickers should be read-only
   */
  public void setDatesReadOnly(boolean readOnly) {
    this.startDate.setReadOnly(readOnly);
    this.endDate.setReadOnly(readOnly);
  }

  /**
   * Changes the date pickers' visibility state
   *
   * @param visible whether the date pickers should be visible
   */
  public void setDatesVisible(boolean visible) {
    this.dateSelector.setVisible(visible);
  }

  /**
   * Changes the days picker's read-only state
   *
   * @param readOnly whether the days picker should be read-only
   */
  public void setDaysReadOnly(boolean readOnly) {
    this.weekDays.setReadOnly(readOnly);
    this.daysChipGroup.setReadOnly(readOnly);
  }

  /**
   * Changes the days picker's visibility state
   *
   * @param visible whether the days picker should be visible
   */
  public void setDaysVisible(boolean visible) {
    this.daysSelector.setVisible(visible);
  }

  /**
   * Changes the time pickers' read-only state
   *
   * @param readOnly whether the time pickers should be read-only
   */
  public void setTimesReadOnly(boolean readOnly) {
    this.startTime.setReadOnly(readOnly);
    this.endTime.setReadOnly(readOnly);
    this.timeChipGroup.setReadOnly(readOnly);
  }

  /**
   * Changes the time pickers' visibility state
   *
   * @param visible whether the time pickers should be visible
   */
  public void setTimesVisible(boolean visible) {
    this.timeSelector.setVisible(visible);
  }

  /**
   * Changes the left line indicator's visibility state
   *
   * @param visible whether the left indicator should be visible
   */
  public void setIndicatorVisible(boolean visible) {
    this.verticalLine.setVisible(visible);
    this.dateCircle.setVisible(visible);
    this.daysCircle.setVisible(visible);
    this.timeCircle.setVisible(visible);
  }

  /**
   * Sets the minimum time gap for the time selection lists
   *
   * @param step the time difference between adjacent lists' items
   */
  public void setTimeStep(Duration step) {
    this.startTime.setStep(step);
    this.endTime.setStep(step);
  }

  /**
   * Sets the time locale for the time selection lists
   *
   * @param locale the {@code Locale} to use for the time lists' items
   */
  public void setTimeLocale(Locale locale) {
    this.startTime.setLocale(locale);
    this.endTime.setLocale(locale);
  }

  /**
   * Sets the custom text properties for internationalization purposes
   *
   * @param i18n instance to attach
   * @see DateTimeRangePickerI18n
   */
  public void setI18n(DateTimeRangePickerI18n i18n) {
    i18n.component = this;
    for(Runnable action : i18n.actions) {
      if(action != null) action.run();
    }
  }

  public DateTimeRangePicker() {
    this(defaultErrorMessage);
  }

  public DateTimeRangePicker(String errorMessage) {
    super();
    setErrorMessage(errorMessage);
    setUI();
  }

  public DateTimeRangePicker(DateTimeRange defaultValue, String errorMessage) {
    super(defaultValue);
    setErrorMessage(errorMessage);
    setUI();
  }

  public DateTimeRangePicker(DateTimeRange defaultValue) {
    this(defaultValue, defaultErrorMessage);
  }

  public static String formatDuration(Duration duration) {
    return String.format("%02d:%02d:%02d",
        duration.toHoursPart(),
        duration.toMinutesPart(),
        duration.toSecondsPart()
    );
  }

  protected static String formatPeriod(Period period) {
    return String.format("%dD", period.getDays());
  }

}
