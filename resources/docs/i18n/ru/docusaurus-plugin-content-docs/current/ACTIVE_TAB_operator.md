---
title: 'Оператор ACTIVE TAB'
---

Оператор **ACTIVE TAB **- создание [свойства](Properties.md), реализующего проверку [активности](Activity_ACTIVE_.md) закладки.

### Синтаксис 

    ACTIVE TAB formName.componentSelector

### Описание

Оператор **ACTIVE TAB** создает свойство, которое возвращает **TRUE**, если указанная закладка активна на [форме](Forms.md). 

### Параметры

*formName*

Имя формы. [Составной идентификатор](IDs.md#cid-broken).

**componentSelector*  
*

[Селектор](DESIGN_instruction.md) компонента дизайна. Компонент должен быть закладкой на панели вкладок.

### Примеры

  

```lsf
//Форма с двумя закладками
FORM tabbedForm 'Форма с табами'
    OBJECTS u = CustomUser
    PROPERTIES(u) name

    OBJECTS c = Chat
    PROPERTIES(c) name
;

DESIGN tabbedForm {
    NEW tabPane FIRST {
        type = TABBED;
        NEW contacts {
            caption = 'Контакты';
            MOVE BOX(u);
        }
        NEW recent {
            caption = 'Последние';
            MOVE BOX(c);
        }
    }
}

//Активна ли закладка 'Последние'
recentActive() = ACTIVE TAB tabbedForm.recent;
```
