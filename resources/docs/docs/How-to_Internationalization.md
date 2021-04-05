---
title: 'How-to: Internationalization'
---

The system supports  [internationalization](Internationalization.md) by passing to the client values localized according to the language chosen by the user. All values in braces are localized. Let's assume that we need to implement a simple form with a list of books and their prices that we can create/delete.

We will declare logic in the usual way, but instead of specifying names explicitly, we will be using IDs.

```lsf
CLASS Book '{use.case.i18n.book}';

name '{use.case.i18n.book.name}' = DATA STRING[40] (Book);
price '{use.case.i18n.book.price}' = DATA STRING[40] (Book);

FORM books '{use.case.i18n.books}'
    OBJECTS b = Book
    PROPERTIES(b) name, price, NEW, DELETE
;

NAVIGATOR {
    NEW books;
}
```

We will then create a new Resource Bundle and will save our Id values in different languages to it:

![](attachments/60555378/60555382.png)

Let's call it **I18nResourceBundle** (the system will only read files ending in **ResourceBundle**) and choose the languages to be used:

![](attachments/60555378/60555383.png)

After that, we define values for each Id in each of the selected languages:

![](attachments/60555378/60555384.png)

Upon execution, all values, by default, will be shown in the server language (which, in turn, is defined by the Os language by default):

![](attachments/60555378/60555385.png)

You can change the language and country settings for a particular user in their profile:

![](attachments/60555378/60555386.png)

In this case, when the user logs in to the system again, everything will be shown in the selected language:

![](attachments/60555378/60555387.png)

Ids can also be used in expressions:

```lsf
description '{use.case.i18n.book.description}' (Book b) = STRING[60] (name(b) + ', {use.case.i18n.book.price} : ' + price(b));
EXTEND FORM books
    PROPERTIES(b) READONLY description
;
```

Remember that such expressions cannot be used in [materializations](Materializations.md). 

The system cannot automatically localize user data. The developer is responsible for this.
