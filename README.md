[![Published on Vaadin Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/date-time-range-picker-add-on)
[![Stars on vaadin.com/directory](https://img.shields.io/vaadin-directory/star/date-time-range-picker-add-on.svg)](https://vaadin.com/directory/component/date-time-range-picker-add-on)
[![Build Status](https://jenkins.flowingcode.com/job/date-time-range-picker-addon/badge/icon)](https://jenkins.flowingcode.com/job/date-time-range-picker-addon)
[![Maven Central](https://img.shields.io/maven-central/v/com.flowingcode.vaadin.addons/date-time-range-picker-addon)](https://mvnrepository.com/artifact/com.flowingcode.vaadin.addons/date-time-range-picker-addon)
[![Javadoc](https://img.shields.io/badge/javadoc-00b4f0)](https://javadoc.flowingcode.com/artifact/com.flowingcode.vaadin.addons/date-time-range-picker-addon)

# DateTimeRangePicker Add-on for Vaadin 24

A component to create [Time Intervals](https://en.wikipedia.org/wiki/ISO_8601#Time_intervals) (_a time period defined by start and end points_) constrained by a time frame.

You use the UI to define time and date ranges in which the time intervals should be created, and then call the API to operate them.

As an example, you could set it to create an interval **every weekend** from **8:30 to 12:30 AM** between the
**1st and 15th of May 2025**.<br>Then, you can make the following queries:

1. How many intervals will be created? (4)
2. Starting from today, when will the next interval occur?
3. Is the 7th of May at 9:15 AM included in any interval? (It's not)
   - How about the 5th of May at 9:00 AM? (Yes)
4. How many intervals will have passed after the 9th of May? (2)
5. How many intervals will remain after the 5th of May at 12:45? (3)

... and more

## Features
- Customizable selection of date, time and days.
- API to create and query time intervals.

## Online demo

[Online demo here](http://addonsv24.flowingcode.com/date-time-range-picker)

## Download release

[Available in Vaadin Directory](https://vaadin.com/directory/component/date-time-range-picker-add-on)

### Maven install

Add the following dependencies in your pom.xml file:

```xml
<dependency>
   <groupId>com.flowingcode.vaadin.addons</groupId>
   <artifactId>date-time-range-picker-addon</artifactId>
   <version>X.Y.Z</version>
</dependency>
```
<!-- the above dependency should be updated with latest released version information -->

Release versions are available from Maven Central repository. For SNAPSHOT versions see [here](https://maven.flowingcode.com/snapshots/).

## Building and running demo

- git clone repository
- mvn clean install jetty:run

To see the demo, navigate to http://localhost:8080/

## Release notes

See [here](https://github.com/FlowingCode/date-time-range-picker-addon/releases)

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. 

As first step, please refer to our [Development Conventions](https://github.com/FlowingCode/DevelopmentConventions) page to find information about Conventional Commits & Code Style requirements.

Then, follow these steps for creating a contribution:

- Fork this project.
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- For commit message, use [Conventional Commits](https://github.com/FlowingCode/DevelopmentConventions/blob/main/conventional-commits.md) to describe your change.
- Send a pull request for the original project.
- Comment on the original issue that you have implemented a fix for it.

## License & Author

This add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

DateTimeRangePicker is written by Flowing Code S.A.

# Developer Guide

## Getting started

```
    DateTimeRangePicker addon = new DateTimeRangePicker();    (1)
    addon.setMinDate(LocalDate.now());                        (2)
    addon.setMaxDate(LocalDate.now().plusDays(15));           (2)
    addon.setWeekDays(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);    (3)
```
- (1) Instantiation. 
- (2) Date range selection will be constraint between **today** and **fifteen** days later.
- (3) Component will have **Monday** and **Friday** selected by default.

## Binding

The API is exposed through the **DateTimeRange** class.

```
    DateTimeRangePicker addon = new DateTimeRangePicker();                        
    Binder<Pojo> binder = new Binder<>(Pojo.class);                                 (1)
    binder.forField(addon).bind(Pojo::getDateTimeRange, Pojo::setDateTimeRange);    (2) 
    binder.setBean(pojo);                                                           (3)
```
 - (1) The **Pojo** class is bound using its getter and setter methods (2).
 - (2) The **DateTimeRangePicker** is bound. Its [value](https://vaadin.com/api/platform/current/com/vaadin/flow/component/HasValue.html) can be operated now.
 - (3) The **DateTimeRange** instance is saved in the **Pojo** class or returned from it.


You can operate time intervals with the **DateTimeRange** class, which acts as a holder.

```
    TimeInterval interval = pojo.getDateTimeRange().getNextInterval();          (1)
    boolean includes = pojo.getDateTimeRange().includes(LocalDateTime.now());   (2)
```
 - (1) "Starting from today, when will the next interval occur?"
 - (2) "Is today included in any interval?"

You can also use the **TimeInterval** instance directly.

```
    boolean includes = interval != null && interval.includes(LocalDateTime.now());
```

## I18n support

Customize a ``DateTimeRangePickerI18n`` instance and pass it to the component (1).

```
    DateTimeRangePicker addon = new DateTimeRangePicker();
    addon.setI18n(new DateTimeRangePickerI18n()                 (1)
        .setDatesTitle("Your custom title")
        .setTimeChipsText("AM", "PM", "AM + PM")
        .setTimesPlaceholder("Start time", "End time")
    );
```


## Special configuration when using Spring

By default, Vaadin Flow only includes ```com/vaadin/flow/component``` to be always scanned for UI components and views. For this reason, the add-on might need to be allowed in order to display correctly. 

To do so, just add ```com.flowingcode``` to the ```vaadin.allowed-packages``` property in ```src/main/resources/application.properties```, like:

```vaadin.allowed-packages = com.vaadin,org.vaadin,dev.hilla,com.flowingcode```
 
More information on Spring scanning configuration [here](https://vaadin.com/docs/latest/integrations/spring/configuration/#configure-the-scanning-of-packages).
