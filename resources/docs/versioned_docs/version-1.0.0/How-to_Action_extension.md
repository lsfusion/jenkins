---
title: 'How-to: Action extension'
---

We can use the following scheme to implement polymorphism:

### Example 1

Create an abstract class **Shape** with an abstract action **whoAmI**:

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseActionShape&block=shape"/>

Then, create **Square** and **Circle** classes inherited from **Shape** :

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseActionShape&block=concreteclass"/>

Define the implementation of **whoAmI** for the created classes:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseActionShape&block=concreteaction"/>

When executing the **whoAmI** action, all the actions added as an implementation will be called. In this case, a corresponding message will appear depending on the argument.

### Example 2

Suppose that we need to implement an action that copies an object (e. g. the **Book** class) with its semantics defined in multiple modules. This can be implemented as follows:

Declare the **Book** class and the actions to copy it:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseActionBook"/>

In the dependent module **MyBook**, we want to extend the **Book** class with new properties and also define the action to copy them:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseActionMyBook"/>
