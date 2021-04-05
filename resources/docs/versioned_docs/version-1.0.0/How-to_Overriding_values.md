---
title: 'How-to: Overriding values'
---

Often there are tasks when it is necessary to give the user opportunity to enter the value of some attribute for some general object and then override it for some specific object.

Let's take a look when you need to define a trade mark-up for a book. At the same time, we have the logic of categories defined. These categories form a tree by specifying a parent for each category. The user should be able to set a mark-up for any product and category at any level.

Let's define the logic of categories and the [data property](Data_properties_DATA_.md) of this category's markup.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseOverride&block=block1"/>

The [RECURSION](RECURSION_operator.md) operator is used to calculate the *level* property for given two categories. This property will be equal to two to the power of N, where N is the distance between these categories.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseOverride&block=block2"/>

Let's create a property that will determine the corresponding parent by category and level.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseOverride&block=block3"/>

Let's find the minimal level of a category for which the a mark-up is defined. It will also be the level of "closest upper" category with a set mark-up.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseOverride&block=block4"/>

We use this level to determine the category and its mark-up.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseOverride&block=block5"/>

Thus, the **overMarkup** property will contain the required markup value for this category with its hierarchy taken into account.

Let's now define the logic for books. Each of them is associated with a certain category that may be located at any level of the category hierarchy.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseOverride&block=block6"/>

Let's define the data property of a product markup. After that, let's construct an overridden property that will return a product markup if it's not **NULL** and a previously created property with a category markup.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseOverride&block=block7"/>

Finally, let's design a form that will allow the user to enter the markup for categories and products at the same time. Let's output both the data and the overridden markup for the category and the product. Note that changes in overridden properties on the form will be displayed immediately, but saved only when the corresponding button is clicked.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseOverride&block=block7"/>

As a result, the form with the filled data will look like this:

![](attachments/46367603/46367612.png)
