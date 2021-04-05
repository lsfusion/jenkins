---
title: 'Read file (READ)'
---

The *read* *file *operator creates an [action](Actions.md) that reads a file from a defined source and [writes](Property_change_CHANGE_.md) this file to the specified local [data](Data_properties_DATA_.md) property without parameters.

The source is defined as a [property](Properties.md) which values are instances of [string classes](Built-in_classes.md). The following types of data sources (URLs) are supported: FILE, HTTP, HTTPS, FTP, SFTP, JDBC, MDB.

### Language

To declare an action that reads a file, use the [**READ** operator](READ_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=read"/>
