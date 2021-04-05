---
title: 'Чтение файла (READ)'
---

Оператор *чтения файла*, создает [действие](Actions.md), которое читает файл из заданного источника и [записывает](Property_change_CHANGE_.md) этот файл в заданное локальное [первичное](Data_properties_DATA_.md) свойства без параметров.

Источник задается как некоторое [свойство](Properties.md), значения которого являются экземплярами [строковых классов](Built-in_classes.md). Поддерживаются следующие типы источников данных (URL): FILE, HTTP, HTTPS, FTP, SFTP, JDBC, MDB.

### Язык

Для объявления действия, выполняющего чтение файла, используется [оператор **READ**](READ_operator.md).

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=read"/>
