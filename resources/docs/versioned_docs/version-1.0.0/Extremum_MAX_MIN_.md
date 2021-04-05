---
title: 'Extremum (MAX, MIN)'
---

The *extremum operator* creates a [property](Properties.md) which calculates the maximum or minimum between several specified properties. If the value of any of these properties is **NULL**, this property is ignored. If the values of all properties are **NULL**, the result value is also **NULL**.

### Language

The maximum property is created using the [**MAX** operator](MAX_operator.md), the minimum - using the [**MIN** operator](MIN_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=max"/>


<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=min"/>

**  
**
