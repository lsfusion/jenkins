---
title: 'Логические операторы (AND, OR, NOT, XOR)'
---

*Логические операторы* создают [свойства](Properties.md), которые рассматривают свои аргументы как логические значения [класса **BOOLEAN**](Built-in_classes.md), и возвращаемым значением которых также является значение класса **BOOLEAN**. Если значение аргумента логического оператора не равно **NULL**, то аргумент рассматривается как значение **TRUE** класса **BOOLEAN**, иначе **NULL**.

В платформе на данный момент поддерживаются следующие логические операторы:

|<div><br/>Оператор<br/></div>|<div><br/>Название<br/></div>|<div><br/>Описание<br/></div>|<div><br/>Пример<br/></div>|<div><br/>Результат<br/></div>|
|---|---|---|---|---|
|AND|Коньюнкция|Принимает два операнда на вход, возвращает <strong>TRUE</strong>, если оба операнда не <strong>NULL</strong>|TRUE AND TRUE|TRUE|
|OR|Дизьюнкция|<p>Принимает два операнда на вход, возвращает <strong>TRUE</strong>, если один из операндов не <strong>NULL</strong></p>|NULL OR TRUE|TRUE|
|NOT|Отрицание|Принимает один операнд на вход, возвращает <strong>TRUE</strong>, если операнд <strong>NULL</strong>|NOT TRUE|NULL|
|XOR|Исключение|Принимает два операнда на вход, возвращает <strong>TRUE</strong>, если ровно один из операндов не <strong>NULL</strong>|TRUE XOR TRUE|NULL|

### Язык

Описание [синтаксиса логических операторов](AND_OR_NOT_XOR_operators.md).

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=aonx"/>
