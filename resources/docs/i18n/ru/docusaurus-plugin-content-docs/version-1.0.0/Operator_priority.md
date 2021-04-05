---
title: 'Приоритет операторов'
---

При вычислении [выражения](Expression.md) [операторы](Operators.md) вычисляются в определенном порядке в зависимости от *приоритета операторов.* Чем выше приоритет оператора, тем раньше он будет выполнен. В таблице ниже перечислены приоритеты всех операторов в порядке убывания.* *

|Оператор|Описание|Тип|
|---|---|---|
|<p>(expression)</p><br/><p>JOIN</p><br/><p>CASE, MULTI, OVERRIDE, EXCLUSIVE, IF ... THEN</p><br/><p>[PARTITION](PARTITION_operator.md)</p><br/><p>RECURSION</p><br/><p>GROUP</p><br/><p>[STRUCT](STRUCT_operator.md)</p><br/><p>MAX/MIN</p><br/><p>CONCAT</p><br/><p>INTEGER, DOUBLE...</p><br/><p>[PREV](PREV_operator.md), [CHANGED, ...](Change_operators.md)</p><br/><p>CLASS</p><br/><p>ACTIVE</p><br/><p>literal</p>|<p>Выражение в круглых скобках</p><br/><p>[Композиция](Composition_JOIN_.md)</p><br/><p>[Выбор](Selection_CASE_IF_MULTI_OVERRIDE_EXCLUSIVE_.md)</p><br/><p>[Разбиение / упорядочивание](Partitioning_sorting_PARTITION_..._ORDER_.md)</p><br/><p>[Рекурсия](Recursion_RECURSION_.md)</p><br/><p>[Группировка](Grouping_GROUP_.md)</p><br/><p>[Создание структуры](Structure_operations_STRUCT_.md)</p><br/><p>Максимум / минимум</p><br/><p>Объединение строк</p><br/><p>[Преобразование типа](Type_conversion.md)</p><br/><p> </p><br/><p>[Сигнатура свойства](Property_signature_CLASS_.md)</p><br/><p>[Активность](Activity_ACTIVE_.md)</p><br/><p> [Константы](Constant.md)</p>|<p> </p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p><br/><p>Префиксный</p>|
|<p>[[ ]](Operator_.md)</p><br/><p>[IS](IS_AS_operators.md)</p><br/><p>[AS](IS_AS_operators.md)</p>|<p>[Обращение к элементу структуры](Structure_operations_STRUCT_.md)</p><br/><p>[Классификация](Classification_IS_AS_.md)</p><br/><p>[Классификация](Classification_IS_AS_.md)</p>|<p>Постфиксный</p><br/><p>Постфиксный</p><br/><p>Постфиксный</p>|
|[-](Arithmetic_operators.md)|Унарный минус|Префиксный|
|<p>[*](Arithmetic_operators.md)</p><br/><p>[/](Arithmetic_operators.md)</p>|<p>Умножение</p><br/><p>Деление</p>|<p>Бинарный</p><br/><p>Бинарный</p>|
|<p>[+](Arithmetic_operators.md)</p><br/><p>[-](Arithmetic_operators.md)</p>|<p>Сложение</p><br/><p>Вычитание</p>|<p>Бинарный</p><br/><p>Бинарный</p>|
|<p>[(+)](Arithmetic_operators.md)</p><br/><p>[(-)](Arithmetic_operators.md)</p>|<p>Сложение с учетом NULL</p><br/><p>Вычитание с учетом NULL</p>|<p>Бинарный</p><br/><p>Бинарный</p>|
|<p>[<](Comparison_operators.md)</p><br/><p>[<=](Comparison_operators.md)</p><br/><p>[\>](Comparison_operators.md)</p><br/><p>[\>=](Comparison_operators.md)</p>|<p>Меньше</p><br/><p>Меньше или равно</p><br/><p>Больше</p><br/><p>Больше или равно</p>|<p>Бинарный</p><br/><p>Бинарный</p><br/><p>Бинарный</p><br/><p>Бинарный</p>|
|<p>[==](Comparison_operators.md)</p><br/><p>[!=](Comparison_operators.md)</p>|<p>Равно</p><br/><p>Не равно</p>|<p>Бинарный</p><br/><p>Бинарный</p>|
|[NOT](AND_OR_NOT_XOR_operators.md)|[Логическое отрицание](Logical_operators_AND_OR_NOT_XOR_.md)|Префиксный|
|[AND](AND_OR_NOT_XOR_operators.md)|[Логическое И](Logical_operators_AND_OR_NOT_XOR_.md)|Бинарный|
|[XOR](AND_OR_NOT_XOR_operators.md)|[Логическое исключающее ИЛИ](Logical_operators_AND_OR_NOT_XOR_.md)|Бинарный|
|[OR](AND_OR_NOT_XOR_operators.md)|[Логическое ИЛИ](Logical_operators_AND_OR_NOT_XOR_.md)|Бинарный|
|[IF](IF_operator.md)|Условие|Бинарный|

* * 
