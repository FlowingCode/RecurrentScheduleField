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
import com.flowingcode.vaadin.addons.recurrentschedulefield.api.TimeInterval;
import com.flowingcode.vaadin.addons.recurrentschedulefield.ui.RecurrentScheduleField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Binding")
@Route(value = "recurrent-schedule-field/binder", layout = RecurrentScheduleFieldDemoView.class)
@DemoSource
public class BinderDemo extends VerticalLayout {

  private final Button dateButton = new Button("Show dates range");
  private final Button daysButton = new Button("Show days");
  private final Button timeButton = new Button("Show times range");
  private final Button intervalButton = new Button("Update intervals");
  private final List<TimeInterval> intervals = new ArrayList<>();

  private Grid<TimeInterval> grid = null;
  private GridListDataView<TimeInterval> dataView = null;

  /*
    RecurrentScheduleField will return a DateTimeRange instance when valid.
    Then you may use that object to operate TimeInterval instances.
    TimeInterval represents a time interval (ISO 8601), defined by start and end points.
  */
  public BinderDemo() {
    setSizeFull();

    // Basic component creation
    RecurrentScheduleField field = new RecurrentScheduleField();
    // Distance between start and end dates is at most 30 days
    field.setMaxDaysSpan(30);

    // An object with simple getter/setter methods
    Pojo pojo = new Pojo();

    // Bind the object with the component
    Binder<Pojo> binder = new Binder<>(Pojo.class);
    binder.forField(field)
        .bind(Pojo::getDateTimeRange, Pojo::setDateTimeRange);
    binder.setBean(pojo);

    // The component will be valid when dates, days and times are set properly,
    // and a DateTimeRange instance will be available in 'pojo' at this point
    binder.addStatusChangeListener(ev -> {
      boolean isValid = binder.isValid();
      dateButton.setEnabled(isValid);
      daysButton.setEnabled(isValid);
      timeButton.setEnabled(isValid);
      intervalButton.setEnabled(isValid);
    });

    // Rest of the code is grid and buttons configuration...
    grid = new Grid<>(TimeInterval.class, false);
    dataView = grid.setItems(intervals);

    intervalButton.addClickListener(ev -> {
      intervals.clear();
      // Fetch time intervals
      intervals.addAll(pojo.getDateTimeRange().getIntervals());
      dataView.refreshAll();
    });
    intervalButton.setEnabled(false);

    dateButton.addClickListener(ev -> {
      DateTimeRange result = pojo.getDateTimeRange();
      LocalDate start = result.getStartDate();
      LocalDate end = result.getEndDate();
      Notification.show(
          String.format("From %s %s to: %s %s (exclusive)",
              start.getDayOfWeek(),
              start,
              end.getDayOfWeek(),
              end
          ),
          5000, Position.BOTTOM_CENTER
      );
    });
    dateButton.setEnabled(false);

    daysButton.addClickListener(ev -> {
      DateTimeRange result = pojo.getDateTimeRange();
      Notification.show(result.getWeekDays().toString(),
          5000, Position.BOTTOM_CENTER
      );
    });
    daysButton.setEnabled(false);

    timeButton.addClickListener(ev -> {
      DateTimeRange result = pojo.getDateTimeRange();
      LocalTime start = result.getStartTime();
      LocalTime end = result.getEndTime();
      Notification.show(
          String.format("From %s to: %s (exclusive)",
              start,
              end
          ),
          5000, Position.BOTTOM_CENTER
      );
    });
    timeButton.setEnabled(false);

    Grid.Column<TimeInterval> firstCol = grid.addColumn(i -> i.getStartDate().getDayOfWeek()).setHeader("Week day")
        .setSortable(true);
    grid.addColumn(TimeInterval::getStartDate).setHeader("Start")
        .setSortable(true);
    grid.addColumn(TimeInterval::getEndDate).setHeader("End").setSortable(true);
    grid.addColumn(i -> formatDuration(i.getDuration())).setHeader("Duration");

    dataView.addItemCountChangeListener(c -> firstCol.setFooter("Total: " + c.getItemCount()));
    grid.setWidth("75%");

    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.setAlignItems(Alignment.CENTER);
    buttonLayout.add(dateButton, daysButton, timeButton);

    add(field, buttonLayout, intervalButton, grid);
  }

  private static class Pojo {

    private DateTimeRange dateTimeRange;

    public DateTimeRange getDateTimeRange() {
      return dateTimeRange;
    }

    public void setDateTimeRange(DateTimeRange dateTimeRange) {
      this.dateTimeRange = dateTimeRange;
    }
  }

  // Show duration as HH:mm:ss
  private static String formatDuration(Duration duration) {
    return String.format("%02d:%02d:%02d",
        duration.toHoursPart(),
        duration.toMinutesPart(),
        duration.toSecondsPart()
    );
  }
}
