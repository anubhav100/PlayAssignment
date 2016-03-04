package models

import java.util.Date

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future


/**
  * Case class for Employee
  */
case class Employee(name:String,address:String,dob:String,joiningDate:String,designation:String)


/**
  * Object EmployeeData that returns Future of ListBuffer of Employees
  */
object EmployeeData{
  val employeeList={
    ListBuffer(Employee("Anubhav", "gurgaon", "1992-7-14", "1992-7-14", "conultant"), Employee("Akshay", "gurgaon", "1992-7-14", "1992-7-14", "conultant"))
  }
  def getEmployees()= employeeList

  def addEmployee(eName:String,eAddress:String,eDob:String,eJoiningDate:String,eDesignation:String): Unit = {
    employeeList += Employee(eName,eAddress,eDob,eJoiningDate,eDesignation)
  }

  def searchEmployee(name:String):List[Employee]={
    employeeList.filter(_.name.contains(name)).toList
  }

}



