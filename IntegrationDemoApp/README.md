Demo Application for integration of SAP Anywhere with ERP systems
=================================================================
a java application providing samples how to integrate SAP Anywhere with any ERP system by means of [SAP Anywhere's OpenAPI](https://dev-eu.sapanywhere.com/api).

Table of contents
-----------------
* [Major Building Blocks](#major-building-blocks)
* [Scope of Integration Demo Application](#scope-of-integration-demo-application)
* [Install](#install)
* [Getting Started](#getting-started)
* [Establish Connection to SAP Anywhere](#establish-connection-to-sap-anywhere)
* [Execute Integration](#execute-integration)
* [Business Flow Diagram](#business-flow-diagram)
* [Data Flow Diagram](#data-flow-diagram)
* [Class Diagram](#class-diagram)
* [ToDo](#todo)

Major Building Blocks
---------------------
Integration Demo App can be devided into 3 major parts:
1. handling of SAP Anywhere OpenAPI
2. Core integration/transformation logic
3. dummy ERP - represented by sqlite3 based database. Replace this part with proper connection to your desired ERP system.

Scope of Integration Demo Application
-------------------------------------
App demonstrates use cases of integration with ERP softwares and devides them logically as:
1. Master Data (Customers, Products)
2. Sales Orders
3. Stock affecting sales documents (Sales Deliveries)
4. Financial sales documents (A/R Invoices, Payments)
5. Stock (Inventory Counts)

Install
-------
clone from the [GitHub repository](https://github.com/SAP/anywhere.git) to run tests and examples locally:
```bash
git clone ssh://git@github.com/SAP/anywhere.git
cd IntegrationDemoApp
mvn clean install
```

Getting Started
---------------
If you do not have access to SAP Anywhere tenant, go to the [SAP Anywhere Solution Portal](https://eap-us.sapanywhere.com) and get your own trial account.


Establish Connection to SAP Anywhere
------------------------------------
Create [Private App](https://dev-eu.sapanywhere.com/manual/private_app) in your SAP Anywhere tenant.
Create `config.properties` in your user's home folder:
```bash
cd ~
vi config.properties
```
and define values for the properties:
```bash
##################################################
# CONFIGURATION FOR CONNECTION TO THE SAP ANYWHERE
#
# ANW_PROTOCOL allowed values - http, https
ANW_PROTOCOL = notDefined
#
# ANW_SERVER - IP address or FQDN of SAP Anywhere's application server
ANW_SERVER = notDefined
#
# ANW_PORT - port of SAP Anywhere application server
ANW_PORT = notDefined
#
# ANW_CERTIFICATE - the IntegrationDemoApp does not use authentication with certificates, implement this for productive usage
ANW_CERTIFICATE = notDefined
##################################################

##################################################
# CONFIGURATION FOR CONNECTION TO THE IDENTITY PROVIDER
#
# IDP_PROTOCOL allowed values - http, https
IDP_PROTOCOL = notDefined
#
# IDP_SERVER - IP address or FQDN of SAP Anywhere's Identity Provider
IDP_SERVER = notDefined
#
# IDP_PORT - port of SAP Anywhere application server
IDP_PORT = notDefined
#
# IDP_PATH - path to Identity Provider's OAuth 2.0
IDP_PATH = sld/oauth2/token
##################################################

##################################################
# CONFIGURATION FOR OAUTH
#
# APP_ID is the private app id, you can get it in the private app details page
APP_ID = notDefined
#
# APP_SECRET is the private app secret, you can get it in the private app details page
APP_SECRET = notDefined
#
# REFRESH_TOKEN is the private app refresh token, you can get it in the private app details page
REFRESH_TOKEN = notDefined
#
ACCESS_TOKEN = notDefined
##################################################

##################################################
# CONFIGURATION OF SCHEDULER
#
# FOR SCHEDULING PURPOSES WE ARE USING CRON4J
# MORE INFO ABOUT CONFIGURATION SCHCEDULER YOU WILL FIND @ http://www.sauronsoftware.it/projects/cron4j/manual.php#p01
#
# IF YOU WANT TO USE SCHEDULER FOR CONTINUOUS INTEGRATION, ENTER true
# IF YOU DO NOT WANT TO USE SCHEDULER AND YOU WANT TO RUN INTEGRATION JUST ONCE, ENTER false. THEN, YOU MAY USE YOUR OWN EXTERNAL
# SCHEDULER.
SCHEDULER_ACTIVATION = true
#
# SCHEDULER_ACCESS_TOKEN - timer for refreshing the access token. Access token is valid for 12 hours.
SCHEDULER_ACCESS_TOKEN = 0 0 0/12 * * ?
#
# SCHEDULER_INTEGRATION - timer for the integration cycle
SCHEDULER_INTEGRATION = 0 * * * * ?
##################################################

##################################################
# CONFIGURATION OF WAREHOUSES
#
# CONFIGURATION FOR USAGE OF DEFAULT WAREHOUSE IN SAP ANYWHERE USED FOR INTEGRATION PURPOSES
# IF CUSTOM ERP DOES NOT SUPPORT WAREHOUSES, YOU HAVE TO SET UP USE_DEFAULT_WAREHOUSE TO true AND FILL DEFAULT_ANW_WAREHOUSE BY 
# WAREHOUSE CODE FROM SAP ANYWHERE, WHICH REPRESENTS DEFAULT WAREHOUSE.
# WHEN CUSTOM ERP DOES SUPPORT WAREHOUSES, YOU HAVE TO SET UP USE_DEFAULT_WAREHOUSE to false. KEY DEFAULT_ANW_WAREHOUSE WILL 
# BE IGNORED AND WAREHOUSES FROM ERP WILL BE USED FOR SYNCHRONIZATION PURPOSES.
# THIS FEATURE IS PROGRAMMED BECAUSE NOT MANY CUSTOM ERP SUPPORT WAREHOUSES. 
USE_DEFAULT_WAREHOUSE = false
#
DEFAULT_ANW_WAREHOUSE = \u4e3b\u4ed3\u5e93 
##################################################
```

Execute Integration
-------------------
There's 2 options:
1. execute from command line:
```bash
cd IntegrationDemoApp\target\
unzip IntegrationDemoApp-1.0-bundle.zip
cd IntegrationDemoApp-1.0-bundle
sh run.sh
```
2. Run from Eclipse - import existing maven project from `IntegrationDemoApp/pom.xml`
Executing from Eclipse gives you the possibility to debug the solution; e.g. put breakpoint into `Main.java` and proceed from that point onward.

Business Flow Diagram
---------------------
![BizFlow](https://github.com/SAP/anywhere/blob/master/IntegrationDemoApp/flows/bizFlow.png "bizFlow.png")

Data Flow Diagram
-----------------
![DataFlow](https://github.com/SAP/anywhere/blob/master/IntegrationDemoApp/flows/DataFlow.png "DataFlow.png")

Class Diagram
-------------
![ClassDiagram](https://github.com/SAP/anywhere/blob/master/IntegrationDemoApp/flows/coreDiagram.png "coreClassDiagram.png")
More class diagrams are available in their raw format at `IntegrationDemoApp/src/main/resources/Diagrams/*.ucls`.

ToDo
----
- improve documentation of API
- ...

