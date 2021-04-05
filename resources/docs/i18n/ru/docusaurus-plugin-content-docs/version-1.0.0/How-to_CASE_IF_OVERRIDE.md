---
title: 'How-to: CASE/IF/OVERRIDE'
---

## Пример 1

### Условие

Есть набор книг, которые могут быть белыми и черными.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseCIO&block=sample1"/>

Нужно определить свойство, которое возвращает цвет книги.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution1"/>

В данном случае эти два варианта идентичны.

## Пример 2

### Условие

Есть заказы на книги поставщикам. Для каждого из них определено, был ли он отправлен поставщику, согласован и поставлен. В примере они введены как [первичные](Data_properties_DATA_.md) свойства, но в более сложных случаях они будут вычисляемыми.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseCIO&block=sample2"/>

Необходимо определить статус заказа.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution2"/>

## Пример 3

### Условие

Есть набор книг, аналогично **Примеру 1**.

Нужно задать для книги торговую надбавку, но чтобы можно было задать значение по умолчанию.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution3"/>

## Пример 4

### Условие

Аналогичен **Примеру 3**, только для книги задана категория.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseCIO&block=sample4"/>

Нужно задать для книги торговую надбавку, но чтобы можно было задать значение по умолчанию для категории, к которой она относится.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution4"/>

## Пример 5

### Условие

Есть набор книг, для каждой из которых задан номер.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseCIO&block=sample5"/>

Нужно найти номер, следующий за максимальным.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution5"/>

Оператор (+) используется вместо обычного оператора +, так как  если не будет ни одной книги, то обычное сложение с единицей вернет **NULL**.
