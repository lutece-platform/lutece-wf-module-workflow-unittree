-- liquibase formatted sql
-- changeset workflow-unittree:update_db_workflow_unittree-3.0.2-3.1.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment swaps the subject and the message of the unit assignment notification: the former DAO stored the subject in the column message and the message in the column subject. Then decodes, once, the HTML entities the removed input filter of lutece-core 8 (LUT-34109) wrote into the subject: the changeset update_db_workflow_unittree-3.0.1-3.0.2.sql decoded the column subject, which held the message.
-- This changeset rewrites the data of the site. Back up the database before the upgrade. It must run ONCE only.
ALTER TABLE workflow_task_unittree_unit_assignment_notification_cf ADD COLUMN swapped_value LONG VARCHAR;
UPDATE workflow_task_unittree_unit_assignment_notification_cf SET swapped_value = subject;
UPDATE workflow_task_unittree_unit_assignment_notification_cf SET subject = message;
UPDATE workflow_task_unittree_unit_assignment_notification_cf SET message = swapped_value;
ALTER TABLE workflow_task_unittree_unit_assignment_notification_cf DROP COLUMN swapped_value;
UPDATE workflow_task_unittree_unit_assignment_notification_cf SET subject = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(subject, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE subject LIKE '%&%;%';
