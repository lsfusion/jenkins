---
title: 'Module header'
---

Each [module](Modules.md) begins with a *header.*

## Syntax

    MODULE name;
    [REQUIRE moduleName1, ..., moduleNameN;]
    [PRIORITY namespaceName1, ..., namespaceNameM;]
    [NAMESPACE namespaceName;]

## Description

The module header can consist of four special instructions, in the following order:

The **MODULE**  instruction defines the module name. It is required. Each module within one [project](Projects.md) must have a unique name.

The **REQUIRE** instruction defines the list of modules on which the current module [depends](Modules.md#depends). If the **REQUIRE** instruction is absent, that is equivalent to depending only on the **System** module.

The **PRIORITY** instruction defines the list of additional [namespaces](Naming.md#namespace) that will have priority in [finding](Search.md) [system elements](Element_identification.md).

The **NAMESPACE** instruction defines the module's namespace.  

**  
**

## Parameters

*name*

The name of the module. [Simple ID](IDs.md#id-broken). Module names cannot contain an underscore.

*moduleName1, ..., moduleNameN*

A list of the names of modules that the current module depends on. Each name is a simple ID. 

*namespaceName1, ..., namespaceNameM*

A list of namespace names. Each name is a simple ID. 

*namespaceName*

The name of the module namespace. A simple ID that cannot contain an underscore. If the **NAMESPACE** instruction is not used in the header, the name of the current module's namespace will be equal to the name of the module. ** **

### **Examples**

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ModuleSample"/>
