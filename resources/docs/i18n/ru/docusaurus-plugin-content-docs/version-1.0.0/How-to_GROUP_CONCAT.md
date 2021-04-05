---
title: 'How-to: GROUP CONCAT'
---

## Пример 1

### Условие

Есть набор книг, привязанный к определенным тегам с приоритетами.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseConcat&block=sample1"/>

Необходимо получить список тэгов книги через запятую в алфавитном порядке.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseConcat&block=solution1"/>

Желательно для всех свойств, которые строятся при помощи **GROUP CONCAT**, задавать размеры, которые будут использоваться для вывода их на форму. По умолчанию система работает по "пессимистичному" сценарию и выдает очень много места таким свойствам.

## Пример 2

### Условие

Есть набор книг, привязанный к определенным категориям и авторам.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseConcat&block=sample2"/>

Необходимо получить список всех авторов по категории через запятую по убыванию количества книг.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseConcat&block=solution2"/>
