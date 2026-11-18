-- liquibase formatted sql
-- changeset workflow-unittree:update_db_workflow_unittree-2.1.3-2.1.4.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- Removes what tasks left behind once removed : their information and their configuration. Only rows whose task or history no longer exists are deleted.
DELETE FROM workflow_task_unittree_information WHERE NOT EXISTS ( SELECT 1 FROM workflow_task t WHERE t.id_task = workflow_task_unittree_information.id_task );
DELETE FROM workflow_task_unittree_information WHERE NOT EXISTS ( SELECT 1 FROM workflow_resource_history h WHERE h.id_history = workflow_task_unittree_information.id_history );
DELETE FROM workflow_task_unittree_unit_assignment_cf WHERE NOT EXISTS ( SELECT 1 FROM workflow_task t WHERE t.id_task = workflow_task_unittree_unit_assignment_cf.id_task );
DELETE FROM workflow_task_unittree_unit_selection_specific_unit_cf WHERE NOT EXISTS ( SELECT 1 FROM workflow_task t WHERE t.id_task = workflow_task_unittree_unit_selection_specific_unit_cf.id_task );
DELETE FROM workflow_task_unittree_unit_selection_parametrable_cf WHERE NOT EXISTS ( SELECT 1 FROM workflow_task t WHERE t.id_task = workflow_task_unittree_unit_selection_parametrable_cf.id_task );
DELETE FROM workflow_task_unittree_unit_assignment_notification_cf WHERE NOT EXISTS ( SELECT 1 FROM workflow_task t WHERE t.id_task = workflow_task_unittree_unit_assignment_notification_cf.id_task );
