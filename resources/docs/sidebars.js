module.exports = {
  docs: [
    {
      type: 'category',
      label: 'Install',
      items: [
        'Install', 
        {
          type: 'category',
          label: 'Automatic installation',
          items: [
            'Automatic_installation', 
            'Development_auto_', 
            'Execution_auto_', 
          ]
        },
        {
          type: 'category',
          label: 'Manual installation',
          items: [
            'Manual_installation', 
            'Development_manual_', 
            'Execution_manual_', 
          ]
        },
      ]
    },
    {
      type: 'category',
      label: 'Learn',
      items: [
        'Learn', 
        {
          type: 'category',
          label: 'Paradigm',
          items: [
            'Paradigm', 
            {
              type: 'category',
              label: 'Logical model',
              items: [
                'Logical_model', 
                {
                  type: 'category',
                  label: 'Domain logic',
                  items: [
                    'Domain_logic', 
                    {
                      type: 'category',
                      label: 'Properties',
                      items: [
                        'Properties', 
                        {
                          type: 'category',
                          label: 'Operators',
                          items: [
                            'Operators', 
                            'Data_properties_DATA_', 
                            'Composition_JOIN_', 
                            'Constant', 
                            {
                              type: 'category',
                              label: 'Primitive operations',
                              items: [
                                'Primitive_operations', 
                                'Arithmetic_operators_+_-_..._', 
                                'Logical_operators_AND_OR_NOT_XOR_', 
                                'Comparison_operators_=_..._', 
                                'Extremum_MAX_MIN_', 
                                'String_operators_+_CONCAT_SUBSTRING_', 
                                'Structure_operations_STRUCT_', 
                                'Type_conversion', 
                              ]
                            },
                            {
                              type: 'category',
                              label: 'Class operations',
                              items: [
                                'Class_operations', 
                                'Classification_IS_AS_', 
                                'Property_signature_CLASS_', 
                              ]
                            },
                            'Selection_CASE_IF_MULTI_OVERRIDE_EXCLUSIVE_', 
                            {
                              type: 'category',
                              label: 'Set operations',
                              items: [
                                'Set_operations', 
                                'Grouping_GROUP_', 
                                'Partitioning_sorting_PARTITION_..._ORDER_', 
                                'Recursion_RECURSION_', 
                                'Distribution_UNGROUP_', 
                              ]
                            },
                          ]
                        },
                      ]
                    },
                    {
                      type: 'category',
                      label: 'Actions',
                      items: [
                        'Actions', 
                        'Change_sessions', 
                        {
                          type: 'category',
                          label: 'Оperators',
                          items: [
                            'Оperators', 
                            {
                              type: 'category',
                              label: 'Execution order',
                              items: [
                                'Execution_order', 
                                'Sequence_..._', 
                                'Call_EXEC_', 
                                'Loop_FOR_', 
                                'Branching_CASE_IF_MULTI_', 
                                'Recursive_loop_WHILE_', 
                                'Interruption_BREAK_', 
                                'Exit_RETURN_', 
                                'New_threads_NEWTHREAD_NEWEXECUTOR_', 
                                'Exception_handling_TRY_', 
                              ]
                            },
                            {
                              type: 'category',
                              label: 'State change',
                              items: [
                                'State_change', 
                                'Property_change_CHANGE_', 
                                'New_object_NEW_', 
                                'Class_change_CHANGECLASS_DELETE_', 
                              ]
                            },
                            {
                              type: 'category',
                              label: 'Session management',
                              items: [
                                'Session_management', 
                                'Apply_changes_APPLY_', 
                                'Cancel_changes_CANCEL_', 
                                'New_session_NEWSESSION_NESTEDSESSION_', 
                                'Previous_value_PREV_', 
                                'Change_operators_SET_CHANGED_..._', 
                              ]
                            },
                            {
                              type: 'category',
                              label: 'User/IS interaction',
                              items: [
                                'User_IS_interaction', 
                                'Show_message_MESSAGE_ASK_', 
                                {
                                  type: 'category',
                                  label: 'File operators',
                                  items: [
                                    'File_operators', 
                                    'Read_file_READ_', 
                                    'Write_file_WRITE_', 
                                    'Data_import_IMPORT_', 
                                    'Data_export_EXPORT_', 
                                  ]
                                },
                                'Send_mail_EMAIL_', 
                              ]
                            },
                          ]
                        },
                      ]
                    },
                    {
                      type: 'category',
                      label: 'Events',
                      items: [
                        'Events', 
                        'Simple_event', 
                        'Calculated_events', 
                      ]
                    },
                    {
                      type: 'category',
                      label: 'Constraints',
                      items: [
                        'Constraints', 
                        'Simple_constraints', 
                        {
                          type: 'category',
                          label: 'Classes',
                          items: [
                            'Classes', 
                            'Built-in_classes', 
                            'User_classes', 
                            'Static_objects', 
                          ]
                        },
                        'Aggregations', 
                      ]
                    },
                  ]
                },
                {
                  type: 'category',
                  label: 'View logic',
                  items: [
                    'View_logic', 
                    {
                      type: 'category',
                      label: 'Forms',
                      items: [
                        'Forms', 
                        {
                          type: 'category',
                          label: 'Form structure',
                          items: [
                            'Form_structure', 
                            'Groups_of_properties_and_actions', 
                          ]
                        },
                        {
                          type: 'category',
                          label: 'Form views',
                          items: [
                            'Form_views', 
                            {
                              type: 'category',
                              label: 'Interactive view',
                              items: [
                                'Interactive_view', 
                                'Form_design', 
                                'Form_events', 
                                {
                                  type: 'category',
                                  label: 'Form operators',
                                  items: [
                                    'Form_operators', 
                                    {
                                      type: 'category',
                                      label: 'Value input',
                                      items: [
                                        'Value_input', 
                                        'Primitive_input_INPUT_', 
                                        'Value_request_REQUEST_', 
                                      ]
                                    },
                                    {
                                      type: 'category',
                                      label: 'Object group operations',
                                      items: [
                                        'Object_group_operations', 
                                        'Search_SEEK_', 
                                        'Filter_FILTER_', 
                                        'Order_ORDER_', 
                                        'View_VIEW_', 
                                      ]
                                    },
                                    {
                                      type: 'category',
                                      label: 'Focus operations',
                                      items: [
                                        'Focus_operations', 
                                        'Activation_ACTIVATE_', 
                                        'Activity_ACTIVE_', 
                                      ]
                                    },
                                  ]
                                },
                              ]
                            },
                            {
                              type: 'category',
                              label: 'Static view',
                              items: [
                                'Static_view', 
                                {
                                  type: 'category',
                                  label: 'Print view',
                                  items: [
                                    'Print_view', 
                                    'Report_design', 
                                  ]
                                },
                                'Structured_view', 
                              ]
                            },
                          ]
                        },
                        {
                          type: 'category',
                          label: 'Open form',
                          items: [
                            'Open_form', 
                            'In_an_interactive_view_SHOW_DIALOG_', 
                            'In_a_print_view_PRINT_', 
                            'In_a_structured_view_EXPORT_IMPORT_', 
                          ]
                        },
                      ]
                    },
                    {
                      type: 'category',
                      label: 'Navigator',
                      items: [
                        'Navigator', 
                        'Navigator_design', 
                      ]
                    },
                  ]
                },
              ]
            },
            {
              type: 'category',
              label: 'Physical model',
              items: [
                'Physical_model', 
                {
                  type: 'category',
                  label: 'Development',
                  items: [
                    'Development', 
                    {
                      type: 'category',
                      label: 'Element identification',
                      items: [
                        'Element_identification', 
                        'Naming', 
                        'Search', 
                      ]
                    },
                    {
                      type: 'category',
                      label: 'Modularity',
                      items: [
                        'Modularity', 
                        'Modules', 
                        'Projects', 
                        {
                          type: 'category',
                          label: 'Extensions',
                          items: [
                            'Extensions', 
                            'Class_extension', 
                            'Property_extension', 
                            'Action_extension', 
                            'Form_extension', 
                          ]
                        },
                      ]
                    },
                    'Metaprogramming', 
                    {
                      type: 'category',
                      label: 'Integration',
                      items: [
                        'Integration', 
                        'Access_from_an_external_system', 
                        'Access_from_an_internal_system', 
                        'Access_to_an_external_system_EXTERNAL_', 
                        {
                          type: 'category',
                          label: 'Access to an internal system (INTERNAL, FORMULA)',
                          items: [
                            'Access_to_an_internal_system_INTERNAL_FORMULA_', 
                            'Internal_call_INTERNAL_', 
                            'Custom_formula_FORMULA_', 
                          ]
                        },
                        'Eval_EVAL_', 
                      ]
                    },
                    'Migration', 
                    'Internationalization', 
                  ]
                },
                {
                  type: 'category',
                  label: 'Execution',
                  items: [
                    'Execution', 
                    'Materializations', 
                    'Indexes', 
                    'Tables', 
                  ]
                },
                {
                  type: 'category',
                  label: 'Management',
                  items: [
                    'Management', 
                    {
                      type: 'category',
                      label: 'System parameters',
                      items: [
                        'System_parameters', 
                        {
                          type: 'category',
                          label: 'Launch parameters',
                          items: [
                            'Launch_parameters', 
                            'Launch_events', 
                          ]
                        },
                        'Working_parameters', 
                      ]
                    },
                    'User_interface', 
                    'Interpreter', 
                    'Security_policy', 
                    'Process_monitor', 
                    'Scheduler', 
                    'Journals_and_logs', 
                    'Profiler', 
                    'Chat', 
                  ]
                },
              ]
            },
          ]
        },
        {
          type: 'category',
          label: 'Language',
          items: [
            'Language', 
            'Tokens', 
            'IDs', 
            'Literals', 
            {
              type: 'category',
              label: 'Instructions',
              items: [
                'Instructions', 
                'Module_header', 
                {
                  type: 'category',
                  label: 'Instruction =',
                  items: [
                    'Instruction_=', 
                    {
                      type: 'category',
                      label: 'Expression',
                      items: [
                        'Expression', 
                        'Operator_priority', 
                      ]
                    },
                    {
                      type: 'category',
                      label: 'Property operators',
                      items: [
                        'Property_operators', 
                        'Arithmetic_operators', 
                        'Operator_', 
                        'ABSTRACT_operator', 
                        'ACTIVE_TAB_operator', 
                        'AGGR_operator', 
                        'CASE_operator', 
                        'CLASS_operator', 
                        'CONCAT_operator', 
                        'DATA_operator', 
                        'EXCLUSIVE_operator', 
                        'FORMULA_operator', 
                        'GROUP_operator', 
                        'IF_operator', 
                        'IF_..._THEN_operator', 
                        'JOIN_operator', 
                        'MAX_operator', 
                        'MIN_operator', 
                        'MULTI_operator', 
                        'OVERRIDE_operator', 
                        'PARTITION_operator', 
                        'PREV_operator', 
                        'RECURSION_operator', 
                        'STRUCT_operator', 
                        'UNGROUP_operator', 
                        'Object_group_operator', 
                        'Type_conversion_operator', 
                        'AND_OR_NOT_XOR_operators', 
                        'IS_AS_operators', 
                        'Change_operators', 
                        'Comparison_operators', 
                      ]
                    },
                    'Property_options', 
                  ]
                },
                {
                  type: 'category',
                  label: 'ACTION instruction',
                  items: [
                    'ACTION_instruction', 
                    {
                      type: 'category',
                      label: 'Action operator',
                      items: [
                        'Action_operator', 
                        'Operator_..._', 
                        'ABSTRACT_operator_action_', 
                        'ACTIVATE_operator', 
                        'ACTIVE_FORM_operator', 
                        'APPLY_operator', 
                        'ASK_operator', 
                        'CHANGE_operator', 
                        'BREAK_operator', 
                        'CANCEL_operator', 
                        'CASE_operator_action_', 
                        'CHANGECLASS_operator', 
                        'INTERNAL_operator', 
                        'DELETE_operator', 
                        'DIALOG_operator', 
                        'EMAIL_operator', 
                        'EVAL_operator', 
                        'EXEC_operator', 
                        'EXPORT_operator', 
                        'EXTERNAL_operator', 
                        'FOR_operator', 
                        'IF_..._THEN_operator_action_', 
                        'IMPORT_operator', 
                        'INPUT_operator', 
                        'MESSAGE_operator', 
                        'MULTI_operator_action_', 
                        'NEW_operator', 
                        'NESTEDSESSION_operator', 
                        'NEWEXECUTOR_operator', 
                        'NEWSESSION_operator', 
                        'NEWTHREAD_operator', 
                        'PRINT_operator', 
                        'READ_operator', 
                        'REQUEST_operator', 
                        'RETURN', 
                        'SEEK_operator', 
                        'SHOW_operator', 
                        'TRY_operator', 
                        'WHILE_operator', 
                        'WRITE_operator', 
                      ]
                    },
                    'Action_options', 
                  ]
                },
                'GROUP_instruction', 
                {
                  type: 'category',
                  label: 'ON instruction',
                  items: [
                    'ON_instruction', 
                    'Event_description_block', 
                  ]
                },
                'WHEN_instruction', 
                'Instruction_-_WHEN', 
                'CONSTRAINT_instruction', 
                'Instruction=_', 
                'CLASS_instruction', 
                {
                  type: 'category',
                  label: 'FORM instruction',
                  items: [
                    'FORM_instruction', 
                    'Object_blocks', 
                    'Properties_and_actions_block', 
                    'Filters_and_sortings_block', 
                    'Event_block', 
                  ]
                },
                'DESIGN_instruction', 
                'NAVIGATOR_instruction', 
                'WINDOW_instruction', 
                'EXTEND_CLASS_instruction', 
                'EXTEND_FORM_instruction', 
                'Instruction_+=', 
                'ACTION+_instruction', 
                'META_instruction', 
                'Instruction_', 
                'TABLE_instruction', 
                'INDEX_instruction', 
                'BEFORE_instruction', 
                'AFTER_instruction', 
                'Empty_instruction', 
              ]
            },
            'Coding_conventions', 
          ]
        },
        'IDE', 
        {
          type: 'category',
          label: 'Examples',
          items: [
            'Examples', 
            'Score_table', 
            'Materials_management', 
            {
              type: 'category',
              label: 'How-to',
              items: [
                'How-to', 
                {
                  type: 'category',
                  label: 'How-to: Computations',
                  items: [
                    'How-to_Computations', 
                    'How-to_GROUP_SUM', 
                    'How-to_GROUP_MAX_MIN_AGGR', 
                    'How-to_CASE_IF_OVERRIDE', 
                    'How-to_GROUP_LAST', 
                    'How-to_GROUP_CONCAT', 
                    'How-to_PARTITION', 
                  ]
                },
                {
                  type: 'category',
                  label: 'How-to: GUI',
                  items: [
                    'How-to_GUI', 
                    {
                      type: 'category',
                      label: 'How-to: Interactive forms',
                      items: [
                        'How-to_Interactive_forms', 
                        'How-to_CRUD', 
                        'How-to_Documents_with_lines', 
                        'How-to_Filtering_and_ordering', 
                        'How-to_Design', 
                        'How-to_Trees', 
                        'How-to_Data_entry', 
                        'How-to_Navigator', 
                        'How-to_Matrix', 
                        'How-to_Table_status', 
                      ]
                    },
                    'How-to_Reports', 
                  ]
                },
                {
                  type: 'category',
                  label: 'How-to: Imperative logic',
                  items: [
                    'How-to_Imperative_logic', 
                    {
                      type: 'category',
                      label: 'How-to: Actions',
                      items: [
                        'How-to_Actions', 
                        'How-to_CHANGE', 
                        'How-to_EXEC', 
                        'How-to_NEW', 
                        'How-to_DELETE', 
                        'How-to_FOR', 
                        'How-to_IF_CASE', 
                        'How-to_WHILE', 
                        'How-to_NEWSESSION', 
                        'How-to_SEEK', 
                      ]
                    },
                    'How-to_Events', 
                  ]
                },
                {
                  type: 'category',
                  label: 'How-to: Declarative logic',
                  items: [
                    'How-to_Declarative_logic', 
                    'How-to_Constraints', 
                    'How-to_Inheritance_and_aggregation', 
                  ]
                },
                {
                  type: 'category',
                  label: 'How-to: Searching for elements',
                  items: [
                    'How-to_Searching_for_elements', 
                    'How-to_Namespaces', 
                    'How-to_Explicit_typing', 
                  ]
                },
                {
                  type: 'category',
                  label: 'How-to: Extensions',
                  items: [
                    'How-to_Extensions', 
                    'How-to_Class_extension', 
                    'How-to_Property_extension', 
                    'How-to_Action_extension', 
                    'How-to_Form_extension', 
                  ]
                },
                {
                  type: 'category',
                  label: 'How-to: Integration',
                  items: [
                    'How-to_Integration', 
                    {
                      type: 'category',
                      label: 'How-to: Working with external formats',
                      items: [
                        'How-to_Working_with_external_formats', 
                        'How-to_Data_export', 
                        'How-to_Data_import', 
                      ]
                    },
                    'How-to_Interaction_via_HTTP_protocol', 
                    'How-to_Frontend', 
                    {
                      type: 'category',
                      label: 'How-to: Access to internal systems',
                      items: [
                        'How-to_Access_to_internal_systems', 
                        'How-to_FORMULA', 
                        'How-to_INTERNAL', 
                      ]
                    },
                  ]
                },
                {
                  type: 'category',
                  label: 'How-to: Use Cases',
                  items: [
                    'How-to_Use_Cases', 
                    'How-to_Working_with_documents', 
                    'How-to_Registers', 
                    'How-to_Numbering', 
                    'How-to_Using_objects_as_templates', 
                    'How-to_Overriding_values', 
                    'How-to_Binding_properties', 
                  ]
                },
                'How-to_Metaprogramming', 
                'How-to_Physical_model', 
                'How-to_Internationalization', 
              ]
            },
          ]
        },
        'Online_demo', 
      ]
    },
  ]
};
