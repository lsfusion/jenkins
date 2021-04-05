---
title: 'How-to: GROUP SUM'
---

## Пример 1

### Условие

Есть набор книг, привязанных к определенной категории.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSum&block=sample1"/>

Необходимо посчитать количество книг в категории.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSum&block=solution1"/>

## Пример 2

### Условие

Есть набор книг, привязанных к определенным тегам. Каждая книга может относиться к нескольким тегам одновременно.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSum&block=sample2"/>

Необходимо посчитать количество книг в теге.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSum&block=solution2"/>

## Пример 3

### Условие

Существует информация о движении книг, где для каждой записи есть ссылка на книгу и склад, по которому было движение, а также количество и тип операции (приход/расход).

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSum&block=sample3"/>

Необходимо посчитать текущей остаток по складу для книги.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSum&block=solution3"/>

Свойство **currentBalance** лучше всего помечать как **[MATERIALIZED](Materializations.md)** для того, чтобы при чтении текущего остатка система не считала его на основе движения за все время, а обращалась бы к таблице **bookStock** с уже готовым значением. Это замедляет запись (так как при записи каждого движения будет требоваться обновление текущего остатка), однако значительно ускоряет чтение.

Для свойства **currentBalance** нигде явно не задается, в какой таблице оно должно храниться, так как система может сама определить по сигнатуре, в какую таблицу его лучше всего положить (в данном случае это будет таблица **bookStock**).

## Пример 4

### Условие

Аналогично **Примеру 3**, только для движения указана дата движения.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSum&block=sample4"/>

Необходимо посчитать остаток по складу для книги на дату (на утро, без учета движений за выбранную дату).

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSum&block=solution4"/>

  

Второй вариант предпочтительнее. Так как чаще всего запросы будут идти к датам ближе к текущей, то сервер будет считать от текущего остатка "назад", что требует использования меньшего количества записей.

## Пример 5

### Условие

Аналогично **Примеру 3**, только нужно посчитать остаток книги по всем складам.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSum&block=solution5"/>

  

Это свойство в отличии от текущего остатка по складам нет смысла делать MATERIALIZED в случае, если количество складов невелико, так как это может приводить к UPDATE CONFLICT при одновременной записи несколькими пользователями движения по одной книге по разным складам.
