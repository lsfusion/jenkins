---
title: 'Form structure: Overview'
sidebar_label: Overview
---

The [form](Forms.md) structure is a complex of *objects*, [properties](Properties.md), [actions](Actions.md) and relations between them.

### Objects {#objects}

When creating a form, first you must define which objects it will display. For each form object you need to specify its [class](Classes.md).

It is possible to combine objects into *object groups*. In this case, the table will show a "Cartesian product" of these objects (i.e., for two objects - all pairs, three objects - triples, etc.). 

In accordance with the order of adding object groups to the form, an ordered *list* of object groups is formed. Accordingly, the group with the maximum number for a certain set of object groups shall be called the *last* group for this set (i.e., the latest). The *last* group for a set of objects is determined similarly: first, a set of object groups into which these objects are included is built, then the last group is determined for the obtained set of object groups.

Each object on the form has a *current value* at any time. It changes either as a result of [corresponding user actions](Interactive_view.md#objects) in [interactive](Interactive_view.md) view or "virtually" while reading data in [static](Static_view.md) view.

### Properties and actions {#properties}

After defining objects, you can add properties and actions to the form, passing these objects to them as arguments.


:::note
Adding actions is relevant only for [interactive](Interactive_view.md) view. In [static](Static_view.md) view added actions are ignored.
:::


:::note
The behavior of properties and actions in the context of their display on the form is absolutely identical, therefore, in the rest of this section, we will use only the term property (the behavior for actions is completely similar).
:::

  
*Display group*

Each property is displayed exactly in one of the object groups on the form (this group shall be called a *display group* of this property). By default, the display group is the last group for the set of objects which this property receives as an input. If necessary, the developer can specify the display group explicitly (with certain [constraints](Structured_view.md#drawgroup-broken) when used in a structured view)

*Groups-in-columns*

By default, a property is displayed in its display group exactly once. In this case, the values of objects which are not in the display group of this property (these objects shall be called *upper*) are used as their current values. However, it is also possible to display one property multiple times so that all object collections are used as the values of certain upper objects (not their current values). With this display of the property, a kind of "matrix" is formed - upper objects x objects of the display group. Thus, to create such a matrix, when adding a property to the form you must specify which *upper* objects (specifically, object groups) must be used to create columns (these object groups shall be called *groups-in-columns*).

When determining a [display group](#drawgroup-broken), properties of the group-to-columns are ignored.

*Property groups*

Properties on the form can be combined into [groups](Groups_of_properties_and_actions.md) which, in turn, are used in the interactive ([default design](Form_design.md#defaultDesign)) and [hierarchical](Structured_view.md#hierarchy) form views. By default, a property is included in a group globally (i.e., this inclusion is defined for a property for all forms at once), however, this inclusion can be redefined for particular forms.

*Default settings*

Properties on the form have a large number of display settings in various [views](Form_views.md), most of which can be set not only directly for the property on the form, but also for the property itself (when creating it). These settings will be the default settings, i.e., if the setting is not explicitly set for a specific property on the form, then the setting of the property itself is used. In addition, these default settings are "inherited" when using [composition](Composition_JOIN_.md), [selection](Selection_CASE_IF_MULTI_OVERRIDE_EXCLUSIVE_.md) and [previous value](Previous_value_PREV_.md) operators (i.e., if property f(a) has the default width of 10, then the property g(a) = f(a) IF h(a) will also have a width of 10 by default).

### Filters {#filters}

For each form, the developer can create *filters* which will limit the list of object collections available for viewing/selection on the form.

To define a filter, you must specify a property that will be used as a filter condition. The filter will be applied to the table of the object group that is the last for the set of objects which this property takes as input (i.e., similar to the definition of the property *display group*). In this case, only those object collections (rows) for which property values are not **NULL** will be shown.

### Orders {#sort}

By default, in all object group views, object collections are displayed in a certain non-deterministic order, which is determined by the specific implementation of the platform (most often, internal identifiers are used to determine the order). If necessary, the developer can define this order explicitly by specifying a list of properties on the form that will be used as orders. At the same time, for each property in this list, you can specify whether the order should be ascending or descending (by default, the ascending option is used).

**NULL** value is always considered to be the smallest value. 

### Language

To create a new form and define its structure, the [**FORM** instruction](FORM_instruction.md) is used.

### Examples

 

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=FormSample&block=form"/>
