---
title: 'How-to: Inheritance and aggregation'
---

In order to demonstrate the principles of object inheritance and aggregation, let's implement the logic of creating batches based on receipts and production documents. Let's make it so that each new document with a Posted property will automatically generate exactly one new product batch.

Let's update our logic with the notion of a product whose batches will be accounted for:

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInheritance&block=item"/>

Let's create a **Receipt** [class](User_classes.md) with objects that will indicate the receipt of products:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInheritance&block=receipt"/>

For the purposes of this example, let's use a simplified scheme with a single class. In reality, you would be using two classes: **Receipt** (for documents) and **ReceiptDetail **(for document lines).

In a similar way, let's create a Production class to be used for manufactured products:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInheritance&block=production"/>

So far, we've been only creating regular classes without any inheritance. To implement the batch logic, let's create an abstract class called **Batch**:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInheritance&block=batch"/>

Each object of this class will correspond to one batch of a particular product. All of its [properties](Properties.md) will be declared abstract — that is, their implementation will differ depending on the class of a particular batch.

You cannot directly create objects of the abstract **Batch** class in the system. To do that, you need to declare specific classes that will be inherited from it. In particular, let's create a class for batches formed from the receipt of products:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInheritance&block=receiptbatch"/>

Use the [AGGR](AGGR_operator.md) operator for each object of the **Receipt**,  class with a defined **posted** property to automatically create (and delete) an object of the **ReceiptBatch** class. At this time, the system creates two properties with reciprocal object links: **batch(Receipt r)** and **receipt(ReceiptBatch b)**.

Now we need to inherit the **ReceiptBatch** class from **Batch** to make sure that all batches created by the receipt document also become objects of the abstract class (that is, previously declared batches):

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInheritance&block=receiptbatchextend"/>

Inheritance is implemented with the help of the [EXTEND CLASS](EXTEND_CLASS_instruction.md) operator. After that, for each abstract property of **Batch**, we define how exactly it should be calculated for a specific **ReceiptBatch** class. Date and product values are retrieved from the receipt document through the **receipt(ReceiptBatch b)** link. The necessary string is substituted into the batch type under the condition that the object belongs to the right class (otherwise, the expression will be defined for objects of any class, and the system will generate a signature mismatch error).

Note that you could inherit a class directly while declaring the **ReceiptBatch** class.

In a similar fashion, let's create batches for manufacturing documents:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInheritance&block=productionbatch"/>

If necessary, you can create a class for manual batch entry by the user:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInheritance&block=userbatch"/>

  
