/*==================================================================*/
/* Table structure for table workflow_task_unittree_unit_selection_specific_unit_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_selection_specific_unit_cf;
CREATE TABLE workflow_task_unittree_unit_selection_specific_unit_cf (
  id_task int(11) NOT NULL,
  id_unit int(11) NOT NULL,
  PRIMARY KEY (id_task)
 ) ;