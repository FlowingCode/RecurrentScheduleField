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
package com.flowingcode.vaadin.addons.recurrentschedulefield.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;

/**
 * A simple UI component representing a horizontal line with an optional label.
 * The line is styled with the "fc-rsf-linespan" CSS class.
 * The label text can be set or cleared using the {@link #setText(String)} and {@link #clearText()} methods.
 */
class SpanLine extends Div {

  private final Paragraph label = new Paragraph();

  /**
   * Creates a new horizontal line with an optional label.
   */
  public SpanLine() {

    addClassName("fc-rsf-linespan");

    Div line = new Div();

    add(line, label);
  }

  public void setText(String text) {
    this.label.setText(text);
  }

  public void clearText() {
    this.label.setText(null);
  }
}
