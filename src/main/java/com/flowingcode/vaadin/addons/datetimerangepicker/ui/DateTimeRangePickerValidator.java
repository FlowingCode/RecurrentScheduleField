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
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class DateTimeRangePickerValidator implements Validator<DateTimeRange> {

  private final DateTimeRangePicker model;

  private static final String SUCCESS_COLOR = "var(--lumo-primary-color)";
  private static final String ERROR_COLOR = "var(--lumo-error-color)";

  public DateTimeRangePickerValidator(DateTimeRangePicker model) {
    this.model = model;
    setManualValidation();
  }

  private void setManualValidation() {
    model.startTime.setManualValidation(true);
    model.endTime.setManualValidation(true);
    model.startDate.setManualValidation(true);
    model.endDate.setManualValidation(true);
    model.weekDays.setManualValidation(true);
  }

  @Override
  public ValidationResult apply(DateTimeRange data, ValueContext valueContext) {
    ValidationResult result;
    if (
        data != null &&
            dateValidation(data.getStartDate(), data.getEndDate())
                & timeValidation(data.getStartTime(), data.getEndTime())  //Single & is intentional
                & daysValidation(data.getWeekDays())
    ) {
      result = ValidationResult.ok();
    } else {
      result = ValidationResult.error(model.getErrorMessage());
    }

    this.model.refreshUI();
    return result;
  }

  private boolean dateValidation(LocalDate start, LocalDate end) {
    boolean nullCheck = (start != null && end != null);
    if (!nullCheck) {
      model.startDate.setInvalid(false);
      model.endDate.setInvalid(false);
    } else {
      boolean dateCheck = start.isBefore(end);
      if (!dateCheck) {
        model.startDate.setInvalid(true);
        model.endDate.setInvalid(true);
        model.dateCircle.setColor(ERROR_COLOR);
      } else {
        model.startDate.setInvalid(false);
        model.endDate.setInvalid(false);
        model.dateCircle.setColor(SUCCESS_COLOR);
        return true;
      }
    }
    return false;
  }

  private boolean timeValidation(LocalTime start, LocalTime end) {
    boolean nullCheck = (start != null && end != null);
    if (!nullCheck) {
      model.startTime.setInvalid(false);
      model.endTime.setInvalid(false);
    } else {
      boolean dateCheck = start.isBefore(end);
      if (!dateCheck) {
        model.startTime.setInvalid(true);
        model.endTime.setInvalid(true);
        model.timeCircle.setColor(ERROR_COLOR);
      } else {
        model.startTime.setInvalid(false);
        model.endTime.setInvalid(false);
        model.timeCircle.setColor(SUCCESS_COLOR);
        return true;
      }
    }
    return false;
  }

  private boolean daysValidation(Set<DayOfWeek> weekDays) {
    boolean check = weekDays != null && !weekDays.isEmpty();
    if (check) {
      model.daysCircle.setColor(SUCCESS_COLOR);
      return true;
    } else {
      return false;
    }
  }
}
