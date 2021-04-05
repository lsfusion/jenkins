---
title: 'How-to: Расширение форм'
---

Предположим, что существует модуль, в котором описывается форма **Sku**, которая является формой для редактирования Sku :

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseFormSku"/>

Необходимо реализовать функционал по добавлению к Sku множества штрих-кодов. Это можно реализовать, создав новый модуль, который введет новый класс **Barcode** и расширит форму редактирования Sku возможностью ввода штрих-кодов :

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=UseCaseFormBarcode"/>

Следует отметить, что модуль **Barcode** рассчитывает на то, что существует форма **Sku**, и в ней есть объект **s** и контейнер с именем **skuDetails**. Если форма по какой-либо причине изменится, то модуль **Barcode** станет неработоспособным.
