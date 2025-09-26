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
package com.flowingcode.vaadin.addons.recurrentschedulefield.ui;

import com.flowingcode.vaadin.addons.recurrentschedulefield.api.DateTimeRange;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

/**
 * Validator for {@link RecurrentScheduleField} component.
 * <p>
 * Ensures that the selected date and time ranges, as well as the selected weekdays,
 * are valid according to the component's rules.
 * </p>
 *
 * @author Flowing Code
 */
public class RecurrentScheduleFieldValidator implements Validator<DateTimeRange> {

  private final RecurrentScheduleField model;

  /**
   * Creates a new validator for the specified {@link RecurrentScheduleField} component.
   * <p>
   * Attaches manual validation to the date, time, and weekday selection fields of the picker.
   * </p>
   *
   * @param model the {@code RecurrentScheduleField} component to validate
   */
  public RecurrentScheduleFieldValidator(RecurrentScheduleField model) {
      this.model = model;
      setManualValidation();
  }

  private void setManualValidation() {
    model.getStartTimePicker().setManualValidation(true);
    model.getEndTimePicker().setManualValidation(true);
    model.getStartDatePicker().setManualValidation(true);
    model.getEndDatePicker().setManualValidation(true);
    model.getWeekDaySelector().setManualValidation(true);
  }

  // Since a DateTimeRange instance is always valid, checking for its presence is enough
  @Override
  public ValidationResult apply(DateTimeRange data, ValueContext valueContext) {
    ValidationResult result;
    result = data != null ? ValidationResult.ok() : ValidationResult.error(model.getErrorMessage());

    return result;
  }

  // Checks if UI is valid
  public boolean isValid() {
    boolean datesOk = dateValidation(model.getStartDatePicker().getValue(), model.getEndDatePicker().getValue());
    boolean daysOk = daysValidation(model.getWeekDaySelector().getValue());
    boolean timesOk = timeValidation(model.getStartTimePicker().getValue(), model.getEndTimePicker().getValue());
    model.refreshUI(datesOk, daysOk, timesOk);
    return datesOk && daysOk && timesOk;
  }

  private boolean dateValidation(LocalDate start, LocalDate end) {
    boolean notNull = (start != null && end != null);
    return notNull && start.isBefore(end);
  }

  private boolean timeValidation(LocalTime start, LocalTime end) {
    boolean notNull = (start != null && end != null);
    return notNull && start.isBefore(end);
  }

  private boolean daysValidation(Set<DayOfWeek> weekDays) {
    boolean notNull = weekDays != null;
    return notNull && !weekDays.isEmpty();
  }


}
