---
title: 'Change operators'
---

Change operators - a set of operators that determine various types of [property value changes](Change_operators_SET_CHANGED_..._.md). 

### Syntax

    typeChange(propExpr)

### Description

Change operators create [actions](Properties.md) which determine whether some types of changes have been made for a certain property in the current session.

### Parameters

*typeChange*

Type of the change operator. It is specified by one of the keywords:

-   **SET**
-   **CHANGED**
-   **DROPPED**
-   **SETCHANGED**
-   **DROPCHANGED**
-   **SETDROPPED**

*propExpr*

An [expression](Expression.md) which value defines the property that should be checked for the presence of a change.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=changed"/>

**  
**
