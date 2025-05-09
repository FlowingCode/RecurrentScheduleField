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

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Border;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderColor;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexWrap;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.LineHeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding.Horizontal;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding.Left;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding.Vertical;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import java.util.HashSet;
import java.util.Set;

/**
 * A UI component representing a group of single-selection chips.
 * The group can be set to read-only mode, disabling user interaction.
 * This component is styled using the "fc-dtrp" CSS classes.
 */
@CssImport("./styles/styles.css")
class ChipGroup extends HorizontalLayout {

  private final Set<Chip> chips = new HashSet<>();
  private Chip checkedChip;
  private boolean readOnly = false;

  public ChipGroup() {
    addClassNames(
        AlignItems.CENTER,
        JustifyContent.END,
        Gap.SMALL,
        FlexWrap.WRAP,
        Left.SMALL,
        Vertical.SMALL
    );
  }

  public ChipGroup(Chip... chips) {
    this();
    for (Chip chip : chips) {
      addChip(chip);
    }
  }

  public Chip addChip(Chip chip) {
    chips.add(chip);
    chip.setParent(this);
    chip.setReadOnly(readOnly);
    add(chip);
    return chip;
  }

  private void onChipChange(Chip chip) {
    if (checkedChip != null) {
      if (!checkedChip.equals(chip)) {
        checkedChip.setChecked(false);
        checkedChip = chip;
      } else {
        checkedChip = null;
      }
    } else {
      checkedChip = chip;
    }
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
    chips.forEach(c -> c.setReadOnly(readOnly));
  }

  static class Chip extends HorizontalLayout {

    private final Icon checkIcon;
    private boolean checked = false;
    private final Span textField;
    private boolean readOnly = false;
    private ChipGroup parent;
    private SerializableConsumer<Boolean> callback;

    public Chip(String text) {
      addClassNames(
          Horizontal.MEDIUM,
          Vertical.XSMALL,
          "fc-dtrp-hoverable",
          AlignItems.CENTER,
          BorderRadius.LARGE,
          Border.ALL,
          BorderColor.CONTRAST_10,
          Display.INLINE_FLEX,
          Gap.SMALL
      );
      checkIcon = VaadinIcon.CHECK.create();
      checkIcon.setSize("14px");

      textField = new Span(text);
      textField.addClassNames(
          FontSize.SMALL,
          FontWeight.SEMIBOLD,
          Margin.NONE,
          Padding.NONE,
          LineHeight.SMALL
      );
      addClickListener(ev -> {
        if (!readOnly) {
          this.checked = !this.checked;
          toggle();
          if (callback != null) {
            callback.accept(this.checked);
          }
          if (parent != null) {
            parent.onChipChange(this);
          }
        }
      });

      add(textField);
    }

    private void toggle() {
      if (checked) {
        removeClassName("fc-dtrp-unselected");
        addClassName("fc-dtrp-selected");
        addComponentAsFirst(checkIcon);
      } else {
        removeClassName("fc-dtrp-selected");
        addClassName("fc-dtrp-unselected");
        remove(checkIcon);
      }
    }

    private void setParent(ChipGroup parent) {
      this.parent = parent;
    }

    public void onPress(SerializableConsumer<Boolean> onClick) {
      this.callback = onClick;
    }

    public void setChecked(boolean checked) {
      this.checked = checked;
      toggle();
    }

    public boolean isChecked() {
      return checked;
    }

    public String getText() {
      return textField.getText();
    }

    public void setText(String text) {
      textField.setText(text);
    }

    public void setReadOnly(boolean readOnly) {
      if(this.readOnly == readOnly) return;
      this.readOnly = readOnly;

      if (readOnly) {
        removeClassName("fc-dtrp-hoverable");
        removeClassName(TextColor.BODY);
        addClassName(TextColor.SECONDARY);
        addClassName("fc-dtrp-readonly");
      } else {
        removeClassName("fc-dtrp-readonly");
        removeClassName(TextColor.SECONDARY);
        addClassName("fc-dtrp-hoverable");
        addClassName(TextColor.BODY);
      }
    }

  }

}
