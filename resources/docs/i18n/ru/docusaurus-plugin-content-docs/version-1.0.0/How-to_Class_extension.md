---
title: 'How-to: Расширение классов'
---

Классическая схема для отделения связи между классами в отдельный модуль выглядит следующим образом :

Создаем модуль **MA**, в котором будет создаваться класс **A** :

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseClassMA"/>

Создаем модуль **MB**, в котором будет создаваться класс **B** :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseClassMB"/>

Создаем модуль **MBA**, в котором будет определяться связь между классами **A** и **B** :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseClassMBA"/>

Таким образом, непосредственной зависимости между модулями **MA** и **MB**, что позволяет включать/отключать связь между ними при необходимости путем подключения модуля **MBA**. Следует отметить, что модуль **MBA** расширяет функциональность модуля **MB**, не изменяя при этом его кода.

Применять mixin классов при использовании метакода можно следующим образом :

Предположим, что у нас существует метакод, который объявляет класс и задает ему определенные свойства :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseClassMyModule&block=define"/>

Следует отметить, что при вызове этого метакода, невозможно указать классы, от которого должно происходить наследование создаваемого класса. Однако, это можно реализовать посредством mixin'а классов следующим образом :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseClassMyModule&block=implement"/>
