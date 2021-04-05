---
title: 'How-to: Деревья'
---

## Пример 1

### Условие

Есть список книг, привязанных к определенным категориям.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseTree&block=sample1"/>

Нужно построить форму с деревом, в котором на верхнем уровне будет категория, а под ним - товар.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseTree&block=solution1"/>

## Пример 2

### Условие

Аналогичен **Примеру 1**, но для категории задана иерархия путем указания родителя каждой категории.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseTree&block=sample2"/>

Нужно построить форму с деревом, в котором будут отображаться категории в виде дерева.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseTree&block=solution2"/>

## Пример 3

### Условие

Аналогичен **Примеру 2**.

Нужно сделать форму с деревом категорий, справа от которого показать книги, которые относятся к текущей категории и всем ее потомкам.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseTree&block=solution3"/>
