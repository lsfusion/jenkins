---
title: 'Aggregations'
---

Aggregation is the creation of a unique (*aggregate*) of the object corresponding to each non-**NULL** value of some *aggregated* property. Each such object is assumed to have properties that map this object to each aggregated property parameter, and, conversely, a property that maps the aggregated property parameters to this object. 

The aggregated object and each aggregated property parameter must belong to a specified [class](Classes.md).

The aggregation mechanism is implemented using two [consequences](Simple_constraints.md) with automatic resolution and an [aggregation](Grouping_GROUP_.md) operator. With the help of the aggregation operator, the first consequence creates an object when the aggregated property becomes non-**NULL**, and writes the necessary values to all its properties. The second consequence deletes the object when the aggregated property becomes **NULL**.

### Language

To create aggregations, use the [operator**AGGR**](AGGR_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=AggregationSample&block=aggr"/>
