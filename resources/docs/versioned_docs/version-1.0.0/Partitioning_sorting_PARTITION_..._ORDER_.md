---
title: 'Partitioning / sorting (PARTITION ... ORDER)'
---

The *partition/order* operator creates a [property](Properties.md) that partitions all objects collections in the system into *groups*, and using the specified *order* calculates an [aggregate function](Set_operations.md#func) for each objects collection. Accordingly, the set on which this aggregating function is calculated is determined as following: all object collections of the group of this object collection, and the order of which is less than or equal to the order of this object collection. 

Groups in this operator are defined as a set of properties (*groupings*), and the order is defined as a list of properties and a marker of increasing or decreasing. If the aggregation function is not [commutative](Set_operations.md#commutative-broken), the order must be uniquely determined. 

Note that the partition/order operator is very similar to the [grouping operator](Grouping_GROUP_.md), but unlike the latter, it computes a result not for grouping values, but for the object collections for which calculation is taking place.

### Language

To declare a property that implements partition/order, use the [**PARTITION** operator](PARTITION_operator.md). 

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=partition"/>
