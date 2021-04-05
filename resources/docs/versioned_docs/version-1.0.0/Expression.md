---
title: 'Expression: Overview'
sidebar_label: Overview
---

An *expression* is a combination of [property operators](Property_operators.md) and [parameters](Properties.md). When an expression is evaluated sequentially in [priority](Operator_priority.md)  order, all the operators are executed.

The result of that execution will be either a [property](Properties.md) or a parameter (in the case of single-parameter expression). Their value shall be called the *value* of the expression.

An expression can be described by the following set of recursive rules:

|<strong>Rule</strong>|<strong>Description</strong>|
|---|---|
|<pre><code>expression := parameter \| constant \| prefixOperator</code></pre>|A single parameter, [constant](Constant.md), or non-arithmetic prefix operator|
|<pre><code>expression := prefixArithmOp expression</code></pre>|A unary arithmetic prefix operator, with the expression passed to it as an operand|
|<pre><code>expression := expression postfixOp</code></pre>|A unary postfix operator, with the expression passed to it as an operand|
|<pre><code>expression := expression binaryOp expression</code></pre>|A binary operator with the expressions passed to it as operands|
|<pre><code>expression := ( expression )</code></pre>|Expression in parentheses|

An expression cannot include [context-independent](Property_operators.md#contextindependent) property operators.

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ExpressionSample"/>

**  
**
