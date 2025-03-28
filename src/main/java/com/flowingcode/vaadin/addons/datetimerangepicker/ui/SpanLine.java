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
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.LineHeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding.Bottom;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding.Horizontal;
import com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

// Horizontal line between pickers
class SpanLine extends Div {

  private final Paragraph text;
  private static final String EMPTY = "\u200E";

  public SpanLine() {

    addClassNames(
        Display.INLINE_FLEX,
        FlexDirection.COLUMN,
        AlignItems.CENTER,
        Bottom.XSMALL,
        Width.AUTO,
        Horizontal.SMALL,
        JustifyContent.END
    );
    setMinHeight("var(--lumo-size-m)");
    setMaxHeight("var(--lumo-size-m)");

    Div line = new Div();
    line.setMinWidth("4.5rem");
    line.setMaxHeight("1px");
    line.setMinHeight("1px");
    line.getElement().getStyle().setBackgroundColor("var(--lumo-contrast-10pct)");

    this.text = new Paragraph(EMPTY);
    text.addClassNames(
        TextAlignment.CENTER,
        FontSize.SMALL,
        Padding.NONE,
        Margin.NONE,
        FontWeight.SEMIBOLD,
        LineHeight.SMALL
    );
    text.getStyle().setColor("var(--lumo-secondary-text-color)");

    add(line, text);

  }

  public void setText(String text) {
    if (text.isEmpty()) {
      text = EMPTY;
    }
    this.text.setText(text);
  }

  public void setEmptyText() {
    this.text.setText(EMPTY);
  }
}
