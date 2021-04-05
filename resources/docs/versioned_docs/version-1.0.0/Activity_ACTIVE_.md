---
title: 'Activity (ACTIVE)'
---

The *activity* operator creates a property that determines whether one of the following form elements is active:

-   Property - whether the focus is on the specified [property](Properties.md) on the form.
-   Tab - whether one of the tabs in the specified [tab panel](Form_design.md#tab-broken) is active.
-   Form - determines whether the specified [form](Forms.md) is active for the user.

### Language

To create a property that determines whether a tab is active, use the [**ACTIVE TAB** operator](ACTIVE_TAB_operator.md). Whether a form is active is determined by creating an action using the [**ACTIVE FORM** operator](ACTIVE_FORM_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=activetab"/>


<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=activeform"/>

  
