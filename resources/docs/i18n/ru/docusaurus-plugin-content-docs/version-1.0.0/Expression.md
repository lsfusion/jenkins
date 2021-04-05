---
title: 'Выражения: Обзор'
sidebar_label: Обзор
---

*Выражение* - это комбинация [операторов-свойств](Property_operators.md) и [параметров](Properties.md). При вычислении выражения последовательно в порядке [приоритетов](Operator_priority.md) выполняются все операторы.

*Значением* выражения будем называть возвращаемое значение [свойства](Properties.md), получившегося в результате выполнения операторов, либо значение параметра, если выражение являлось одиночным параметром.

Выражение можно описать набором следующих рекурсивных правил:

|<strong>Правило</strong>|<strong>Описание</strong>|
|---|---|
|<pre><code>expression := parameter \| constant \| prefixOperator</code></pre>|Одиночный параметр, [константа](Constant.md) или префиксный неарифметический оператор|
|<pre><code>expression := prefixArithmOp expression</code></pre>|Унарный префиксный арифметический оператор, с переданным ему в качестве операнда выражением|
|<pre><code>expression := expression postfixOp</code></pre>|Унарный постфиксный оператор, с переданным ему в качестве операнда выражением|
|<pre><code>expression := expression binaryOp expression</code></pre>|Бинарный оператор с переданными ему в качестве операндов выражениями|
|<pre><code>expression := ( expression )</code></pre>|Выражение в круглых скобках|

В состав выражения не могут входить [контекстно-независимые](Property_operators.md#contextindependent) операторы-свойства.

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ExpressionSample"/>

**  
**
