---
title: 'How-to: IF/CASE'
---

## Пример 1

### Условие

Есть список книг, которые привязаны к заданным категориям. Также для каждой книги задана цена.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseIfCase&block=sample1"/>

Нужно создать действие, которое установит определенную цену на книгу, если она относится к заданной категории, и фиксированную цену в противном случае. Если категория не выбрана, то должно быть выдано сообщение об ошибке.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseIfCase&block=solution1"/>

## Пример 2

### Условие

Аналогично **Примеру 1**.

Нужно создать действие, которое устанавливает одну из трех цен в зависимости от трех категорий. Во всех остальных случаях цена должна стать равной нулю.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseIfCase&block=solution2"/>
