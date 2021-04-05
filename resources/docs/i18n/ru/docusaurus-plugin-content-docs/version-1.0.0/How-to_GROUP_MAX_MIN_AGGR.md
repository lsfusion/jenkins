---
title: 'How-to: GROUP MAX/MIN/AGGR'
---

## Пример 1

### Условие

Есть набор книг, для каждой из которых задан уникальный номер.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseMMA&block=sample1"/>

Необходимо найти максимальный номер книги.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseMMA&block=solution1"/>

## Пример 2

### Условие

Аналогично **Примеру 1**.

Необходимо по номеру книги найти объект *Книга*.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseMMA&block=solution2"/>

Вариант 2 отличается от варианта 1 тем, что объявление этого свойства добавляет [ограничение](Constraints.md) на уникальность номера для книги. При попытке добавить две книги с одинаковыми номерами будет выдано сообщение с ошибкой.

## Пример 3

### Условие

Есть набор книг, для каждой из которых задана категория и цена.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseMMA&block=sample3"/>

Нужно посчитать минимальную цену по категории.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseMMA&block=solution3"/>

## Пример 4

### Условие

Задан документ отгрузки книг.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseMMA&block=sample4"/>

Необходимо найти некоторую строку отгрузки по документу и книге.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseMMA&block=solution4"/>

Этот свойство может использоваться для реализации функционала Подбор при вводе документа отгрузки.
