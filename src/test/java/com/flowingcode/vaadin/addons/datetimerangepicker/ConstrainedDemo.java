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
@SuppressWarnings("serial")
@Route(value = "dtrp/constrained", layout = DateTimeRangePickerTabbedView.class)
@DemoSource
public class ConstrainedDemo extends VerticalLayout {

  public ConstrainedDemo() {
    setSizeFull();
    addClassNames(AlignItems.CENTER);

    // Component creation
    DateTimeRangePicker addon = new DateTimeRangePicker();
    addon.setMinDate(LocalDate.now());
    addon.setMaxDate(LocalDate.now().plusDays(15));
    addon.setWeekDays(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
    addon.setDaysReadOnly(true);
    addon.setTimeStep(Duration.ofMinutes(15));
    addon.setMinTime(LocalTime.of(13, 30));
    addon.setMaxTime(LocalTime.of(20, 0));

    add(addon);

  }

}
