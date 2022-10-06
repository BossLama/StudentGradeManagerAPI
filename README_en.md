 _ _ _ _ _
<p align="center">
	<a href="https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README.md">deutsch</a> |
	<a href="https://riemerjonas.de">website</a>
</p>

 _ _ _ _ _
# StudentGradeManagerAPI
API to manage student grade data!

# Foreword:
The Student Grades Manager API (SGMA for short) is intended to manage student data and their grades. For this purpose, communication takes place via HTTP in JSON format. Please adhere to the formatting given in the examples to avoid errors.
The default port for the API is 8974.
Note: Not following the order in which attributes are specified can cause errors.


# Overview
- [Setup](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#Setup)
- [student data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#student-data)
	- [create student data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#sch%C3%BClerdaten-erstellen-post-request)
	- [change student data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#%C3%BCberarbeitenver%C3%A4ndern-von-sch%C3%BClerdaten-post-request)
	- [delete student data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#l%C3%B6schen-von-sch%C3%BClerdaten-post-request)
	- [get single student data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#einzelne-sch%C3%BClerdaten-abfragen-get-request)
	- [get list of student data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#liste-aller-sch%C3%BCler-einer-klasse-abfragen-get-request)
- [grade data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#notendaten)
	- [create grade data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#erstellen-einer-note-post-request)
	- [change grade data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#%C3%BCberarbeitenver%C3%A4ndern-von-notendaten-post-request)
	- [delete grade data](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#l%C3%B6schen-von-notendaten-post-request)
	- [get grade data by id](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#bestimme-notendaten-abfragen-get-request)
	- [get grade data by student](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#abfragen-aller-noten-eines-sch%C3%BClers-get-request)
- [Errorcodes](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#fehler-und-fehlerbehebung)
- [Features in future](https://github.com/BossLama/StudentGradeManagerAPI/blob/master/README_en.md#features-in-zukunft)

# Setup
Start the program once. A new config file is created. This is located here:
`config/config-database.json`
The program will now end automatically. Now edit the database login data and set "isSetup" to "true".
The file should then look like this:
```json
{
  "map":{
    "database":"student_manager",
    "password":"",
    "port":"3306",
    "host":"localhost",
    "username":"root",
    "isSetup":"true"
  }
}
```
Now start the program again. If everything worked, the server will start on port `8974`
You can then change this in the ` config/config-server.json ` file.

# Student data:
In order to be able to work with student data, the following endpoint must be addressed:

` /api/student `

To query data use the REQUEST method GET, to send data use the REQUEST method POST. You specify which data you want to receive, create, edit or delete in the REQUEST-BODY in JSON format (which is explained to you in the respective examples).

## Create student data (POST-REQUEST):
In order to register a new student in the system, you must specify the following in the REQUEST-BODY:
- action (executive action, here "create")
- firstname (first name of the student)
- lastname (last name of the student)
- classtag (class name of the student)

```json
{
  "action":"create",
  "data": {
  "firstname":"Max",
"lastname":"Doe",
"classtag":"5A"
  }
}
```

If a student is successfully created, you will receive the following RESPONSE:
```json
{
  "success":"true",
  "error": {
     "name":"Successful",
    "message":"There is no error",
    "error code":"0"
}
```
(Figure 3)

## Revision/change of student data (POST-REQUEST):
In order to be able to change student data, the following attributes must be specified in the REQUEST-BODY:
- action (action to be performed, here "update")
- student-id (ID of the student)
- data-name (attribute to be changed)
- data-value (New value for the attribute)

The following attributes can be changed: (Apply these names in 'data-name' to change them)
- firstname (first name of the student)
- lastname (last name of the student)
- classtag (class name of the student)

```json
{
  "action":"update",
  "student-id":"7623",
  "data": {
  "firstname":"Max",
"classtag":"6A"
  }
}
```

If the data in the database has been successfully revised, you will receive the following RESPONSE CODE: (see Figure 3)


## Deletion of student data (POST-REQUEST):
In order to be able to delete student data, the following 2 attributes are required:
- action (action to be executed, here "delete")
- student-id (ID of the student to be deleted)

```json
{
  "action":"delete",
  "student-id":"93874"
}
```

If the action is successful, you will receive the following RESPONSE CODE:
(see Figure 3)

## Query individual student data (GET-REQUEST):
In order to query individual student data, you must pass the following attributes to the "student" endpoint (ATTENTION: Please keep to the order given below):
- studentid (The ID of the student to search for)
OR
- firstname (first name of the student to be searched)
- lastname (last name of the student to be searched)

` /api/student?studentid=87324 `

or.

` /api/student?firstname=Max&lastname=Doe `

If the REQUEST is successful, you will receive the following RESPONSE in JSON format:

```json
{
   "success":"true",
   "University student": {
"studentid":13,
"firstname":"Maria",
"lastname":"Example",
"classtag":"5A",
"average": "2.5"
   }
}
```

## Query the list of all students in a class (GET-REQUEST):
There are two ways to query a class list:

1. Query unsorted:
To do this, use the following attributes in exactly the same order:
o list (Indicates that this is a list)
o classtag (specifies the searched class)

` /api/student?list&classtag=10A `

2. Query sorted:
To do this, use the following attributes in exactly the same order:
o list (Indicates that this is a list)
o classtag (specifies the searched class)
o sort-by (Indicates what to sort by)

sort-by can have the following values:
o firstname (Sort list by first name)
o lastname (Sort list by last name)
o average (Sort list by average grade)

` /api/student?list&classtag=10A&sort-by=firstname `

If the REQUEST is successful, you will receive the following RESPONSE code:

```json
{
   "success":"true",
   "studentlist":[
{
"studentid":13,
"firstname":"Maria",
"lastname":"Example",
"classtag":"5A",
"average": "2.5"
},
{
"studentid":14,
"firstname":"Max",
"lastname":"Example",
"classtag":"5A",
"average": "2.5"
}
  ]
}
```

# note data:
In order to work with grade data, the following endpoint must be addressed:

` /api/grade `

The endpoint allows you to create, change/revise and delete grade data (see respective sub-chapter).

## Creating a grade (POST-REQUEST):
The following attributes are required to create a grade:
- action (Indicates the action to be performed, here "create")
- student-id (The ID of the associated student)
- grade-type (The type of school assignment, e.g. written)
- value (The value of the grade itself, e.g. 1.0)
- weight (value of the grade)

```json
{
  "action":"create",
  "data": {
    "student-id":"87432",
    "grade-type":"written",
    "subject":"english",
    "value":1.0,
    "weight":1
  }
}
```

If the REQUEST is successful, you will receive the following RESPONSE CODE: (see Fig. 3)

## Revision/change of note data (POST-REQUEST):
To change notes, you need the following attributes:
- action (action to be carried out, update here)
- grade-id (ID of the grade to be changed)
- data-name (attribute to be changed)
- data-value (new value of the attribute)

```json
{
  "action":"update",
  "grade-id":"7623",
  "data": {
  "grade-type":"oral",
"subject":"math"
  }
}
```

If the REQUEST is successful, you will receive the following RESPONSE code: (see Fig. 3)

## Deletion of note data (POST-REQUEST):
To delete grades from the system, the following attributes are required:
- action (action to be executed, here "delete")
- grade-id (ID of the grade to be deleted)

```json
{
  "action":"delete",
  "grade-id":"93874"
}
```

If the REQUEST is successful, you will receive the following RESPONSE code: (see Fig. 3)


## Get specific note data (GET-REQUEST):
In order to be able to execute a GET request, you must address the following endpoint:

` /api/grade `

If you want to query data of a grade, specify the attribute "grade-id":

` /api/grade?grade-id=8723 `

If the REQUEST is successful, you will receive the following RESPONSE code:

```json
{
   "success":"true",
   "grade": {
"gradid":13,
"studentid":9823,
"gradetype":"in writing",
"subject":"english",
"value":1.0,
"weight": 1,
"date":"2022-09-25 00:13:41"
}
}
```

## Query all grades of a student (GET-REQUEST):
To get a list of all grades for a student, the following attributes must be provided:
- student-id (ID of the student)

` /api/grade?student-id=9632 `

If the REQUEST is successful, you will receive the following response:

```json
{
   "success":"true",
   "grade": [
        {
"gradid":13,
"studentid":9823,
"gradetype":"in writing",
"subject":"english",
"value":1.0,
"weight": 1,
"date":"2022-09-25 00:13:41"
},
        {
"gradid":14,
"studentid":9823,
"gradetype":"oral",
"subject":"english",
"value":2.0,
"weight": 1,
"date":"2022-09-25 00:13:41"
}
  ]
}
```
# Errors and troubleshooting:

| CODE | ERROR-NAME | CAUSE | FIX |
| ------------- | ------------- | ------------- | ------------- |
| 0 | No error | Successful request | NO |
| 1 | Syntax - Error | Currently no | NO |
| 2 | API KEY - Error | Currently no | NO |
| 3 | Unknown request type | No Get or Post Request | Set Request-Method to POST or GET |
| 4 | Wrong JSON Format | Wrong format in JSON | Check format in Documentation |
| 5 | Unset ActionError | No action attribute | In your JSON, specify the action attribute |
| 6 | Save DatabaseError | Error sending to database | Check the login data for the database |
| 7 | Unknown Dataname Error | Unknown dataname attribute in JSON | Check which Datanames you are allowed to use |
| 8 | Entry not found Error | No element found which is searched for | Check details like studentid or gradeid |
| 9 | Invalid Get Request | Wrong get request | Check attributes and their order |

# Features in the future:
The following features will be added in the future:

-	API-KEY
-	~~Config.json File (Einstellen von Datenbankverbindungen etc.)~~ (Implementiert seit 06.10.2022 - v1.3)
-	~~Prevent sql-insection~~ (Implementiert seit 06.10.2022 - v1.3.1)
