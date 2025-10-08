-- liquibase formatted sql
-- changeset workflow-unittree:update_db_workflow_unittree-1.0.0-1.0.1.sql
-- preconditions onFail:MARK_RAN onError:WARN
/*==================================================================*/
/* Table structure for table workflow_task_unittree_unit_selection_specific_unit_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_selection_specific_unit_cf;
CREATE TABLE workflow_task_unittree_unit_selection_specific_unit_cf (
  id_task int(11) NOT NULL,
  id_unit int(11) NOT NULL,
  PRIMARY KEY (id_task)
 ) ;