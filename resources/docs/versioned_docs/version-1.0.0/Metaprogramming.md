---
title: 'Metaprogramming'
---

*Metaprogramming* is a type of programming associated with writing software code that results in the generation of more software code. Metaprogramming is used for code reusability and to speed up development.  

### Metacode {#metacode}

In l**sFusion **the metaprogramming tool used is *metacode*, which is described by the [**META** instruction](META_instruction.md). Metacode consists of a header and an **lsFusion** code block describing the [instruction](Instructions.md) sequence.** **This code block must end with the keyword **END**. Let us consider an example of metacode that allows you to add two [actions](Actions.md) to an arbitrary [form](Forms.md):

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=MetaSample&block=definemeta1"/>

The first line of the example contains the metacode header. It consists of the keyword **META**, metacode name, and parameter list. In this example, the metacode **addActions** has one parameter: **formName**. This is the name of the form to which the actions will be added. Let's consider the possible uses for this metacode, which are described by the [instruction @](Instruction_.md). 

<CodeSample url="https://documentation.lsfusion.org/sample?file=MetaSample&block=implementmeta1"/>

The instruction to use metacode starts with the special symbol @, followed by the name of the metacode and the parameters passed. When generating the code, each metacode parameter is replaced by the value passed as a parameter of the @ instruction in all places where the metacode parameter is used. In this example, the metacode parameter **formName** will be replaced with **documentForm** and **orderForm**. The above metacode uses generate the following code block:

<CodeSample url="https://documentation.lsfusion.org/sample?file=MetaSampleResult&block=resultmeta1"/>

### Lexeme concatenation  {#concat}

Simply substituting an ID for a metacode parameter is often not enough. For example, when creating a large number of new [system elements](Element_identification.md) inside the metacode, you must be able to specify these new names. Passing all the names as metacode parameters can be inconvenient. For this reason the metacode contains the special operation \#\#, which operates at the [tokens](Tokens.md) level. This operation can concatenate two adjacent lexemes into one. If one of the concatenated lexemes is a [string literal](Literals.md#strliteral-broken), the concatenation will result in a single string literal.

<CodeSample url="https://documentation.lsfusion.org/sample?file=MetaSample&block=definemeta2"/>

Using the metacode **objectProperties** produces the following code:

<CodeSample url="https://documentation.lsfusion.org/sample?file=MetaSampleResult&block=resultmeta2"/>

There is also the special operation \#\#\#. It is equivalent to operation \#\#, except that in the second of the concatenated literals, the first character, if a letter, is converted to uppercase.

### Examples

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=meta"/>
