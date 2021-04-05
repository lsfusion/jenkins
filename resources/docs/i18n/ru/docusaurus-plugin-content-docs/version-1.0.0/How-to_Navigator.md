---
title: 'How-to: Навигатор'
---

## Пример 1

### Условие

Есть формы со списком книг и категорий.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseNavigator&block=sample1"/>

Нужно добавить их в [навигатор](Navigator.md) в новую папку **Приложение** снизу от основного тулбара.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseNavigator&block=solution1"/>

Указав **WINDOW** для элемента **application**, мы указали, что все его потомки должны отображаться в системном [окне](Navigator_design.md) **toolbar**. Это будет выглядеть следующим образом :

![](attachments/46367463/46367465.png)

## Пример 2

### Условие

Аналогичен **Примеру 1**

Нужно сделать, чтобы эти же формы были в подпапке Справочники.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseNavigator&block=solution2"/>

Результат :

![](attachments/46367463/46367468.png)
