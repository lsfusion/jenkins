---
title: 'How-to: GROUP LAST'
---

## Пример 1

### Условие

Есть набор книг, привязанных к определенной категории, и даты их прихода.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseLast&block=sample1"/>

Необходимо найти последнюю приходившую книгу по выбранной категории.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseLast&block=solution1"/>

Важно помнить, что в **ORDER* ***должен указываться однозначно определяемый порядок. Для этого вторым параметром там добавлена сама книга (точнее ее внутренний идентификатор), так как у нескольких книг может совпадать дата прихода.

## Пример 2 

### Условие

Аналогичен **Примеру 1**, но для каждой книги задан автор и список жанров.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseLast&block=sample2"/>

Нужно найти наиболее часто встречающуюся категорию по автору и жанру.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseLast&block=solution2"/>

## Пример 3

### Условие

Задан некоторый набор книг, и есть информация об изменении цен для книги и склада. Каждый объект класса **Ledger** отражает одно изменение цены, начиная с определенной даты.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseLast&block=sample3"/>

Нужно определить текущую цену, действующую на книгу для склада.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseLast&block=solution3"/>

## Пример 4

### Условие

Аналогично **Примеру 3**.

Нужно определить цену, действующую на определенную дату для книги и склада.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseLast&block=solution4"/>

## Пример 5

### Условие

Аналогично Примеру 4, только для изменения цены есть срок окончания действия. Если он не задан, то считается, что цена действует бесконечно.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseLast&block=sample5"/>

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseLast&block=solution5"/>

Важно учитывать, что выражение *NOT dateTo(l) < date* не эквивалентно *dateTo(l) \>= date*. Отличие возникает, когда значение *dateTo(l)* равно *NULL*. В первом случае *dateTo(l) < date* будет равно *NULL*(то есть ложь), а *NOT NULL* равно TRUE. Во втором случае выражение будет сразу равно *NULL* (то есть ложь).
