---
title: 'Arithmetic operators (+, -, \*, ...)'
---

*Arithmetic operators* create [properties](Properties.md) whose value is the result of an arithmetic operation. The arguments of these operators must be properties whose values are instances of [number classes](Built-in_classes.md) . The platform currently supports the following arithmetic operators:

|Operator|Name|Description|Example|Result|
|---|---|---|---|---|
|<strong>+</strong>|Summation|Takes two input operands and returns their sum|3 + 5|8|
|<strong>-</strong>|Difference|<p>Accepts two input operands and returns their difference</p><br/><p>This operator also has a unary form, in which case the first operand is considered equal to 0</p>|<p>5 - 3</p><br/><p>-3</p>|<p>2</p><br/><p>-3</p>|
|<strong>*</strong>|Multiplication|Accepts two input operands and returns their product|3 * 5|15|
|<strong>/</strong>|Ratio|Takes two input operands and returns their ratio|15 / 3|5|

All of these operators return **NULL** if one of the operands is **NULL** . It is also possible to use a special form of summation and difference operators with brackets, in which case **NULL** will be equivalent to 0. The reverse is also true for these type of operators: if the result of an operator in such form is 0, then **NULL is returned** (e. g., 5 (-) 5 = **NULL**):

|<strong>Operator</strong>|<strong>Name</strong>|<strong>Description</strong>|<strong>Example</strong>|<strong>Result</strong>|
|---|---|---|---|---|
|<strong>(+)</strong>|Summation|Takes two input operands and returns their sum, treating <strong>NULL</strong> as 0|<p>3 (+) 5</p><br/><p>3 (+) <strong>NULL</strong></p>|<p>8</p><br/><p>3</p>|
|<strong>(-)</strong>|Difference|Takes two input operands and returns their difference, treating <strong>NULL</strong> as 0|<p>5 (-) 3</p><br/><p>5 (-) <strong>NULL</strong></p><br/><p>5 (-) 5</p>|<p>2</p><br/><p>5</p><br/><p><strong>NULL</strong></p>|

### Determining the result class

The result class is determined as:

|Operator|Result|
|---|---|
|<strong>+</strong>, <strong>-</strong>|[Common ancestor](Built-in_classes.md#commonparentclass) ("Numbers" family)|
|<strong>*</strong>|<pre><code>NUMERIC[p1.IntegerPart + p1.Precision + p2.IntegerPart + p2.Precision, p1.Precision + p2.Precision]</code></pre>|
|<strong>/</strong>|<pre><code>NUMERIC[p1.IntegerPart + p1.Precision + p2.IntegerPart + p2.Precision, p1.Precision + p2.IntegerPart]</code></pre>|

  

### Language

Description [of arithmetic operators](Arithmetic_operators.md).

### Examples

```lsf
sum(a, b) = a + b;
transform(a, b, c) = -a * (b (+) c);
```
