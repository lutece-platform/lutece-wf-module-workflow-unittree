/*==================================================================*/
/* Table structure for table unittree_unit_assignment   */
/*==================================================================*/
DROP TABLE IF EXISTS unittree_unit_assignment;
CREATE TABLE unittree_unit_assignment (
  id int AUTO_INCREMENT,
  id_resource int(11) NOT NULL,
  resource_type VARCHAR(255) NOT NULL,
  id_assignor_unit int(11) NOT NULL DEFAULT '0',
  id_assigned_unit int(11) NOT NULL DEFAULT '0',
  assignment_type VARCHAR(50) NOT NULL,
  assignment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_active SMALLINT NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
 ) ;
 
/*==================================================================*/
/* Table structure for table workflow_task_unittree_information   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_information;
CREATE TABLE workflow_task_unittree_information (
  id_history int(11) NOT NULL,
  id_task int(11) NOT NULL,
  information_key VARCHAR(255) NOT NULL,
  information_value VARCHAR(255) NULL
 ) ;
 
/*==================================================================*/
/* Table structure for table workflow_task_unittree_unit_assignment_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_assignment_cf;
CREATE TABLE workflow_task_unittree_unit_assignment_cf (
  id_task int(11) NOT NULL,
  assignment_type VARCHAR(50) NULL,
  unit_selections VARCHAR(1000) NULL,
  PRIMARY KEY (id_task)
 ) ;

/*==================================================================*/
/* Table structure for table workflow_task_unittree_unit_selection_specific_unit_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_unittree_unit_selection_specific_unit_cf;
CREATE TABLE workflow_task_unittree_unit_selection_specific_unit_cf (
  id_task int(11) NOT NULL,
  id_unit int(11) NOT NULL,
  PRIMARY KEY (id_task)
 ) ;

/*==================================================================*/
/* Indexes creation for module workflow_unittree */
/*===================================================================*/
CREATE INDEX index_unittree_unit_assignment_resource ON unittree_unit_assignment (id_resource,resource_type);
CREATE INDEX index_unittree_unit_assignment_id_assigned_unit ON unittree_unit_assignment (id_assigned_unit,assignment_date);
CREATE INDEX index_unittree_unit_assignment_id_assignor_unit ON unittree_unit_assignment (id_assignor_unit,assignment_date);
