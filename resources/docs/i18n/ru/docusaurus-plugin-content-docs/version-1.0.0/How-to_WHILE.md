---
title: 'How-to: WHILE'
---

## Пример 1

### Условие

Есть заказ, для которого задана дата.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseWhile&block=sample1"/>

Нужно для каждой даты из заданного интервала выдать сообщение пользователю с количеством заказов за эту дату.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseWhile&block=solution1"/>

Оба варианта идентичны с точки зрения полученного результата.

Для добавления к дате одного дня используется свойство **sum**, определенное в системном [модуле](Modules.md) **Time**.

## Пример 2

### Условие

Аналогично **Примеру 1**. Также определены строки заказов, для каждой из которой определены сумма (полная) и сумма скидки.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseWhile&block=sample2"/>

Нужно создать действие, которое "распишет" заданную сумму скидки по строкам, начиная со строки с наибольшей суммой.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseWhile&block=solution2"/>

## Пример 3

### Условие

Задана логика изменения остатка для книги следующим образом :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseWhile&block=sample3"/>

Нужно создать действие, которое посчитает накопленный остаток (интеграл) за определенный интервал времени.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseWhile&block=solution3"/>
