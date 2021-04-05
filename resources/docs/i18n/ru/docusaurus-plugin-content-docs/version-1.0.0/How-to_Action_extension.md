---
title: 'How-to: Расширение действий'
---

Для реализации полиморфизма можно использовать следующую схему :

### Пример 1

Создаем абстрактный класс **Shape** с абстрактным действием **whoAmI** :

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseActionShape&block=shape"/>

Создаем классы **Square** и **Circle**, который наследуется от **Shape** :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseActionShape&block=concreteclass"/>

Определяем реализацию **whoAmI** для созданных классов :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseActionShape&block=concreteaction"/>

При выполнении действия **whoAmI** будут вызваны все действия, которые были добавлены в качестве реализации. В описанном случае будет выдано соответствующее сообщение в зависимости от переданного аргумента.

### Пример 2

Предположим, что существует необходимость реализовать действие по копированию некоторого объекта (например, класса **Book**), семантика которого определяется в различных модулях. Это может быть реализовано следующим образом :

Объявляем класс **Book** и действия по его копированию :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseActionBook"/>

В зависимом модуле **MyBook** расширяем класс **Book** новыми свойствами и делаем, чтобы они также копировались :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseActionMyBook"/>
