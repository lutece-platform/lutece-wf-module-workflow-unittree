/*====================================================================================*/
/* Table structure for table workflow_task_unittree_unit_assignment_notification_cf   */
/*====================================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_assignment_notification_cf;
CREATE TABLE workflow_task_unittree_unit_assignment_notification_cf (
  id_task INT NOT NULL,
  subject LONG VARCHAR NOT NULL,
  message LONG VARCHAR NOT NULL,
  PRIMARY KEY (id_task)
 ) ;