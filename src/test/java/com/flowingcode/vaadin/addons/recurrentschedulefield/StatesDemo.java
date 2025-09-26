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
import com.flowingcode.vaadin.addons.recurrentschedulefield.api.DateTimeRange;
import com.flowingcode.vaadin.addons.recurrentschedulefield.ui.RecurrentScheduleField;
import com.flowingcode.vaadin.addons.recurrentschedulefield.ui.RecurrentScheduleFieldI18n;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@PageTitle("States")
@Route(value = "recurrent-schedule-field/states", layout = RecurrentScheduleFieldDemoView.class)
@DemoSource
public class StatesDemo extends VerticalLayout {

  private boolean indicator = true;
  private final Map<PickerSection, Boolean> readOnly = new HashMap<>();
  private final Map<PickerSection, Boolean> visible = new HashMap<>();

  public StatesDemo() {
    setSizeFull();
    addClassNames(AlignItems.CENTER);

    // Component creation using an initial value
    DateTimeRange dtr = new DateTimeRange(
        LocalDate.now(),
        LocalDate.now().plusDays(10),
        LocalTime.MIN,
        LocalTime.NOON
    );
    RecurrentScheduleField field = new RecurrentScheduleField(dtr);
    // Set the first or leftmost day
    field.setFirstWeekDay(DayOfWeek.THURSDAY);
    // Use i18n utility class for localization
    field.setI18n(new RecurrentScheduleFieldI18n()
        .setDatesTitle("Custom date title")
        .setTimeChipsText("AM", "PM", "AM + PM")
        .setTimesPlaceholder("Begin", "End")
        //... and more
    );

    VerticalLayout buttonLayout = new VerticalLayout();
    HorizontalLayout readOnlyLayout = new HorizontalLayout();
    HorizontalLayout visibleLayout = new HorizontalLayout();

    Button indicatorButton = new Button("Toggle indicator", ev -> {
      indicator = !indicator;
      field.setIndicatorVisible(indicator);
    });

    for(PickerSection section : PickerSection.values()) {
      readOnlyLayout.add(
          new Button("Toggle " + section.name().toLowerCase() + " read only",
              ev -> {
                boolean value = !readOnly.getOrDefault(section, false);
                readOnly.put(section, value);
                switch(section) {
                  case DATES -> field.setDatesReadOnly(value);
                  case DAYS -> field.setDaysReadOnly(value);
                  case TIMES -> field.setTimesReadOnly(value);
                }
              }
          )
      );
      visibleLayout.add(
          new Button("Toggle " + section.name().toLowerCase() + " visible",
              ev -> {
                boolean value = !visible.getOrDefault(section, true);
                visible.put(section, value);
                switch(section) {
                  case DATES -> field.setDatesVisible(value);
                  case DAYS -> field.setDaysVisible(value);
                  case TIMES -> field.setTimesVisible(value);
                }
              }
          )
      );
    }

    buttonLayout.setAlignItems(Alignment.CENTER);
    readOnlyLayout.setAlignItems(Alignment.CENTER);
    visibleLayout.setAlignItems(Alignment.CENTER);

    buttonLayout.add(indicatorButton, readOnlyLayout, visibleLayout);
    add(field, buttonLayout);
  }

  // Represents the different sections of the RecurrentScheduleField that can be toggled
  private enum PickerSection {DATES, DAYS, TIMES}
}
