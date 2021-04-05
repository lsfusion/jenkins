---
title: 'How-to: SEEK'
---

## Пример 1

### Условие

Определена логика книг и категорий. Создана форма со списком книг, разбитых по категориям.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSeek&block=sample1"/>

Нужно создать действие, которое создаст новую книгу, автоматически проставит текущую категорию и выберет ее активной после того, как пользователь сохранит и закроет форму.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSeek&block=solution1"/>

После закрытия формы вызывается оператор [SEEK](SEEK_operator.md), который делает добавленный объект активным.

## Пример 2

### Условие

Аналогично **Примеру 1**. Также добавлена логика покупателей. Для каждого покупателя и книги можно задать цену в определенной форме.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSeek&block=sample2"/>

Нужно добавить покупателя по умолчанию, который будет проставляться при открытии формы.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSeek&block=solution2"/>

Свойство с покупателем по умолчанию добавляется на форму Настройка во вкладку Общие. Текущий объект изменится при входе на форму, так как сработает событие [ON INIT](Event_block.md).

## Пример 2

### Условие

Предположим, что есть некоторая форма отчетов, в котором задан интервал дат.

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSeek&block=sample3"/>

Нужно сделать кнопки, которые будут изменять интервал на последнюю неделю, текущий месяц и последний месяц.

### Решение

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseSeek&block=solution3"/>

Свойства по работе с датами находятся в [системном модуле](Modules.md) **Time**, который подключается в самом начале через инструкцию **REQUIRE**.
