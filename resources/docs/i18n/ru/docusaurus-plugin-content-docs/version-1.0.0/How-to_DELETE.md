---
title: 'How-to: DELETE'
---

## Пример 1

### Условие

Есть заказ, с заданной датой и покупателем и строками заказов, которые ссылаются на книги.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseDelete&block=sample1"/>

Нужно создать действие, которое удалит книгу, если по ней нету заказов.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseDelete&block=solution1"/>

## Пример 2

### Условие

Аналогично **Примеру 1**.

Нужно создать действие, которое очистит заказ, удалив все его строки.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseDelete&block=solution2"/>

Оба варианта равносильны с точки зрения выполнения.
