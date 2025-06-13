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
package com.flowingcode.vaadin.addons.datetimerangepicker;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.flowingcode.vaadin.addons.datetimerangepicker.ui.DateTimeRangePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@PageTitle("Constrained")
@Route(value = "datetimerange/constrained", layout = DateTimeRangePickerTabbedView.class)
@DemoSource
public class ConstrainedDemo extends VerticalLayout {

  public ConstrainedDemo() {
    setSizeFull();
    addClassNames(AlignItems.CENTER);

    // Basic component creation
    DateTimeRangePicker addon = new DateTimeRangePicker();
    // Min selectable date
    addon.setMinDate(LocalDate.now());
    // Max selectable date
    addon.setMaxDate(LocalDate.now().plusDays(15));
    // Select days of the week programmatically
    addon.setWeekDays(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
    // Days will be unmodifiable
    addon.setDaysReadOnly(true);
    // Interval between selectable time items
    // Only times at 15-minute steps will be shown as options
    addon.setTimeStep(Duration.ofMinutes(15));
    // Min selectable time
    addon.setMinTime(LocalTime.of(13, 30));
    // Max selectable time
    // Both options will constraint the chips as well
    addon.setMaxTime(LocalTime.of(20, 0));

    add(addon);
  }
}
