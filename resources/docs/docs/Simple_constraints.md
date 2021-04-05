---
title: 'Simple constraints'
---

The platform currently supports four types of *simple constraints*: *consequence*, *exception*, *equality*, and *definiteness*. For all simple constraints, an *automatic resolution* mechanism is supported. When this is enabled, instead of throwing an error the platform itself restores system integrity.

|<br /><br/>|Consequence|Exception|Equality|Definiteness|
|---|---|---|---|---|
|Number of properties|2|2...N|2|1|
|Description|If the value of one property (the *premise*) is non-<strong>NULL</strong>, the value of the second property (the *consequence*) must also be non-<strong>NULL</strong>|Only one of the values of the specified properties must be non-<strong>NULL</strong>|If the value of one property is non-<strong>NULL</strong> and the value of the second property is non-<strong>NULL</strong>, they must be equal|If all property parameters match specified classes, a non-<strong>NULL</strong> value must be specified for them|
|Instruction|<strong>[=>](Instruction=_.md)</strong>|Not yet implemented|Not yet implemented|<strong>NONULL</strong> option when defining properties|
|Auto-resolution|<p>Two modes are supported:</p><br/><ul><br/><li><strong>LEFT</strong> - if the premise changes to non-<strong>NULL</strong>, change the * consequence to non-<strong>NULL</strong></li><br/><li><strong>RIGHT</strong> - if the consequence changes to <strong>NULL</strong>, change the premise to <strong>NULL</strong></li><br/></ul>|If one of the properties changes to non-<strong>NULL</strong>, change the rest to <strong>NULL</strong>|If one of the properties changes to a non-<strong>NULL</strong> value, change the second to the same value|<p><strong>AGGR</strong> - if a property value changes to <strong>NULL</strong>, delete objects corresponding to the parameters</p><br/><p><strong>DEFAULT</strong> value - if objects whose classes match the parameters classes are added/reclassified, change the property value for these objects to the default value.</p>|

\* A change to **NULL**/non-**NULL** is the following:

-   For [data](Data_properties_DATA_.md) properties - [input](Property_change_CHANGE_.md) **NULL** or the default value for this property value class.
-   For [membership](Classification_IS_AS_.md) to a class - deleting an object \\\\ adding an object of a class for which belonging to the class is checked.
-   For an [aggregation](Grouping_GROUP_.md) constraint - (only if changing to non-**NULL**) create an object of the base class (**System**.**Object**), change to non-**NULL** conditions of the aggregation constraint, write the values of the corresponding parameters to all grouping values.

Just as for a basic [constraint](Constraints.md), for each simple constraint, a base event must be defined, which determines when the specified constraint will be checked. 

Note that the definiteness constraint is a special case of the consequence, in which the consequence is a property that must be defined, and the premise is its signature (a property obtained from it using the corresponding [operator](Property_signature_CLASS_.md)).

There is also an implicit fifth kind of simple constraint, *uniqueness*, but it is combined with an aggregation operator (which returns this most unique value), and therefore it is not considered here. Together with consequences, this type of constraint allows to implement the [aggregation](Aggregations.md) mechanism. 

### Examples

```lsf
is(Sku s) = s IS Sku;
// the product must have a barcode and name specified
is(Sku s) => barcode(s);
is(Sku s) => name(s);


CLASS Invoice;
CLASS InvoiceLine;
invoice = DATA Invoice (InvoiceLine);
is(InvoiceLine l) = l IS InvoiceLine;
// for a document line, a document must be specified, and when deleting a document, the lines of this document should be deleted
is(InvoiceLine l) => invoice(l) RESOLVE RIGHT;
// is equivalent to declaring document = DATA Invoice (InvoiceLine) NONULL DELETE;

// aggregation for f(a, b) create an object of class x, whose property a(x) equals a, and property b(x) equals b
CLASS A;
CLASS B;
f = DATA BOOLEAN (A, B);

CLASS X;
a = DATA A(X);
b = DATA B(X);
is (X x) = x IS X;

f(a,b) => [ GROUP AGGR X x WHERE x IS X BY a(x), b(x)](a,b) RESOLVE LEFT;
is(X x) => f(a(x), b(x)) RESOLVE RIGHT;
```
