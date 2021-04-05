---
title: 'Custom formula (FORMULA)'
---

The *custom formula* operator allows you to create a [property](Properties.md) that calculates defined formula in SQL. You can specify different implementations of the formula for different SQL servers. The formula is defined as a string, within which the special character $ and the number of this parameter (starting from 1) are used to access the parameter. Accordingly, the number of parameters of the result property will be equal to the greatest of the numbers of the parameters used. 

Using this operator is recommended only if the task cannot be accomplished using other operators, and only if it is known for certain which specific SQL servers can be used, or if the syntax constructs used comply with one of the latest SQL standards.

### Determining the result class

By default, the result class of the custom operator is a [common ancestor](Built-in_classes.md#commonparentclass) of all its operands. If necessary, the developer can specify this class explicitly.

### Language

To declare a property using a custom formula, use the [**FORMULA** operator](FORMULA_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=formula"/>
