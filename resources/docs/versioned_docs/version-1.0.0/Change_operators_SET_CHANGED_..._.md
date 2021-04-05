---
title: 'Change operators (SET, CHANGED, ...)'
---

*Change operators* determine whether some types of changes have occurred for a certain property in the current session. All these operators are derived from the [previous value operator (PREV)](Previous_value_PREV_.md), however, it is recommended to use them to improve readability and performance. The following table shows the supported types of changes and their description:

|Operator|Value|Description|
|---|---|---|
|<strong>SET</strong>|f AND NOT PREV(f)|Value is set|
|<strong>DROPPED</strong>|NOT f AND PREV(f)|Value is reset|
|<strong>CHANGED</strong>|(f OR PREV(f)) AND NOT f==PREV(f)|Value is changed|
|<strong>SETCHANGED</strong>|<p>f AND NOT f==PREV(f)</p><br/><p>or</p><br/><p>CHANGED(f) AND NOT DROPPED(f)</p>|Value is changed to non-<strong>NULL</strong>|
|<strong>DROPCHANGED</strong>|CHANGED(f) AND NOT SET(f)|Value is either reset or changed from one non-<strong>NULL</strong> to another non-<strong>NULL</strong>|
|<strong>SETDROPPED</strong>|SET(f) OR DROPPED(f)|Value is either reset or changed from <strong>NULL</strong> to non-<strong>NULL</strong>|

:::caution
These operators are computed differently inside the [event](Events.md#change) handler: in this case, they return changes from the point of the previous occurrence of this event, or rather, from the point at which all its handlers are completed.
:::

### Language

To declare a property using change operators, the following [syntax constructs](Change_operators.md) are used. 

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=changed"/>
