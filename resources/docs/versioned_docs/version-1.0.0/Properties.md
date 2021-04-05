---
title: 'Properties: Overview'
sidebar_label: Overview
---

A *property* is an element of the system that takes a set of objects (*parameters*) and returns exactly one object (the *return value*). 

The type and the specifics of how to calculate each property are determined by the operator used to create the property.

### Type constraint {#type}

Due to implementation features, all non-**NULL** property values returned must be of the same type. That is, a property cannot return, for example, a string for one set of parameters and a number for another.

The same constraint exists for each property parameter: a property cannot have a non-**NULL** value for an object collection in which, for example, the first parameter is a string, and at the same time have a non-**NULL** value for another object collection, in which the first parameter is an object.

### Language

To create properties, use [the **=** instruction](Instruction_=.md). 

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=PropertySample"/>
