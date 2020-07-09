/*==================================================================*/
/* Table structure for table unittree_unit_selection_parametrable_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_selection_parametrable_cf;
CREATE TABLE workflow_task_unittree_unit_selection_parametrable_cf (
  id_task INT NOT NULL,
  parametrable_config_handler VARCHAR(255),
  PRIMARY KEY (id_task)
 ) ;