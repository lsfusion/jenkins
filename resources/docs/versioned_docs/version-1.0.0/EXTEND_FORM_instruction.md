---
title: 'EXTEND FORM instruction'
---

The **EXTEND FORM** instruction [extends](Form_extension.md) an existing [form](Forms.md).

### Syntax

    EXTEND FORM formName 
        formBlock1
        ...
        formBlockN
    ;

### Description

The **EXTEND FORM** instruction allows you to extend an existing form with additional [form blocks](FORM_instruction.md#blocks-broken).

### Parameters

*formName*

The name of the form being extended. [Composite ID](IDs.md#cid-broken) .

*formBlock1 ... formBlockN *

Form blocks.

### Example


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=FormSample&block=extendform"/>

  
