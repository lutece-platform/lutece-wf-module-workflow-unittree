/*==================================================================*/
/* Table structure for table workflow_task_unittree_information   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_information;
CREATE TABLE workflow_task_unittree_information (
  id_history int NOT NULL,
  id_task int NOT NULL,
  information_key VARCHAR(255) NOT NULL,
  information_value VARCHAR(255) NULL
 ) ;
 
/*==================================================================*/
/* Table structure for table workflow_task_unittree_unit_assignment_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_assignment_cf;
CREATE TABLE workflow_task_unittree_unit_assignment_cf (
  id_task int NOT NULL,
  assignment_type VARCHAR(50) NULL,
  unit_selections VARCHAR(1000) NULL,
  PRIMARY KEY (id_task)
 ) ;

/*==================================================================*/
/* Table structure for table workflow_task_unittree_unit_selection_specific_unit_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_selection_specific_unit_cf;
CREATE TABLE workflow_task_unittree_unit_selection_specific_unit_cf (
  id_task int NOT NULL,
  id_unit int NOT NULL,
  PRIMARY KEY (id_task)
 ) ;

 /*==================================================================*/
/* Table structure for table unittree_unit_selection_parametrable_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_selection_parametrable_cf;
CREATE TABLE workflow_task_unittree_unit_selection_parametrable_cf (
  id_task INT NOT NULL,
  parametrable_config_handler VARCHAR(255),
  PRIMARY KEY (id_task)
 ) ;
 
/*==================================================================*/
/* Indexes creation for module workflow_unittree */
/*===================================================================*/
CREATE INDEX index_unittree_unit_assignment_resource ON unittree_unit_assignment (id_resource,resource_type);
CREATE INDEX index_unittree_unit_assignment_id_assigned_unit ON unittree_unit_assignment (id_assigned_unit,assignment_date);
CREATE INDEX index_unittree_unit_assignment_id_assignor_unit ON unittree_unit_assignment (id_assignor_unit,assignment_date);
