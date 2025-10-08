-- liquibase formatted sql
-- changeset workflow-unittree:update_db_workflow_unittree-1.0.1-2.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
/*==================================================================*/
/* Table structure for table unittree_unit_selection_parametrable_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_selection_parametrable_cf;
CREATE TABLE workflow_task_unittree_unit_selection_parametrable_cf (
  id_task INT NOT NULL,
  parametrable_config_handler VARCHAR(255),
  PRIMARY KEY (id_task)
 ) ;