<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<?xml version="1.0"?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href="style/default.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<p>
<f:view>
    <h:form id="searchForm">
        <p>What do you seek?</p>
        <h:inputText id="searchTerm" value="#{search.searchTerm}"/>
        <h:commandButton type="submit" value="Enter"
            action="#{search.handleSubmitEnter}"/>
    </h:form>

    <f:subview id="searchResult" rendered="#{search.hasSearchResult}">
        <hr/>
        <p>
            You shall find it here:
        </p>
        <h:dataTable
                 value="#{search.searchResult}" var="entry"
                 styleClass="jar"
                 first="0" width="80%" rules="all"
                 summary="This is a JSF code to create dataTable." >

	        <h:column>
	            <f:facet name="header">
	                <h:outputText value="Directory"/>
	            </f:facet>
	            <h:outputText value="#{entry.archive.directory}"/>
	        </h:column>
	        <h:column>
	            <f:facet name="header">
	                <h:outputText value="Jar"/>
	            </f:facet>
	            <h:outputText value="#{entry.archive.name}"/>
	        </h:column>
	        <h:column>
	            <f:facet name="header">
	                <h:outputText value="Class/Resource"/>
	            </f:facet>
	            <h:outputText value="#{entry.package}.#{entry.name}"/>
	        </h:column>
	    </h:dataTable>
    </f:subview>
</f:view>
</p>
</body>
</html>
