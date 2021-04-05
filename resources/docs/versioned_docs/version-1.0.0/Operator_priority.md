---
title: 'Operator priority'
---

When evaluating an [expression](Expression.md), [operators](Operators.md) are evaluated in a specific order depending on *operator priority.* The higher the operator’s priority, the earlier it will be executed. The table below lists the priorities of all operators in descending order.* *

|Operator|Description|Type|
|---|---|---|
|<p>(expression)</p><br/><p>JOIN</p><br/><p>CASE, MULTI, OVERRIDE, EXCLUSIVE, IF ... THEN</p><br/><p>[PARTITION](PARTITION_operator.md)</p><br/><p>RECURSION</p><br/><p>GROUP</p><br/><p>[STRUCT](STRUCT_operator.md)</p><br/><p>MAX/MIN</p><br/><p>CONCAT</p><br/><p>INTEGER, DOUBLE...</p><br/><p>[PREV](PREV_operator.md), [CHANGED, ...](Change_operators.md)</p><br/><p>CLASS</p><br/><p>ACTIVE</p><br/><p>literal</p>|<p>Expression in parentheses</p><br/><p>[Composition](Composition_JOIN_.md)</p><br/><p>[Selection](Selection_CASE_IF_MULTI_OVERRIDE_EXCLUSIVE_.md)</p><br/><p>[Partition/order](Partitioning_sorting_PARTITION_..._ORDER_.md)</p><br/><p>[Recursion](Recursion_RECURSION_.md)</p><br/><p>[Group](Grouping_GROUP_.md)</p><br/><p>[Structure creation](Structure_operations_STRUCT_.md)</p><br/><p>Maximum/minimum</p><br/><p>String concatenation</p><br/><p>[Type conversion](Type_conversion.md)</p><br/><p><br /><br/></p><br/><p>[Property signature](Property_signature_CLASS_.md)</p><br/><p>[Activity](Activity_ACTIVE_.md)</p><br/><p> [Constants](Constant.md)</p>|<p><br /><br/></p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p><br/><p>Prefix</p>|
|<p>[[ ]](Operator_.md)</p><br/><p>[IS](IS_AS_operators.md)</p><br/><p>[AS](IS_AS_operators.md)</p>|<p>[Structure element access](Structure_operations_STRUCT_.md)</p><br/><p>[Classification](Classification_IS_AS_.md)</p><br/><p>[Classification](Classification_IS_AS_.md)</p>|<p>Postfix</p><br/><p>Postfix</p><br/><p>Postfix</p>|
|[-](Arithmetic_operators.md)|Unary minus|Prefix|
|<p>[*](Arithmetic_operators.md)</p><br/><p>[/](Arithmetic_operators.md)</p>|<p>Multiplication</p><br/><p>Division</p>|<p>Binary</p><br/><p>Binary</p>|
|<p>[+](Arithmetic_operators.md)</p><br/><p>[-](Arithmetic_operators.md)</p>|<p>Addition</p><br/><p>Subtraction</p>|<p>Binary</p><br/><p>Binary</p>|
|<p>[(+)](Arithmetic_operators.md)</p><br/><p>[(-)](Arithmetic_operators.md)</p>|<p>Addition with NULL values</p><br/><p>Subtraction with NULL values</p>|<p>Binary</p><br/><p>Binary</p>|
|<p>[<](Comparison_operators.md)</p><br/><p>[<=](Comparison_operators.md)</p><br/><p>[\>](Comparison_operators.md)</p><br/><p>[\>=](Comparison_operators.md)</p>|<p>Less</p><br/><p>Less or equal</p><br/><p>Greater</p><br/><p>Greater or equal</p>|<p>Binary</p><br/><p>Binary</p><br/><p>Binary</p><br/><p>Binary</p>|
|<p>[==](Comparison_operators.md)</p><br/><p>[!=](Comparison_operators.md)</p>|<p>Equal</p><br/><p>Not equal</p>|<p>Binary</p><br/><p>Binary</p>|
|[NOT](AND_OR_NOT_XOR_operators.md)|[Logical negation](Logical_operators_AND_OR_NOT_XOR_.md)|Prefix|
|[AND](AND_OR_NOT_XOR_operators.md)|[Logical AND](Logical_operators_AND_OR_NOT_XOR_.md)|Binary|
|[XOR](AND_OR_NOT_XOR_operators.md)|[Logical exclusive OR](Logical_operators_AND_OR_NOT_XOR_.md)|Binary|
|[OR](AND_OR_NOT_XOR_operators.md)|[Logical OR](Logical_operators_AND_OR_NOT_XOR_.md)|Binary|
|[IF](IF_operator.md)|Condition|Binary|

* * 
