---
title: 'Logical operators (AND, OR, NOT, XOR)'
---

*Logical operators* create [properties](Properties.md) that consider their arguments as logical values [of class **BOOLEAN**](Built-in_classes.md) and whose return value is also a value of class **BOOLEAN**. If the value of an argument of an logical operator is not **NULL**, then the argument is treated as the value **TRUE** of class **BOOLEAN**, otherwise as **NULL**.

The platform currently supports the following logical operators:

|<div><br/>Operator<br/></div>|<div><br/>Name<br/></div>|<div><br/>Description<br/></div>|<div><br/>Example<br/></div>|<div><br/>Result<br/></div>|
|---|---|---|---|---|
|AND|Conjunction|Takes two operands and returns <strong>TRUE</strong> if both operands are non-<strong>NULL</strong>|TRUE AND TRUE|TRUE|
|OR|Disjunction|<p>Takes two operands and returns <strong>TRUE</strong> if either operand is non-<strong>NULL</strong></p>|NULL OR TRUE|TRUE|
|NOT|Negation|Takes one operand and returns <strong>TRUE</strong> if the operands is <strong>NULL</strong>|NOT TRUE|NULL|
|XOR|Exception|Takes two operands and returns <strong>TRUE</strong> if exactly one operand is non-<strong>NULL</strong>|TRUE XOR TRUE|NULL|

### Language

Description of [logical operator syntax](AND_OR_NOT_XOR_operators.md).

### Examples

```lsf
likes = DATA BOOLEAN (Person, Person);
likes(Person a, Person b, Person c) = likes(a, b) AND likes(a, c);
outOfInterval1(value, left, right) = value < left OR value > right;
outOfInterval2(value, left, right) = NOT (value >= left AND value <= right);
```
