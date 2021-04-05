---
title: 'How-to: Документы со строками'
---

## Пример 1

### Условие

Есть заказы и их спецификация в виде строк.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseDocument&block=sample1"/>

Необходимо создать форму со списком заказов с возможностью их добавления, редактирования и удаления.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseDocument&block=solution1"/>

На форме *order* для объекта строки не добавляется ссылка на заказ, так как при добавлении объекта через *NEW*, ссылка будет автоматически проставлена на основании конструкции *FILTERS*.

## Пример 2

### Условие

Аналогично Примеру 1.

Необходимо добавить на форму со списком заказов их спецификацию.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseDocument&block=solution2"/>

Это бывает удобно, чтобы пользователь мог смотреть состав заказа, не редактируя его.

 
