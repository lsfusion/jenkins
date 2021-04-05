---
title: 'How-to: Interaction via HTTP protocol'
---

## Example 1

### Task

We have a certain set of cities associated with their countries.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseExternal&block=sample1"/>

We need to send an HTTP request for adding a city in the JSON format to a certain url.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseExternal&block=solution1"/>

The [EXPORT](Data_export_EXPORT_.md) operator will create a JSON in the [FILE](Built-in_classes.md) format and then will write it to the exportFile property. Here is an example of the generated file: 

    {"countryId":"123","name":"San Francisco"}

Then we call the [EXTERNAL](Access_to_an_external_system_EXTERNAL_.md) operator, which sends a request to the specified url passing there the contents of the generated file as Body. In this case, since the property in the FROM block has the type JSON, *application/json* will be used as the content type. <namespace\><property name\> is encoded in the url. In this case, the namespace of the action being called (**createCity**) is **Location**. All parameters are passed consequently with the ID **p**.  The response from the server will be written to the **result** property. Suppose that the response is received in the JSON format and has one of the following types:

    {"code":"0","message":"OK"}

    {"code":"1","message":"Invalid country code"}

The response is handled by the [IMPORT](Data_import_IMPORT_.md) operator which parses the corresponding parameters into the **code** and **message** properties respectively. If any error occurs, the user will see a corresponding error message.

## Example 2

### Task

Similar to **Example 1**. 

We need to handle the incoming HTTP request and create a new city in the database with the parameters provided in the request.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseExternal&block=solution2"/>

Since the property is named **createCity** and located in the [module](Modules.md) with the namespace **Location**, the url on which the request will be handled looks like this:

    http://localhost:7651/exec?action=Location.createCity

Body of the HTTP request will be passed as a parameter of the type **FILE**. The values read from the **countryId** and **name** parameters are written to the local properties **cy** and **ne** respectively.

If there is no country with the corresponding code, then a JSON file is generated similar to that described in the previous example, and the [RETURN](Exit_RETURN_.md) operator is called to abort execution. By default, the response message value is also stored in the **exportFile** property.

If all the actions are completed successfully, the corresponding "OK message" is generated in response.

## Example 3

### Task

We have the logic of book orders.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseExternal&block=sample3"/>

We need to send an HTTP request for creating a new order in the JSON format to a certain url.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseExternal&block=solution3"/>

To create a JSON with nested tags, we need to create a form with the corresponding objects linked via the **FILTERS** block of operators. Based on the dependencies between objects, the system will generate a JSON file with the corresponding structure. In the considering example, the output JSON structure will look like this:

    {
       "dt":"20.08.18",
       "nm":"1",
       "detail":[
          {
             "pr":5.99,
             "id":"b1",
             "qn":3
          },
          {
             "pr":6.99,
             "id":"b2",
             "qn":2
          }
       ]
    }

We do not create a custom tag for "order", since the object value is passed as an argument to the **EXPORT** operator.  
In this example, the response to the HTTP request is ignored.

## Example 4

### Task

Similar to **Example 3**. 

We need to handle the incoming HTTP request and create a new order in the database with the parameters provided in the request.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseExternal&block=solution4"/>

To import the corresponding file in the JSON format, we need to create a form of a similar structure, except that the INTEGER type will be used as object classes. During the import process, the tag values will be placed in the properties with the corresponding names. The **date** and **number** properties have no parameters, since their values in JSON are provided at the topmost level.

## Example 5

### Condition

Similar to **Example 4**. 

We need to send an HTTP request to create an order in the JSON format to a certain url as in the previous example, except that everything must be wrapped in the **order** tag.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseExternal&block=solution5"/>

  

Unlike the previous example, here we create a property [group](Groups_of_properties_and_actions.md) named **order** using the [GROUP](GROUP_operator.md) operator. When declaring a form, we put all the properties of the purchase order as well as the "detail" object into this property group. The result JSON will look like this:

    {
       "order":{
          "dt":"20.08.18",
          "nm":"1",
          "detail":[
             {
                "pr":5.99,
                "id":"b1",
                "qn":3
             },
             {
                "pr":6.99,
                "id":"b2",
                "qn":2
             }
          ]
       }
    }

## Example 6

### Condition

Similar to **Example 5**. 

We need to handle the incoming HTTP request and create a new order in the database with the parameters provided in the request.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseExternal&block=solution6"/>

Just as in the export process, we put all the properties and the **detail** object to the "order" group to correctly receive the new version of JSON.

## Example 7

### Task

Similar to **Example 3**. 

We need to return a list of order numbers for a given date using an HTTP GET request in which this date is provided.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseExternal&block=solution7"/>

The url to which the HTTP request should be sent will look like this:   http://localhost:7651/exec?action=Location.getOrdersByDate&p=12.11.2018 .

The response JSON will look like this:

  

    {
        "order": [
            {
                "nm": "42"
            },
            {
                "nm": "65"
            }
        ]
    }
