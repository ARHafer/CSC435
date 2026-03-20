CSC435: Homework 2
==================

* * *

Homework 2 consists of implementing a service layer to your project, as well as consuming external services as needed.

Read the [document](../RESTful.pdf) pertaining to REST services on the course web page. It has some details that were not mentioned in class.

Write servlets and/or filters for a view layer to your application. This view layer will present your entire application functionality as a set of RESTful services. Use the JSON format by default, and do not forget to set the appropriate MIME type of your response:  
response.setContentType("application/json; charset=UTF-8");

Do not forget to work with all related servlet methods, depending on the service endpoint you are providing: GET, POST, PUT, or DELETE.

**NB: if your req spec document had poorly specified endpoints / external services, now is the time to fix it.** For some projects you may have to rethink your entire application and change its focus.

Submit to the Brightspace dropbox your sources and your packaged application in .war format.

* * *