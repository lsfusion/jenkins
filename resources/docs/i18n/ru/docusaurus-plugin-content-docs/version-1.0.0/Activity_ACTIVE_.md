---
title: 'Активность (ACTIVE)'
---

Оператор *активности* создает свойство, которое определяет, является ли активным один из следующих элементов формы:

-   Свойство - находится ли фокус на заданном [свойстве](Properties.md) на форме.
-   Закладка - является ли одна из закладок активной в заданной [панели закладок](Form_design.md#tab-broken).
-   Форма - определяет активна ли у пользователя заданная [форма](Forms.md).

### Язык

Для создания свойства, определяющего активность закладки, используется [оператор **ACTIVE TAB**](ACTIVE_TAB_operator.md). Определение активности формы реализуется путем создания действия с использованием [оператора **ACTIVE FORM**](ACTIVE_FORM_operator.md).

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=activetab"/>


<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=activeform"/>

  
