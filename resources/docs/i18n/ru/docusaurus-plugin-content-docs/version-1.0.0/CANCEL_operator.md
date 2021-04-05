---
title: 'Оператор CANCEL'
---

Оператор **CANCEL **- создание [действия](Actions.md), [отменяющего изменения](Cancel_changes_CANCEL_.md) в текущей сессии.

### Синтаксис

    CANCEL [nestedBlock]

где *nestedBlock* имеет один из двух вариантов синтаксиса:

    NESTED LOCAL
    NESTED (propertyId1, ..., propertyIdN)

### Описание

Оператор **CANCEL** создает действие, которое отменяет изменения в текущей сессии. С помощью указания ключевого слова **NESTED **можно указать [локальные свойства](Data_properties_DATA_.md#local), изменения которых не сбросятся при отмене изменений. 

### Параметры

*LOCAL*

Ключевое слово. Если указывается, то все локальные свойства сохранят свои изменения после выполнении оператора **CANCEL**. 

*propertyId1, ..., propertyIdN*

Список локальных свойств. Каждый элемент списка является [идентификатором свойства](IDs.md#propertyid-broken). Указанные в списке локальные свойства сохранят свои изменения после выполнении оператора.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=cancel"/>

  
