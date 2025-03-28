package com.flowingcode.vaadin.addons.datetimerangepicker;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.flowingcode.vaadin.addons.datetimerangepicker.ui.DateTimeRangePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;

@PageTitle("Basic")
@SuppressWarnings("serial")
@Route(value = "dtrp/basic", layout = DateTimeRangePickerTabbedView.class)
@DemoSource
public class ComponentDemo extends VerticalLayout {

  public ComponentDemo() {
    setSizeFull();
    addClassNames(AlignItems.CENTER);

    // Component creation
    DateTimeRangePicker addon = new DateTimeRangePicker();
    add(addon);
  }
}
