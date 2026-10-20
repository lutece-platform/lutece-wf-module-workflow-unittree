-- liquibase formatted sql
-- changeset workflow-unittree:update_db_workflow_unittree-3.0.1-3.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment decodes, once, the HTML entities the removed input filter of lutece-core 8 (sanitizeFilterMode, LUT-34109) wrote into the plain text values: the subject of the unit assignment notification. Its message, typed with the rich text editor, is left as it is, as is workflow_task_unittree_information (history).
-- This changeset rewrites the data of the site. Back up the database before the upgrade. It must run ONCE only: run
-- again, it would decode a second time a text that really holds an entity (a typed &lt; stored as &amp;lt; becomes &lt;
-- once, then < the second time). Only the values matching LIKE '%&%;%' are concerned; to list them before the upgrade,
-- run the WHERE clauses below in a SELECT.
UPDATE workflow_task_unittree_unit_assignment_notification_cf SET subject = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(subject, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE subject LIKE '%&%;%';
