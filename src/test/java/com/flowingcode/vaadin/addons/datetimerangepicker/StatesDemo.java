package com.flowingcode.vaadin.addons.datetimerangepicker;

import com.flowingcode.vaadin.addons.datetimerangepicker.ui.DateTimeRangePickerI18n;
import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.flowingcode.vaadin.addons.datetimerangepicker.ui.DateTimeRangePicker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import java.time.DayOfWeek;

@PageTitle("States")
@SuppressWarnings("serial")
@Route(value = "dtrp/states", layout = DateTimeRangePickerTabbedView.class)
@DemoSource
public class StatesDemo extends VerticalLayout {

  private boolean indicator = true;
  private final boolean[] readOnly = {false, false, false};
  private final boolean[] visible = {true, true, true};

  public StatesDemo() {
    setSizeFull();
    addClassNames(AlignItems.CENTER);

    // Component creation
    DateTimeRangePicker addon = new DateTimeRangePicker();
    // Set the first or leftmost day
    addon.setFirstWeekDay(DayOfWeek.THURSDAY);
    addon.setI18n(new DateTimeRangePickerI18n()
        .setDatesTitle("Custom date title")
        .setTimeChipsText("AM", "PM", "AM + PM")
        .setTimesPlaceholder("Begin", "End")
    );

    VerticalLayout buttonLayout = new VerticalLayout();
    buttonLayout.setAlignItems(Alignment.CENTER);
    HorizontalLayout readOnlyLayout = new HorizontalLayout();
    readOnlyLayout.setAlignItems(Alignment.CENTER);
    HorizontalLayout visibleLayout = new HorizontalLayout();
    visibleLayout.setAlignItems(Alignment.CENTER);

    Button indicatorButton = new Button("Toggle indicator", ev ->
    {
      indicator = !indicator;
      addon.setIndicatorVisible(indicator);
    });

    for (int i = 0; i < 3; i++) {
      final int index = i;
      readOnlyLayout.add(
          new Button("Toggle " + (i == 0 ? "dates" : i == 1 ? "days" : "times") + " read only",
              ev -> {
                readOnly[index] = !readOnly[index];
                if (index == 0) {
                  addon.setDatesReadOnly(readOnly[index]);
                } else if (index == 1) {
                  addon.setDaysReadOnly(readOnly[index]);
                } else {
                  addon.setTimesReadOnly(readOnly[index]);
                }
              }
          )
      );
      visibleLayout.add(
          new Button("Toggle " + (i == 0 ? "dates" : i == 1 ? "days" : "times") + " visible",
              ev -> {
                visible[index] = !visible[index];
                if (index == 0) {
                  addon.setDatesVisible(visible[index]);
                } else if (index == 1) {
                  addon.setDaysVisible(visible[index]);
                } else {
                  addon.setTimesVisible(visible[index]);
                }
              }
          )
      );
    }

    buttonLayout.add(indicatorButton, readOnlyLayout, visibleLayout);

    add(addon, buttonLayout);

  }

}
