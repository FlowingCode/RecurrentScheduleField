package com.flowingcode.vaadin.addons.datetimerangepicker;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.flowingcode.vaadin.addons.datetimerangepicker.api.DateTimeRange;
import com.flowingcode.vaadin.addons.datetimerangepicker.api.TimeInterval;
import com.flowingcode.vaadin.addons.datetimerangepicker.ui.DateTimeRangePicker;
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
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Horizontal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Binding")
@SuppressWarnings("serial")
@Route(value = "dtrp/binder", layout = DateTimeRangePickerTabbedView.class)
@DemoSource
public class BinderDemo extends VerticalLayout {

  private final Button dateButton = new Button("Show dates range");
  private final Button daysButton = new Button("Show days");
  private final Button timeButton = new Button("Show times range");
  private final Button interButton = new Button("Update intervals");
  private final Grid<TimeInterval> grid = new Grid<>(TimeInterval.class, false);
  private final List<TimeInterval> intervals = new ArrayList<>();

  /*
    DateTimeRangePicker::getValue returns a DateTimeRange instance.
    You operate TimeInterval instances using that class.
    TimeInterval represents a time interval (ISO 8601), defined by start and end points.
  */
  public BinderDemo() {
    setSizeFull();
    addClassNames(AlignItems.CENTER);

    // Component creation
    DateTimeRangePicker addon = new DateTimeRangePicker();
    // Distance between start and end dates is at most 30 days
    addon.setMaxDaysSpan(30);

    // An object with getter/setter for DateTimeRange
    Pojo pojo = new Pojo();
    Binder<Pojo> binder = new Binder<>(Pojo.class);
    binder.forField(addon)
        .bind(Pojo::getDateTimeRange, Pojo::setDateTimeRange);
    binder.setBean(pojo);

    binder.addStatusChangeListener(ev -> {
      boolean isValid = binder.isValid();
      dateButton.setEnabled(isValid);
      daysButton.setEnabled(isValid);
      timeButton.setEnabled(isValid);
      interButton.setEnabled(isValid);

    });

    Grid.Column<TimeInterval> firstCol = grid.addColumn(i -> i.getStartDate().getDayOfWeek()).setHeader("Week day")
        .setSortable(true);
    grid.addColumn(TimeInterval::getStartDate).setHeader("Start")
        .setSortable(true);
    grid.addColumn(TimeInterval::getEndDate).setHeader("End").setSortable(true);
    // You can use this function to get a Duration formatted as hh:mm:ss
    grid.addColumn(i -> DateTimeRangePicker.formatDuration(i.getDuration())).setHeader("Duration");

    GridListDataView<TimeInterval> dataView = grid.setItems(intervals);
    dataView.addItemCountChangeListener(c -> firstCol.setFooter("Total: " + c.getItemCount()));
    grid.setWidth("75%");
    grid.addClassName(Horizontal.AUTO);

    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.setAlignItems(Alignment.CENTER);
    buttonLayout.add(dateButton, daysButton, timeButton);

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

    interButton.addClickListener(ev -> {
      intervals.clear();
      intervals.addAll(pojo.getDateTimeRange().getIntervals());
      dataView.refreshAll();
    });
    interButton.setEnabled(false);

    add(addon, buttonLayout, interButton, grid);

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


}
