---
title: 'String operators (+, CONCAT, SUBSTRING)'
---

String operators are operators which parameters and result are the properties which values are instances of the string classes. The platform currently supports the following string operators:

|<div><br/><div><br/>Operator<br/></div><br/></div>|<div><br/><div><br/>Name<br/></div><br/></div>|<div><br/><div><br/>Description<br/></div><br/></div>|<div><br/><div><br/>Example<br/></div><br/></div>|<div><br/><div><br/>Result<br/></div><br/></div>|
|---|---|---|---|---|
|<strong>+, <strong>[CONCAT](CONCAT_operator.md)</strong></strong>|Concatenation|Takes two operands and returns a string obtained by concatenating the strings specified in the operands|'a' + 'b'|'ab'|
|<strong>SUBSTRING</strong>|Substring|<p>Takes three operands and returns a substring obtained from the string specified in the first operand, starting with the character specified in the second operand, and having length specified in the third operand</p>|SUBSTRING('abc', 2, 2)|'bc'|

The **+** and **SUBSTRING** operators return **NULL** if one of the operands is **NULL**. The **CONCAT** operator treats **NULL** value of the operand as an empty string (however, concatenation of two **NULL** values still returns **NULL**). Also, in the **CONCAT** operator you can optionally specify the third operand (*delimiter*) which will be inserted if and only if both operands are not **NULL**. For example, CONCAT ' ', 'John', 'Smith' = 'John Smith', but CONCAT ' ', 'John', NULL = 'John'.

### Determining the result class

The result class is defined as:

|Operator|Description|
|---|---|
|<strong>+</strong>, <strong>CONCAT</strong>|<pre><code>result = STRING[p1.blankPadded AND p2.blankPadded, p1.caseInsensitive OR p2.caseInsensitive, p1.length + p2.length]</code></pre>|
|<strong>SUBSTRING(p, from, length)</strong>|<pre><code>result = STRING[p.blankPadded, p.caseInsensitive, length]</code></pre>|

where *blankPadded*, *caseInsensitive* and *length* are determined similarly to the rules for construction of a common ancestor for two built-in classes (Strings family).

In the + operator, operands which classes are other than string are cast to strings in accordance with the following table:

|Class|Cast class|
|---|---|
|<p><strong>DATE</strong>, <strong>DATETIME</strong>, <strong>TIME</strong></p>|<strong>STRING[25]</strong>|
|<strong>NUMERIC</strong>|<strong>STRING[p.length]</strong>|
|<strong>LOGICAL</strong>|<strong>STRING[1]</strong>|
|<strong>FILE</strong>|<strong>TEXT</strong>|
|[Object](User_classes.md)|<strong>STRING[10]</strong>|
|Other|<strong>STRING[8]</strong>|

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=concat"/>
