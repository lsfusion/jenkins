---
title: 'How-to: Пространства имен'
---

Иногда возникают ситуации, когда необходимо использовать одинаковое [имя](Naming.md) для разных элементов системы. Для этой цели их можно отнести к разным пространствам имен, которое задается для модуля через инструкцию [NAMESPACE](Module_header.md). По умолчанию, пространство имен совпадает с именем модуля.

Создадим два модуля **UseCaseNamePurchase** и **UseCaseNameSale**, которые объявляют логику заказов на закупку и продажу :

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseNamePurchase&block=sample"/>

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseNameSale&block=sample"/>

В обоих из них объявлен класс **Order**, но так как у модулей разные пространства имен, то у первого класса оно будет **Purchase**, а у второго **Sale**.

Объявим тестовый модуль с пространством имен **Test**, который будет одновременно зависеть и от первого, и от второго модуля :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseNameTest&block=sample"/>

При попытке обратиться к классу **Order** без явного указания пространства имен будет выдана ошибка :

![](attachments/60555394/60555398.png)

Все такие обращения требуют явного указания пространства имен.

В случае, если пространство имен модуля совпадает с пространством искомого элемента системы (например, **Purchase**)

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseNameTest2&block=namespace"/>

или указан приоритет пространств имен через инструкцию [PRIORITY](Module_header.md)

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseNameTest2&block=priority"/>

то можно пространство имен не указывать

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseNameTest2&block=sample"/>

Без указания пространства имен будет использоваться класс из **Purchase**. При этом остается возможность в явную указать пространство имен (например, **Sale**).
