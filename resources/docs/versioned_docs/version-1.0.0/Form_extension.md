---
title: 'Form extension'
---

The [form](Forms.md) [extension](Extensions.md) technique allows the developer to extend the [structure](Form_structure.md) and [design](Interactive_view.md) of a form created in another module.

Form extension allows you to extract a specific functionality into a separate module, which when loaded will cause new components to be "embedded" into existing forms. The disadvantage of this approach is that this module must know the precise structure and design of the form which it depends on, and when these are modified the module may become inoperative.

### Language

In order to extend the structure and design of an existing form, the [**EXTEND FORM** instruction](EXTEND_FORM_instruction.md) must be used.

### Example

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=FormSample&block=extendform"/>
