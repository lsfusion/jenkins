---
title: 'How-to: Расширение свойств'
---

Классический подход для реализации полиморфизма может выглядеть следующим образом :

Создаем абстрактный класс **Shape** с абстрактным свойством **square** :

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePropertyShape&block=shape"/>

Создаем классы **Rectangle **и **Circle**, который наследуется от **Shape** :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePropertyShape&block=concrete"/>

Определяем реализацию абстрактного свойства **square **для созданных классов :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePropertyShape&block=extendsimple"/>

Предположим, необходимо сделать таким образом, чтобы в определенных случаях можно было переопределить способ расчета площади для класса **Circle**. В таком случае, в строке с определением реализации площади для окружности можно вставить своеобразную "точку входа" в виде абстрактного свойства, реализацию которого можно изменить в другом модуле :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePropertyShape&block=extendover"/>

Если ни в одном модуле свойство **overSquareCircle** не будет реализовано, то его значение всегда будет равно **NULL** и будет использоваться базовый механизм расчета площади. Для изменения же расчета можно в некотором модуле **MyShape** задать иную реализацию, которая и будет использоваться :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCasePropertyMyShape"/>

Следует отметить, что вместо [оператора OVERRIDE](OVERRIDE_operator.md) можно использовать любые другие выражения. В частности, наиболее часто используемыми могут быть [операторы (+) и (-)](Arithmetic_operators_+_-_..._.md).
