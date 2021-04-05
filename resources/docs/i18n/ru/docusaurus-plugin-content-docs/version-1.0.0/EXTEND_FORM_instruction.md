---
title: 'Инструкция EXTEND FORM'
---

Инструкция **EXTEND FORM** - [расширение](Form_extension.md) существующей [формы](Forms.md).

### Синтаксис

    EXTEND FORM formName 
        formBlock1
        ...
        formBlockN
    ;

### Описание

Инструкция **EXTEND FORM** позволяет расширять существующую форму дополнительными [блоками формы](FORM_instruction.md#blocks-broken).

### Параметры

*formName*

Имя расширяемой формы. [Составной идентификатор](IDs.md#cid-broken).

*formBlock1 ... formBlockN *

Блоки формы.

### Пример


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=FormSample&block=extendform"/>

  
