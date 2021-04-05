---
title: 'Instruction@'
---

The @ instruction uses [metacode](Metaprogramming.md#metacode).

### Syntax

    @name(param1, ..., paramN);

### Description

The @ instruction generates code obtained from the metacode with the name **name**, replacing metacode parameters with the values of its own parameters and executing the special [\#\# and \#\#\#](Metaprogramming.md#concat) instructions. 

### Parameters 

*name*

The name of the metacode used. [Composite ID](IDs.md#cid-broken).  

*param1, ..., paramN*

The list of instruction parameters that will be substituted for the parameters of the metacode used. The parameters can be a [composite ID](IDs.md#cid-broken), a [class ID](IDs.md#classid-broken), a [literal](Literals.md) or the *empty parameter* when nothing is passed as a parameter.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=implementmeta"/>

  
