---
title: 'Exception handling (TRY)'
---

The *exception handling* operator enables you to execute an action and do the following if an error (exception) occurs:

-   ignore this error, 
-   execute a different action, then pass the error to the top action (as if the operation didn't exist),
-   execute a different action regardless of whether there's been an error or not, then pass the error to the top action (as if this operator didn't exist)

### Language

To declare an exception handling action, use the [**TRY** operator](TRY_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=try"/>

  
  
