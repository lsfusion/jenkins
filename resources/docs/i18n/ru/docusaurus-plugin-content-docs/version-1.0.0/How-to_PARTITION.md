---
title: 'How-to: PARTITION'
---

## Пример 1

### Условие

Есть заказ вместе со строками.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePartition&block=sample1"/>

Необходимо пронумеровать строки от 1 в данном заказе в порядке их добавления.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePartition&block=solution1"/>

В данном случае, сортируем по внутреннему идентификатору строки заказа, так как гарантируется, что он возрастает при создании новых строк.

## Пример 2

### Условие

Есть список заказов покупателей с указанной датой.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePartition&block=sample2"/>

Необходимо для заказа найти дату предыдущего заказа этого же покупателя.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePartition&block=solution2"/>

Как и в случае с **[How-to: GROUP CONCAT](How-to_GROUP_CONCAT.md)**, порядок должен быть полностью детерминирован. Поэтому в **ORDER** последним параметром добавляется сам заказ (де-факто, его внутренний идентификатор).

## Пример 3

### Условие

Есть текущие остатки книг по партиям на складе.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePartition&block=sample3"/>

Необходимо для заданного количества одной книги расписать это количество по партиям по принципу FIFO.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePartition&block=solution3"/>

Параметр **STRICT** обозначает, что если количество будет больше остатка по всем партиям, то вся разница будет добавлена к последней партии.  
  
