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
import com.flowingcode.vaadin.addons.datetimerangepicker.api.TimeInterval;
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
import com.vaadin.flow.function.SerializableRunnable;
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
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A component to generate {@link TimeInterval} instances by customizing a {@link DateTimeRange}.
 *
 * <h5>Features:</h5>
 * <ul>
 * <li>Date range: Select start and end dates with optional maximum span.</li>
 * <li>Days: Choose specific days of the week or predefined groups.</li>
 * <li>Time range: Define start and end times with customizable steps and locale.</li>
 * </ul>
 *
 * <p>
 * Provides validation, customization, and internationalization support for flexible scheduling.
 * </p>
 *
 * @author Flowing Code
 * @see DateTimeRange
 */
public class DateTimeRangePicker
    extends CustomField<DateTimeRange> implements HasValidator<DateTimeRange> {

  static final String defaultErrorMessage = "Invalid or incomplete fields remaining";
  private static final String SUCCESS_COLOR = "var(--lumo-primary-color)";
  private static final String ERROR_COLOR = "var(--lumo-error-color)";

  // Mandatory attributes for validation
  private DateTimeRangePickerValidator validator;
  private DatePicker startDatePicker;
  private DatePicker endDatePicker;
  private TimePicker startTimePicker;
  private TimePicker endTimePicker;
  private DayOfWeekSelector weekDaySelector;

  // UI-only validation attributes (should change after validation check)
  private SpanLine daysDivider;
  private SpanLine timeDivider;
  private Circle dateCircle;
  private Circle timeCircle;
  private Circle daysCircle;

  // Others
  private H5 datesTitle;
  private H5 daysTitle;
  private H5 timesTitle;
  private Component dateSelector;
  private Component daysSelector;
  private Component timeSelector;
  private Div verticalLine;
  private ChipGroup daysChipGroup;
  private Chip weekdaysChip;
  private Chip weekendChip;
  private Chip allDaysChip;
  private ChipGroup timeChipGroup;
  private Chip morningChip;
  private Chip afterNoonChip;
  private Chip allTimeChip;
  private List<String> daysInitials;
  private Integer maxDaysSpan = null;

  /**
   * Creates a new {@code DateTimeRangePicker} with the default error message.
   */
  public DateTimeRangePicker() {
    this(defaultErrorMessage);
  }

  /**
   * Creates a new {@code DateTimeRangePicker} with a custom error message.
   *
   * @param errorMessage the error message to display when validation fails
   */
  public DateTimeRangePicker(String errorMessage) {
    super();
    setUI();
    setDefaultI18n();
    setErrorMessage(errorMessage);
  }

  /**
   * Creates a new {@code DateTimeRangePicker} with a default value and custom error message.
   *
   * @param defaultValue the initial {@link DateTimeRange} value
   * @param errorMessage the error message to display when validation fails
   */
  public DateTimeRangePicker(DateTimeRange defaultValue, String errorMessage) {
    super(defaultValue);
    setUI();
    setDefaultI18n();
    setErrorMessage(errorMessage);
    setPresentationValue(defaultValue);
  }

  /**
   * Creates a new {@code DateTimeRangePicker} with a default value.
   *
   * @param defaultValue the initial {@link DateTimeRange} value
   */
  public DateTimeRangePicker(DateTimeRange defaultValue) {
    this(defaultValue, defaultErrorMessage);
  }

  private void setUI() {
    addClassNames(
        Width.AUTO,
        Display.INLINE,
        Margin.NONE,
        Padding.SMALL
    );

    final HorizontalLayout rootLayout = new HorizontalLayout();
    rootLayout.addClassNames(
        Padding.SMALL,
        Width.FULL,
        Height.FULL,
        AlignItems.STRETCH,
        Gap.MEDIUM
    );

    final VerticalLayout mainLayout = new VerticalLayout();
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

    validator = new DateTimeRangePickerValidator(this);

    mainLayout.add(dateSelector, daysSelector, timeSelector);
    rootLayout.add(verticalLine, mainLayout);
    add(rootLayout);
  }

  void refreshUI(boolean datesOk, boolean daysOk, boolean timesOk) {
    daysDivider.setText(formatDaysSpan(startDatePicker.getValue(), endDatePicker.getValue()));
    timeDivider.setText(formatTimeSpan(startTimePicker.getValue(), endTimePicker.getValue()));

    startDatePicker.setInvalid(startDatePicker.getValue() != null && endDatePicker.getValue() != null && !datesOk);
    endDatePicker.setInvalid(startDatePicker.getValue() != null && endDatePicker.getValue() != null && !datesOk);
    dateCircle.setColor(datesOk ? SUCCESS_COLOR : ERROR_COLOR);

    daysCircle.setColor(daysOk ? SUCCESS_COLOR : ERROR_COLOR);

    startTimePicker.setInvalid(startTimePicker.getValue() != null && endTimePicker.getValue() != null && !timesOk);
    endTimePicker.setInvalid(startTimePicker.getValue() != null && endTimePicker.getValue() != null && !timesOk);
    timeCircle.setColor(timesOk ? SUCCESS_COLOR : ERROR_COLOR);
  }

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

    datesTitle = new H5();

    dateCircle = new Circle();

    headerWrapper.add(dateCircle, datesTitle);

    startDatePicker = new DatePicker();
    startDatePicker.setClearButtonVisible(true);
    startDatePicker.addValueChangeListener(it -> {
      if (maxDaysSpan != null && maxDaysSpan > 0) {
        LocalDate current = it.getValue();
        LocalDate maxDate = current != null ? current.plusDays(maxDaysSpan) : null;
        endDatePicker.setMax(maxDate);
      }
    });

    endDatePicker = new DatePicker();
    endDatePicker.setClearButtonVisible(true);
    endDatePicker.addValueChangeListener(it -> {
      if (maxDaysSpan != null && maxDaysSpan > 0) {
        LocalDate current = it.getValue();
        LocalDate minDate = current != null ? current.minusDays(maxDaysSpan) : null;
        startDatePicker.setMin(minDate);
      }
    });

    daysDivider = new SpanLine();

    HorizontalLayout selectorLayout = new HorizontalLayout();
    selectorLayout.addClassNames(Gap.SMALL);
    selectorLayout.add(startDatePicker, daysDivider, endDatePicker);

    layout.add(headerWrapper, selectorLayout);

    return layout;
  }

  private Component getDaysSelector() {
    VerticalLayout layout = new VerticalLayout();
    layout.addClassNames(AlignItems.STRETCH, Bottom.NONE, Padding.NONE, Gap.XSMALL);

    daysTitle = new H5();

    HorizontalLayout headerLayout = new HorizontalLayout();
    headerLayout.addClassNames(
        AlignItems.CENTER,
        Position.RELATIVE,
        JustifyContent.BETWEEN
    );

    weekendChip = new Chip();
    weekdaysChip = new Chip();
    allDaysChip = new Chip();

    daysChipGroup = new ChipGroup(
        weekendChip,
        weekdaysChip,
        allDaysChip
    );

    daysCircle = new Circle();

    headerLayout.add(daysCircle, daysTitle, daysChipGroup);

    weekDaySelector = new DayOfWeekSelector();
    weekDaySelector.getChildren().forEach(e ->
        e.getStyle().setScale("1.15")
    );
    weekDaySelector.addValueChangeListener(ev -> updateValue());
    weekDaySelector.setFirstDayOfWeek(DayOfWeek.SUNDAY);
    weekDaySelector.addClassNames(
        AlignSelf.CENTER,
        Padding.NONE,
        Margin.NONE
    );

    weekendChip.onPress(checked -> {
      if (checked) {
        weekDaySelector.setValue(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY);
        updateValue();
      }
      weekDaySelector.setReadOnly(checked);
    });

    weekdaysChip.onPress(checked -> {
      if (checked) {
        weekDaySelector.setValue(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        );
        updateValue();
      }
      weekDaySelector.setReadOnly(checked);
    });

    allDaysChip.onPress(checked -> {
      if (checked) {
        weekDaySelector.setValue(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        );
        updateValue();
      }
      weekDaySelector.setReadOnly(checked);
    });

    layout.add(headerLayout, weekDaySelector);

    return layout;
  }

  private Component getTimeSelectors() {
    VerticalLayout layout = new VerticalLayout();
    layout.addClassNames(AlignItems.STRETCH, Padding.NONE, Gap.XSMALL);

    timesTitle = new H5();

    morningChip = new Chip();
    afterNoonChip = new Chip();
    allTimeChip = new Chip();
    timeChipGroup = new ChipGroup(
        morningChip,
        afterNoonChip,
        allTimeChip
    );

    startTimePicker = new TimePicker();
    startTimePicker.setStep(Duration.ofMinutes(30));
    // Sets the TimePicker to use 24-hours format
    startTimePicker.setLocale(Locale.FRENCH);
    startTimePicker.setClearButtonVisible(true);

    endTimePicker = new TimePicker();
    endTimePicker.setStep(Duration.ofMinutes(30));
    endTimePicker.setLocale(Locale.FRENCH);
    endTimePicker.setClearButtonVisible(true);

    morningChip.onPress(checked ->
        applyTime(LocalTime.MIN, LocalTime.NOON, checked)
    );

    afterNoonChip.onPress(checked ->
        applyTime(LocalTime.NOON, LocalTime.MAX, checked)
    );

    allTimeChip.onPress(checked ->
        applyTime(LocalTime.MIN, LocalTime.MAX, checked)
    );

    timeDivider = new SpanLine();

    HorizontalLayout headerLayout = new HorizontalLayout();
    headerLayout.addClassNames(AlignItems.CENTER, Position.RELATIVE, JustifyContent.BETWEEN);

    timeCircle = new Circle();

    headerLayout.add(timeCircle, timesTitle, timeChipGroup);

    HorizontalLayout selectorLayout = new HorizontalLayout();
    selectorLayout.addClassNames(Gap.SMALL);
    selectorLayout.add(startTimePicker, timeDivider, endTimePicker);

    layout.add(headerLayout, selectorLayout);

    return layout;
  }

  /**
   * Retrieves the current value of the DateTimeRangePicker component.
   *
   * @return the current {@link DateTimeRange} value, or null if the fields are invalid or incomplete
   */
  @Override
  public DateTimeRange getValue() {
    return super.getValue();
  }

  @Override
  protected DateTimeRange generateModelValue() {
    boolean isValid = validator.isValid();
    setInvalid(!isValid);

    if(isValid) {
      return new DateTimeRange(
          startDatePicker.getValue(),
          endDatePicker.getValue(),
          startTimePicker.getValue(),
          endTimePicker.getValue(),
          weekDaySelector.getValue()
      );
    }
    else return null;
  }

  @Override
  protected void setPresentationValue(DateTimeRange dateTimeRange) {
    if (dateTimeRange == null) {
      startDatePicker.clear();
      endDatePicker.clear();
      startTimePicker.clear();
      endTimePicker.clear();
      weekDaySelector.clear();
      daysDivider.clearText();
      timeDivider.clearText();
    }
    else {
      startDatePicker.setValue(dateTimeRange.getStartDate());
      endDatePicker.setValue(dateTimeRange.getEndDate());
      startTimePicker.setValue(dateTimeRange.getStartTime());
      endTimePicker.setValue(dateTimeRange.getEndTime());
      weekDaySelector.setValue(dateTimeRange.getWeekDays());

      daysDivider.setText(formatDaysSpan(dateTimeRange.getStartDate(), dateTimeRange.getEndDate()));
      timeDivider.setText(formatTimeSpan(dateTimeRange.getStartTime(), dateTimeRange.getEndTime()));
    }
  }

  @Override
  public Validator<DateTimeRange> getDefaultValidator() {
    return validator;
  }

  /**
   * Changes this component's visibility state.
   *
   * @param visible whether this component should be visible
   */
  @Override
  public void setVisible(boolean visible) {
    getElement().setVisible(visible);
    setDatesVisible(visible);
    setDaysVisible(visible);
    setTimesVisible(visible);
  }

  /**
   * Changes this component's read-only state.
   *
   * @param readOnly whether this component should be read-only
   */
  @Override
  public void setReadOnly(boolean readOnly) {
    getElement().setProperty("readonly", readOnly);
    setDaysReadOnly(readOnly);
    setTimesReadOnly(readOnly);
    setDatesReadOnly(readOnly);
    timeChipGroup.setReadOnly(readOnly);
    daysChipGroup.setReadOnly(readOnly);
  }

  /**
   * Sets the maximum days distance between start and end dates.
   *
   * @param max the maximum distance measured in days
   */
  public void setMaxDaysSpan(int max) {
    maxDaysSpan = max;
  }

  /**
   * Sets the minimum start date.
   *
   * @param date the minimum date that can be selected
   */
  public void setMinDate(LocalDate date) {
    startDatePicker.setMin(date);
    endDatePicker.setMin(date);
  }

  /**
   * Sets the maximum end date.
   *
   * @param date the maximum date that can be selected
   */
  public void setMaxDate(LocalDate date) {
    endDatePicker.setMax(date);
    startDatePicker.setMax(date);
  }

  /**
   * Sets the minimum start time.
   *
   * @param time the minimum time that can be selected
   */
  public void setMinTime(LocalTime time) {
    startTimePicker.setMin(time);
    endTimePicker.setMin(time);
    // If afternoon chip is not visible, keep only the 'all time' chip
    morningChip.setVisible((time == null || time.isBefore(LocalTime.NOON)) && afterNoonChip.isVisible());
  }

  /**
   * Sets the maximum end time.
   *
   * @param time the maximum time that can be selected
   */
  public void setMaxTime(LocalTime time) {
    startTimePicker.setMax(time);
    endTimePicker.setMax(time);
    // If morning chip is not visible, keep only the 'all time' chip
    afterNoonChip.setVisible((time == null || time.isAfter(LocalTime.NOON)) && morningChip.isVisible());
  }

  /**
   * Sets the selected week days.
   *
   * @param weekDaySelector the days that will be selected
   * <br>note that days not included will be deselected
   */
  public void setWeekDays(DayOfWeek... weekDaySelector) {
    this.weekDaySelector.setValue(Set.of(weekDaySelector));
  }

  /**
   * Sets which day should be placed at the starting or left-most position.
   *
   * @param weekDay the starting or left-most day
   */
  public void setFirstWeekDay(DayOfWeek weekDay) {
    weekDaySelector.setFirstDayOfWeek(weekDay);
  }

  /**
   * Changes the date pickers' read-only state.
   *
   * @param readOnly whether the date pickers should be read-only
   */
  public void setDatesReadOnly(boolean readOnly) {
    startDatePicker.setReadOnly(readOnly);
    endDatePicker.setReadOnly(readOnly);
  }

  /**
   * Changes the date pickers' visibility state.
   *
   * @param visible whether the date pickers should be visible
   */
  public void setDatesVisible(boolean visible) {
    dateSelector.setVisible(visible);
  }

  /**
   * Changes the days picker's read-only state.
   *
   * @param readOnly whether the days picker should be read-only
   */
  public void setDaysReadOnly(boolean readOnly) {
    weekDaySelector.setReadOnly(readOnly);
    daysChipGroup.setReadOnly(readOnly);
  }

  /**
   * Changes the days picker's visibility state.
   *
   * @param visible whether the days picker should be visible
   */
  public void setDaysVisible(boolean visible) {
    daysSelector.setVisible(visible);
  }

  /**
   * Changes the days chips' visibility state.
   *
   * @param visible whether the days chips should be visible
   */
  public void setDaysChipsVisible(boolean visible) {
    daysChipGroup.setVisible(visible);
  }

  /**
   * Changes the time pickers' read-only state.
   *
   * @param readOnly whether the time pickers should be read-only
   */
  public void setTimesReadOnly(boolean readOnly) {
    startTimePicker.setReadOnly(readOnly);
    endTimePicker.setReadOnly(readOnly);
    timeChipGroup.setReadOnly(readOnly);
  }

  /**
   * Changes the time pickers' visibility state.
   *
   * @param visible whether the time pickers should be visible
   */
  public void setTimesVisible(boolean visible) {
    timeSelector.setVisible(visible);
  }

  /**
   * Changes the time chips' visibility state.
   *
   * @param visible whether the time chips should be visible
   */
  public void setTimeChipsVisible(boolean visible) {
    timeChipGroup.setVisible(visible);
  }

  /**
   * Changes the left line indicator's visibility state.
   *
   * @param visible whether the left indicator should be visible
   */
  public void setIndicatorVisible(boolean visible) {
    verticalLine.setVisible(visible);
    dateCircle.setVisible(visible);
    daysCircle.setVisible(visible);
    timeCircle.setVisible(visible);
  }

  /**
   * Sets the minimum time gap for the time selection lists.
   *
   * @param step the time difference between adjacent lists' items
   */
  public void setTimeStep(Duration step) {
    startTimePicker.setStep(step);
    endTimePicker.setStep(step);
  }

  /**
   * Sets the time locale for the time selection lists.
   *
   * @param locale the {@code Locale} to use for the time lists' items
   */
  public void setTimeLocale(Locale locale) {
    startTimePicker.setLocale(locale);
    endTimePicker.setLocale(locale);
  }

  /**
   * Sets the custom text properties for internationalization purposes.
   *
   * @param i18n instance to attach
   * @see DateTimeRangePickerI18n
   */
  public void setI18n(DateTimeRangePickerI18n i18n) {
    if(i18n != null) {
      i18n.attachComponent(this);
      for (SerializableRunnable action : i18n.getPendingActions()) {
        if (action != null)
          action.run();
      }
    }
  }

  private void setDefaultI18n() {
    DateTimeRangePickerI18n defaultI18n = new DateTimeRangePickerI18n()
        .setDatesTitle("Select dates range")
        .setDatesPlaceholder("Start date", "End date")
        .setDaysTitle("Select days")
        .setDayInitials(List.of("S","M","T","W","T","F","S"))
        .setDaysChipsText("Weekend", "Weekdays", "All")
        .setTimesTitle("Select times range")
        .setTimeChipsText("Morning", "Afternoon", "All")
        .setTimesPlaceholder("Start time", "End time");
    setI18n(defaultI18n);
  }

  // Fit start and end times within current time range constraints
  private void applyTime(LocalTime start, LocalTime end, boolean checked) {
    if(checked) {
      LocalTime min = startTimePicker.getMin();
      LocalTime max = endTimePicker.getMax();
      LocalTime minTime = min != null && min.isAfter(start) ? min : start;
      LocalTime maxTime = max != null && max.isBefore(end) ? max : end;
      startTimePicker.setValue(minTime);
      endTimePicker.setValue(maxTime);
      updateValue();
    }
    startTimePicker.setReadOnly(checked);
    endTimePicker.setReadOnly(checked);
  }

  private static String formatTimeSpan(LocalTime startTime, LocalTime endTime) {
    if(startTime == null || endTime == null) return "";

    Duration duration = Duration.between(startTime, endTime);
    int hours = duration.toHoursPart();
    int minutes = duration.toMinutesPart();
    int seconds = duration.toSecondsPart();
    boolean isNegative = hours < 0 || minutes < 0 || seconds < 0;

    return String.format((isNegative ? "-" : "+") + "%02d:%02d:%02d",
        isNegative ? Math.abs(hours) : hours,
        isNegative ? Math.abs(minutes) : minutes,
        isNegative ? Math.abs(seconds) : seconds
    );
  }

  private static String formatDaysSpan(LocalDate startDate, LocalDate endDate) {
    if(startDate == null || endDate == null) return "";
    long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    return days >= 0 ? "+" + days : "" + days;
  }

  // Getters & Setters

  DatePicker getStartDatePicker() {
    return startDatePicker;
  }

  DatePicker getEndDatePicker() {
    return endDatePicker;
  }

  TimePicker getStartTimePicker() {
    return startTimePicker;
  }

  TimePicker getEndTimePicker() {
    return endTimePicker;
  }

  DayOfWeekSelector getWeekDaySelector() {
    return weekDaySelector;
  }

  H5 getDatesTitle() {
    return datesTitle;
  }

  H5 getDaysTitle() {
    return daysTitle;
  }

  H5 getTimesTitle() {
    return timesTitle;
  }

  Chip getWeekdaysChip() {
    return weekdaysChip;
  }

  Chip getWeekendChip() {
    return weekendChip;
  }

  Chip getAllDaysChip() {
    return allDaysChip;
  }

  Chip getMorningChip() {
    return morningChip;
  }

  Chip getAfterNoonChip() {
    return afterNoonChip;
  }

  Chip getAllTimeChip() {
    return allTimeChip;
  }

  List<String> getDaysInitials() {
    return daysInitials;
  }

  void setDaysInitials(List<String> daysInitials) {
    this.daysInitials = daysInitials;
  }
}
