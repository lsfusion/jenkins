---
title: 'Инструкция @'
---

Инструкция @ - использование [метакода](Metaprogramming.md#metacode).

### Синтаксис

    @name(param1, ..., paramN);

### Описание

Инструкция @ формирует код, получаемый из метакода с именем **name**, заменяя параметры метакода на значения собственных параметров и выполняя специальные [инструкции \#\# и \#\#\#](Metaprogramming.md#concat). 

### Параметры 

*name*

Имя используемого метакода. [Составной идентификатор](IDs.md#cid-broken).  

*param1, ..., paramN*

Список параметров инструкции, которые будут подставляться вместо параметров используемого метакода. В качестве параметров могут выступать [составной идентификатор](IDs.md#cid-broken), [идентификатор класса](IDs.md#classid-broken), [литерал](Literals.md) или *пустой параметр*, когда ничего не передается в качестве параметра.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=InstructionSample&block=implementmeta"/>

  
