---
title: 'Classification (IS/AS)'
---

*Classification* operators create [properties](Properties.md) that determine whether an object belongs to the [class](Classes.md) specified. If the property argument does not belong to the class specified in the operator, the property returns **NULL***.*  Otherwise, the operator **IS** returns **TRUE**, and the operator **AS** returns the object passed as an argument.

### Language

To implement classification operators, [**IS** and **AS** operators](IS_AS_operators.md) are used. 

### Examples 

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=isas"/>
