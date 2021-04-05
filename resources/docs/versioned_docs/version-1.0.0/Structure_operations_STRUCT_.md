---
title: 'Structure operations (STRUCT, \[\])'
---

The term *structure* is used in the platform to refer to the [classes](Classes.md) which objects consist of a collection of other objects in a fixed order. The platform supports two operators for working with structures:

|<div><br/><div><br/><div><br/>Operator<br/></div><br/></div><br/></div>|<div><br/><div><br/><div><br/>Name<br/></div><br/></div><br/></div>|<div><br/><div><br/><div><br/>Description<br/></div><br/></div><br/></div>|<div><br/><div><br/><div><br/>Example<br/></div><br/></div><br/></div>|<div><br/><div><br/><div><br/>Result<br/></div><br/></div><br/></div>|
|---|---|---|---|---|
|STRUCT|Creation|Creates a [property](Properties.md) that takes a list of operands and returns a structure consisting of the objects passed|STRUCT('a', 1)|STRUCT('a', 1)|
|[ ]|Access|<p>Creates a property that takes an operand with a fixed integer and returns an object of the structure specified in the first operand with a number equal to the specified integer</p>|STRUCT('a',1)[2]|1 |

Structures support comparison operations which are executed sequentially for each object included in the structure. 

:::caution
To better understand how this works, it’s sufficient to say that physically a structure is just a concatenation of the objects included in this structure converted to bit strings
:::

### Language

To create a property that creates a structure the [**STRUCT** operator](STRUCT_operator.md) is used.

To create a property that returns an object from the structure the [**\[ \]** operator](Operator_.md) is used.

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=struct"/>


<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=brackets"/>

**  
**
