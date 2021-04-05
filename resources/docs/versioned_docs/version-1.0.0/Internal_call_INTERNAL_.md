---
title: 'Internal call (INTERNAL)'
---

The *internal call* operator allows to create actions in programming languages other than the lsFusion language.** **The platform currently supports external actions only in the **Java** language.

This operator also makes it possible to directly specify the source for an action in Java, as well as the name of a Java class for which the bytecode is located in a separate Java file. In the second case it is assumed that the file is within the classpath of the server's virtual machine; the Java class itself must inherit from **lsfusion.server.physics.dev.integration.internal.to.InternalAction**. The class's **executeInternal(lsfusion.server.logics.action.controller.context.ExecutionContext context)** method will be executed.

This operator allows to specify which classes the action created can take, and whether or not it can take **NULL** values. If the specified conditions are not met, the created action is not executed: control simply passes to the next action.

### Language

To declare an action implemented in **Java** use the  [**INTERNAL** operator](INTERNAL_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=custom"/>
