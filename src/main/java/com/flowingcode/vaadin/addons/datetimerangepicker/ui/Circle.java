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

import com.vaadin.flow.component.html.Div;

/**
 * A simple UI component representing a circular indicator.
 * The circle's background color can be customized using the {@link #setColor(String)} method.
 * This component is styled with the "fc-dtrp-circle" CSS class.
 */
class Circle extends Div {

  private final Div circle;

  public Circle() {
    circle = new Div();

    addClassName("fc-dtrp-circle");

    add(circle);
  }

  public void setColor(String background) {
    circle.getStyle().setBackgroundColor(background);
  }

}