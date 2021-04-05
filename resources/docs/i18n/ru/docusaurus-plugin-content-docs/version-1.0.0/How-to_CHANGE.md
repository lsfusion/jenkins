---
title: 'How-to: CHANGE'
---

## Пример 1

### Условие

Есть заказ на продажу книг.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseAssign&block=sample1"/>

Нужно создать действие, при выполнении которого, заказ будет выставлен на 30 дней вперед, начиная с текущей датой, и применена скидка 5%.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseAssign&block=solution1"/>

## Пример 2

### Условие

Аналогично **Примеру 1**, только добавлена спецификация к заказу. Также для каждой книги указана текущая цена.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseAssign&block=sample2"/>

Нужно создать действие, при выполнении которого, для всех строк заказа будет проставлена текущая цена для соответствующих книг.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseAssign&block=solution2"/>

Важно не забыть указать в действии WHERE, так как иначе цена будет установлена для всех строк заказов в базе данных.
