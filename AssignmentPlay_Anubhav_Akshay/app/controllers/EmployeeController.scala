package controllers

import models.{EmployeeData,Employee}

import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**Case class for EmployeeformData
  */


case class EmployeeFormData(name:String,address:String,dob:String,joiningDate:String,designation:String)

/**Case class for EmployeeformData
  */

case class EmployeeSearch(name:String)

/**class employeeController
  */

class EmployeeController extends Controller{

  val addEmployeeForm = Form(
    mapping(
      "name"->nonEmptyText,
      "address"->nonEmptyText,
      "dob"->nonEmptyText,
      "joiningDate"->nonEmptyText,
      "designation"->nonEmptyText
    )(EmployeeFormData.apply)(EmployeeFormData.unapply)
  )

  val updateEmployeeForm = Form(
    mapping(
      "name"->nonEmptyText,
      "address"->nonEmptyText,
      "dob"->nonEmptyText,
      "joiningDate"->nonEmptyText,
      "designation"->nonEmptyText
    )(EmployeeFormData.apply)(EmployeeFormData.unapply)
  )

  def listEmployees = Action { implicit request =>
    Ok(views.html.listEmployee(searchEmployeeForm,EmployeeData.employeeList.toList))
  }
  /** A method that search for a Employee with a given name for filtering
    * @param name the name of employee
    * @return list of employeee
    */

  def searchEmployee(name:String):List[Employee]={
    EmployeeData.employeeList.filter(_.name.contains(name)).toList
  }
  /** A Action for updating Employee
    */
  def updateEmployee(name:String) = Action(implicit request =>
    Ok(views.html.updateEmployee(getEmployeeData(name),updateEmployeeForm))
  )
  /**
    *@param name the name of employee
    * @return  employeee
    */
  def getEmployeeData(name:String) :Employee ={
    val completeList = EmployeeData.getEmployees()
    val tempList = completeList map (x => x.name)
    completeList.apply(tempList.indexOf(name))
  }
  /**
    *@param name the name of employee,newname,newdob,newjoinningDate,newDesignation
    * @return  updated list of employeee
    */

  def updateEmployeeList(name:String,newName:String,newAddr:String,newDob:String,
                         newJoiningDate:String,newDesignation:String)={
    val tempList = EmployeeData.getEmployees() map (x => x.name)
    EmployeeData.employeeList.update(tempList.indexOf(name),Employee(newName,newAddr,newDob,newJoiningDate,newDesignation))
  }
  /** Action for deleting Employee
    */

  def delete(name:String) =Action.async { implicit request =>
    deleteEmployee(name)
   Future(Redirect(routes.EmployeeController.listEmployees()))
  }
  /** method for deleting Employee
    */
  def deleteEmployee(name:String) ={
    val tempList = EmployeeData.getEmployees() map (x => x.name)
    EmployeeData.employeeList.remove(tempList.indexOf(name))
  }

  val searchEmployeeForm = Form(
    mapping(
      "name"->nonEmptyText.verifying(value => checkName(value))
    )(EmployeeSearch.apply) (EmployeeSearch.unapply)
  )

  /** Action for adding Employee
    */

  def addEmployee = Action{implicit request =>
    Ok(views.html.addEmployee(addEmployeeForm))
  }
  /**check whether employee name is valid or not
    *@param  the name of employee
    * @return boolean
    */
  def checkName(eName:String) :Boolean ={
    val listOfNames = EmployeeData.getEmployees() map (x => x.name)
    if(listOfNames.contains(eName))
      true
    else
      false
  }
  /**Action for authorization of  addEmployeeForm
    *@return Redirection request
    */

  def auth=Action.async{implicit request =>
    addEmployeeForm.bindFromRequest.fold(
      error => Future(BadRequest(views.html.addEmployee(error))),
      addEmployeeFormData => {
        EmployeeData.addEmployee(addEmployeeFormData.name,addEmployeeFormData.address,addEmployeeFormData.dob,addEmployeeFormData.joiningDate,addEmployeeFormData.designation)
        Future(Redirect(routes.EmployeeController.listEmployees))
      }
    )
  }
  /**Action for authorization of  searchEmployeeForm
    *@return Redirection request
    */
  def searchAuth = Action.async{ implicit  request =>
    searchEmployeeForm.bindFromRequest.fold(
      error => Future(BadRequest(views.html.listEmployee(searchEmployeeForm,EmployeeData.employeeList.toList))),
      searchData => Future(Ok(views.html.listEmployee(searchEmployeeForm,searchEmployee(searchData.name))))
    )
  }

  def updateAuth(name:String)=Action.async{implicit request =>
    updateEmployeeForm.bindFromRequest.fold(
      error => Future(BadRequest(views.html.updateEmployee(getEmployeeData(name),updateEmployeeForm))),
      data => {
        updateEmployeeList(name,data.name,data.address,data.dob,data.joiningDate,data.designation)
        Future(Redirect(routes.EmployeeController.listEmployees()))
      }
    )
  }

}
