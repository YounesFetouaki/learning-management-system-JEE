<%@ page isErrorPage="true" %>
<h1>Oops! Something went wrong.</h1>
<p><b>Error Message:</b> <%= exception.getMessage() %></p>
<pre><%= exception.toString() %></pre>
