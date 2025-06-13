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
import com.vaadin.flow.dom.Style.Visibility;
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
 * This component is styled using the "fc-dtrp" and Lumo classes.
 */
@CssImport("./styles/fc-date-time-range-picker.css")
class ChipGroup extends HorizontalLayout {

  private final Set<Chip> chips = new HashSet<>();
  private Chip checkedChip;
  private boolean readOnly = false;

  /**
   * Creates an empty {@code ChipGroup}.
   * Chips can be added later using {@link #addChip(Chip)}.
   */
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

  /**
   * Creates a {@code ChipGroup} containing the specified chips.
   *
   * @param chips the chips to add to this group
   */
  public ChipGroup(Chip... chips) {
    this();
    for (Chip chip : chips) {
      addChip(chip);
    }
  }

  public void addChip(Chip chip) {
    chips.add(chip);
    chip.setChipGroup(this);
    chip.setReadOnly(readOnly);
    add(chip);
  }

  // Makes sure one chip is selected at most by deselecting old chips
  private void onChipChange(Chip chip) {
    if(checkedChip != null && chip.isChecked() && !checkedChip.equals(chip)) {
      checkedChip.setChecked(false);
    }
    checkedChip = chip.isChecked() ? chip : null;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
    chips.forEach(c -> c.setReadOnly(readOnly));
  }

  public void setVisible(boolean visible) {
    getStyle().set("visibility", visible ? "visible" : "hidden");
  }

  /**
   * A checkable chip component for use within a {@code ChipGroup}.
   * Supports read-only state, and displays a check icon when checked.
   */
  static class Chip extends HorizontalLayout {

    private boolean checked = false;
    private boolean readOnly = false;

    private final Icon checkIcon;
    private final Span textField;

    // ChipGroup this chip is part of
    private ChipGroup chipGroup;
    // Listener added by calling onPress()
    private SerializableConsumer<Boolean> listener;

    /**
     * Creates a new {@code Chip} with no text.
     * The chip is initially unchecked and not read-only.
     */
    public Chip() {
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

      textField = new Span();
      textField.addClassNames(
          FontSize.SMALL,
          FontWeight.SEMIBOLD,
          Margin.NONE,
          Padding.NONE,
          LineHeight.SMALL
      );

      addClickListener(ev -> {
        if (!readOnly) {
          toggle(!this.checked);
          if (listener != null) {
            listener.accept(this.checked);
          }
          if (chipGroup != null) {
            chipGroup.onChipChange(this);
          }
        }
      });

      add(textField);
    }

    // Switches UI to given state
    private void toggle(Boolean isChecked) {
      if (isChecked) {
        removeClassName("fc-dtrp-unselected");
        addClassName("fc-dtrp-selected");
        addComponentAsFirst(checkIcon);
      } else {
        removeClassName("fc-dtrp-selected");
        addClassName("fc-dtrp-unselected");
        remove(checkIcon);
      }
      this.checked = isChecked;
    }

    private void setChipGroup(ChipGroup chipGroup) {
      this.chipGroup = chipGroup;
    }

    public void onPress(SerializableConsumer<Boolean> onClick) {
      this.listener = onClick;
    }

    public void setChecked(boolean isChecked) {
      toggle(isChecked);
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
