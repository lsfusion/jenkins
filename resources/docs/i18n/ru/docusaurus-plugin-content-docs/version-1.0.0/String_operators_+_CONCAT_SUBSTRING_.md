---
title: 'Строковые операторы (+, CONCAT, SUBSTRING)'
---

Строковые операторы - операторы, параметрами и результатом которых являются свойства, значения которых являются экземплярами [строковых классов](Built-in_classes.md). В платформе на данный момент поддерживаются следующие строковые операторы:

|<div><br/><div><br/>Оператор<br/></div><br/></div>|<div><br/><div><br/>Название<br/></div><br/></div>|<div><br/><div><br/>Описание<br/></div><br/></div>|<div><br/><div><br/>Пример<br/></div><br/></div>|<div><br/><div><br/>Результат<br/></div><br/></div>|
|---|---|---|---|---|
|<strong>+, <strong>[CONCAT](CONCAT_operator.md)</strong></strong>|Объединение|Принимает два операнда на вход, и возвращает строку, полученную объединением строк, указанных в операндах|'a' + 'b'|'ab'|
|<strong>SUBSTRING</strong>|Подстрока|<p>Принимает три операнда на вход, возвращает подстроку, полученную из строки, указанной в первом операнде, начиная с символа, указанного во втором операнде, и длины, указанной в третьем операнде</p>|SUBSTRING('abc', 2, 2)|'bc'|

Операторы **+** и **SUBSTRING** возвращают **NULL**, если один из операндов **NULL**. Для оператора **CONCAT** значение **NULL** в операнде будет эквивалентно пустой строке (но объединение двух **NULL** значений все равно возвращает **NULL**). Также, в операторе **CONCAT,** можно дополнительно указывать третий операнд (*разделитель*), который будет вставляться тогда и только тогда, когда оба операнда не **NULL**. Например CONCAT ' ', 'John', 'Smith' = 'John Smith', а CONCAT ' ', 'John', NULL = 'John'.

### Определение класса результата

Класс результата определяется как:

|Оператор|Описание|
|---|---|
|<strong>+</strong>, <strong>CONCAT</strong>|<pre><code>result = STRING[p1.blankPadded AND p2.blankPadded, p1.caseInsensitive OR p2.caseInsensitive, p1.length + p2.length]</code></pre>|
|<strong>SUBSTRING(p, from, length)</strong>|<pre><code>result = STRING[p.blankPadded, p.caseInsensitive, length]</code></pre>|

где *blankPadded*, *caseInsensitive* и *length* определяются аналогично правилам построения [общего предка](Built-in_classes.md#commonparentclass) для двух встроенных классов (семейство - Строки).

В операторе + операнды, классы которых отличны от строк, приводятся к строкам в соответствии со следующей таблицей:

|Класс|Приведенный класс|
|---|---|
|<p><strong>DATE</strong>, <strong>DATETIME</strong>, <strong>TIME</strong></p>|<strong>STRING[25]</strong>|
|<strong>NUMERIC</strong>|<strong>STRING[p.length]</strong>|
|<strong>LOGICAL</strong>|<strong>STRING[1]</strong>|
|<strong>FILE</strong>|<strong>TEXT</strong>|
|[Объект](Static_objects.md)|<strong>STRING[10]</strong>|
|Остальные|<strong>STRING[8]</strong>|

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=concat"/>
