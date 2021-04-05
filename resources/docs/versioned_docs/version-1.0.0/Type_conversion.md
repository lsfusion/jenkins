---
title: 'Type conversion'
---

The *type conversion* operator creates a [property](Properties.md) that converts an object of one [built-in class](Built-in_classes.md) to an object of another built-in class. If type conversion is not possible, the property value will be **NULL**.

### String and file types

String types can be converted to human-readable file types (**CSVFILE**, **XMLFILE**, **JSONFILE**, **HTMLFILE**, etc.), and vice versa - human-readable file types can be converted to string types.


:::note
Converting dynamic-type files (**FILE**) to strings and vice versa is prohibited in the current implementation, but if necessary this can be done via an intermediate human-readable type - for example, by first converting to **CSVFILE**, and only then to **FILE** (the resulting file [will have the extension](Built-in_classes.md#csv-broken) CSV)
:::

### Language

To implement conversion, the [type conversion operator](Type_conversion_operator.md) is used.

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=explicitcast"/>
