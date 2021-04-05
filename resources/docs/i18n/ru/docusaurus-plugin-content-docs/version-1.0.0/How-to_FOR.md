---
title: 'How-to: FOR'
---

## Пример 1

### Условие

Есть список книг с наименованиями.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseFor&block=sample1"/>

Нужно найти все книги, содержащие определенную строк и выдать сообщение с именем и внутренним кодом.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseFor&block=solution1"/>

Для определения содержит ли одна строка другую используется свойство isSubstring, определенное в системном [модуле](Modules.md) **Utils**.

## Пример 2

### Условие

Аналогично **Примеру 1**.

Нужно создать действие, которое создаст 100 новых книг с определенными наименованиями.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseFor&block=solution2"/>

Оба варианта идентичны с точки зрения полученного результата.

Для решения задачи используется свойство **iterate**, определенное в системном модуле **Utils**, которое принимает значение **TRUE** для всех целых чисел в диапазоне.

## Пример 3

### Условие

Аналогично **Примеру 1**, но добавлена логика заказов. Для каждого заказа заданы строки с указанием книги, цен со скидками.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseFor&block=sample3"/>

Нужно создать действие, которое проставит скидку всем строкам, у которых цена больше 100.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseFor&block=solution3"/>

Оба варианта идентичны с точки зрения полученного результата.

## Пример 4

### Условие

Аналогично **Примеру 3**, но для книги добавлена цена по умолчанию.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseFor&block=sample4"/>

Нужно создать действие, которое добавит в заказ все книги с ценой больше 100.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseFor&block=solution4"/>

Оба варианта идентичны с точки зрения полученного результата.
