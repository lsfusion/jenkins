---
title: 'How-to: Registers'
---

### Accumulation ledger

Let's assume we need to implement the logic for calculating the SKU balances.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseLedger&block=skumaster"/>

Theoretically we could just create a [property](Properties.md) that would sum up all incomings and subtract all outgoings, with all operations set explicitly. The weakness of this approach is that whenever a new operation is added, it needs to be added to the formula for calculating the balance. In addition, it will be difficult to build a form with a list of all the operations that can affect the balance for a specific SKU and warehouse. [Modularity](Modularity.md) will also be violated, because the module in which the balance property is declared will depend on all modules containing operations that affect it.

To give the system efficient [extensibility](Extensions.md), it is best to implement this kind of functionality using *ledgers*. To do this, we introduce an abstract class **SKULedger**. One instance of the class will reflect a single change in the balance by a given amount (positive or negative) for one SKU in one warehouse. Abstract properties are set for it, which need to be defined when the class is implemented.


:::note
All ledgers can have an arbitrary number and type of measurements. In this example they are the SKU and the Warehouse.
:::

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseLedger&block=skuledger"/>

The current balance and the balance for a certain time period are calculated only from the properties of the **SKULedger** class without reference to specific operations. This code can and must be declared in a separate module. Modules containing specific operations will use and extend this class.

For example, let's look at one operation: *Stock receipt.*

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseLedger&block=skureceipt"/>

To "post" it into the ledger, we need to [extend the class](Class_extension.md) **SKULedger** with a **ReceiptDetail** class for stock receipt. We also need to [extend the properties](Property_extension.md) of the ledger.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseLedger&block=skureceiptimplement"/>

Let's look at a more complex case, when we have a document recording transfer from one warehouse to another.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseLedger&block=skutransfer"/>

In this case, the data from the document must be "posted" into the ledger twice. By analogy with stock receipt, we will post the line into the ledger as an outgoing operation with a negative value.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseLedger&block=skutransferimplement"/>

To post it into the ledger for the warehouse where the SKUs are being transferred to, we use object [aggregation](Aggregations.md). The line in the transfer document will generate an object, which in turn will be "posted" into the ledger.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseLedger&block=skutransferaggregation"/>

The ledger object will only be created when the transfer document has been posted. Therefore, in this case the **posted** property will always equal **TRUE**.

It should be noted that documents with one warehouse can also be posted into the ledger using aggregation. The aggregation scheme is more flexible but requires the creation of additional objects in the system, which may be worse from a performance perspective.

### Information ledger

The *information ledger* technique makes it possible to implement the logic of changing a certain indicator over time in a flexible way. Unlike the inventory ledger, it calculates not the sum of an indicator but its latest value over a certain period of time.

To implement this technique we introduce an abstract class **PriceLedger**. Its instance reflects a single price change for one SKU and one warehouse at a certain time.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseLedger&block=priceledger"/>

As a result, we get properties giving the price by SKU and warehouse for the date/time, the latest price, and also the latest price for that SKU for all warehouses.

Documents are posted into the information ledger the same way they are posted into the inventory ledger.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseLedger&block=pricereceiptimplement"/>

In this case the signature of the abstract property needs to be specified explicitly, because there can be several of them with the same name and namespace (properties are named in just the same way for class **SKULedger**).
