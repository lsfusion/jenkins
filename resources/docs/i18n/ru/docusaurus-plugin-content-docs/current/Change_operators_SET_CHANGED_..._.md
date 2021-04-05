---
title: 'Операторы изменений (SET, CHANGED, ...)'
---

*Операторы изменений* позволяют определить произошли ли для свойства в текущей сессии те или иные виды изменений. Все эти операторы являются производными от [оператора предыдущего значения (PREV)](Previous_value_PREV_.md), однако для улучшения читабельности и производительности рекомендуется использовать именно их. В следующей таблице приведены поддерживаемые виды изменений и их описание:

|Оператор|Значение|Описание|
|---|---|---|
|<strong>SET</strong>|f AND NOT PREV(f)|Установлено значение|
|<strong>DROPPED</strong>|NOT f AND PREV(f)|Сброшено значение|
|<strong>CHANGED</strong>|(f OR PREV(f)) AND NOT f==PREV(f)|Изменено значение|
|<strong>SETCHANGED</strong>|<p>f AND NOT f==PREV(f)</p><br/><p>или</p><br/><p>CHANGED(f) AND NOT DROPPED(f)</p>|Значение изменено на не <strong>NULL</strong>|
|<strong>DROPCHANGED</strong>|CHANGED(f) AND NOT SET(f)|Значение или сброшено, или изменено с одного не <strong>NULL</strong> на другое не <strong>NULL</strong>|
|<strong>SETDROPPED</strong>|SET(f) OR DROPPED(f)|Значение или сброшено или установлено с <strong>NULL</strong> на не <strong>NULL</strong>|

:::caution
Эти операторы вычисляются по другому внутри обработки [событий](Events.md#change) : в этом случае они возвращают изменения с момента предыдущего возникновения этого события (а точнее, окончания выполнения всех его обработок).
:::

### Язык

Для объявления свойства при помощи операторов изменений, используются следующие [синтаксические конструкции](Change_operators.md). 

### Примеры

```lsf
quantity = DATA NUMERIC[14,2] (OrderDetail);
price = DATA NUMERIC[14,2] (OrderDetail);
sum(OrderDetail d) <- quantity(d) * price(d) WHEN CHANGED(quantity(d)) OR CHANGED(price(d));

createdUser = DATA CustomUser (Order);
createdUser (Order o) <- currentUser() WHEN SET(o IS Order);

numerator = DATA Numerator (Order);
number = DATA STRING[28] (Order);
series = DATA BPSTRING[2] (Order);
WHEN SETCHANGED(numerator(Order o)) AND
     NOT CHANGED(number(o)) AND
     NOT CHANGED(series(o))
     DO {
        number(o) <- curStringValue(numerator(o));
        series(o) <- series(numerator(o));
        incrementValueSession(numerator(o));
     }
;
```
