---
title: 'How-to: Numbering'
---

Let's suppose we have a set of books. For each of these books, we define a number as an integer.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNumerating&block=numbermaster"/>

We implement a property that will find a book by its number. It can be useful, for example, for importing data where each book is identified by a number. It can be used to get a link to a book object by getting its number as a parameter.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNumerating&block=numberaggr"/>

The [GROUP AGGR](Grouping_GROUP_.md) operator automatically adds a constraint on the uniqueness of the number. If you try to save the same number to the database, you will get an error message.

Let's add an [event](Events.md) that will automatically number books by increasing the maximum number existing in the database.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNumerating&block=numbergenerate"/>

The event will be triggered at the moment of saving a book to the database in the same transaction.

In some situations, you may need to apply different numbering for the same object. For this purpose, you can add a special **Numerator** class.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNumerating&block=numerator"/>

The **value** property will store the current value of the numerator that will be written to the number of the necessary object. To achieve this, a reference to a particular numerator is set for an object (for example, an order). If such a reference is specified at the time of object creation, you need to automatically assign the numerator's current value increased by one to the order number.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNumerating&block=numeratororder"/>

The event conditions check if the number has been changed in order to avoid changing it if the user specified it manually (or if it was assigned during import).

An important difference between the numerator and "assigning the maximum value plus one" is the support of the simultaneous object creation. In this case, if two users simultaneously create objects, the last started saving user will get a message about number duplication and will need to manually re-save it. Changes made in all events in this way will be "rolled back" and re-saving will generate a new number. If you use a numerator, the last user's transaction will get a CONFLICT UPDATE on the **value** field for the numerator (since both transactions change the field of the same row in the database). The system will then automatically roll back the transaction and all the changes made in the event and will start processing it again without the user's involvement. This way, the user will only experience slower data saving (up two times slower), but no additional actions will be required.

You can define a default numerator with property without arguments so that the user does not have to select a numerator every time. After that, you can create an event that will automatically set the numerator if the user doesn't choose it manually.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNumerating&block=numeratororderdefault"/>
