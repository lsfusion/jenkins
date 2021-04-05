---
title: 'Сигнатура свойства (CLASS)'
---

Оператор сигнатуры создает [свойство](Properties.md), которое определяет может ли, с точки зрения [классов](Classes.md), заданное свойство иметь не **NULL** значение для переданных аргументов или нет. Фактически данный оператор выводит возможные классы заданного свойства из его семантики, после чего при помощи [логических](Logical_operators_AND_OR_NOT_XOR_.md) операторов и оператора [классификации](Classification_IS_AS_.md) создает требуемое свойство.

### Язык

Для реализации этого оператора используется [оператор **CLASS**](CLASS_operator.md).

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=class"/>
