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
package com.flowingcode.vaadin.addons.recurrentschedulefield;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.flowingcode.vaadin.addons.recurrentschedulefield.ui.RecurrentScheduleField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@PageTitle("Constrained")
@Route(value = "recurrent-schedule-field/constrained", layout = RecurrentScheduleFieldDemoView.class)
@DemoSource
public class ConstrainedDemo extends VerticalLayout {

  public ConstrainedDemo() {
    setSizeFull();

    // Basic component creation
    RecurrentScheduleField field = new RecurrentScheduleField();
    // Min selectable date
    field.setMinDate(LocalDate.now());
    // Max selectable date
    field.setMaxDate(LocalDate.now().plusDays(15));
    // Select days of the week programmatically
    field.setWeekDays(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
    // Days will be unmodifiable
    field.setDaysReadOnly(true);
    // Interval between selectable time items
    // Only times at 15-minute steps will be shown as options
    field.setTimeStep(Duration.ofMinutes(15));
    // Min selectable time
    field.setMinTime(LocalTime.of(13, 30));
    // Max selectable time
    // Both options will constraint the chips as well
    field.setMaxTime(LocalTime.of(20, 0));

    add(field);
  }
}
