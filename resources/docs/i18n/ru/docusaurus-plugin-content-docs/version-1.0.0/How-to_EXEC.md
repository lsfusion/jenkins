---
title: 'How-to: EXEC'
---

## Пример 1

### Условие

Есть категория книг, для которой заданы наименование, числовой код и дата начала продаж.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseExec&block=sample1"/>

Нужно создать действие, которое создаст 3 новых предопределенных категории.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseExec&block=solution1"/>

## Пример 2

### Условие

Аналогично **Примеру 1**, но у каждой категории задан ее "родитель".

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseExec&block=sample2"/>

Нужно создать действие, которое проставит глубину категории для каждой из них.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseExec&block=solution2"/>
