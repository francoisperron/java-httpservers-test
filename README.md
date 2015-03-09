# How easy is it to change the http server implementation in a java project?

With [@ericminio], we wanted to explore the [port and adapter] pattern on a http server.
Exploring this idea, we found it pretty simple to hide behind interfaces which http server library was used at run-time.

The [Server], [Endpoint], [HttpRequest], and [HttpResponse] emerge as interfaces we like to use when dealing with http communication. They are all in the same http package inside the core of the application.

We tested these implementations: [built-in java http server], [simple], [servlet] and [jetty] duo, and [undertow].

These implementations are outside the core of the application, in the http adapter package. As you can see, we choose not to use the routing of any of these libraries.

# How do they perform?

They all do a pretty good job for the vast majority of http servers out there. However, some are better under heavy load. We found it pretty distrubing that the built-in java http server could not handle a hundred simultaneous TCP connection, and drops packets. 

We found [TechEmpower frameworks benchmarks] a better place to check for http server performance.

[TechEmpower frameworks benchmarks]: http://www.techempower.com/benchmarks/#section=data-r9&hw=i7&test=query
[built-in java http server]: http://docs.oracle.com/javase/7/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html
[simple]: http://www.simpleframework.org/ 
[servlet]: http://download.eclipse.org/jetty/stable-9/apidocs/org/eclipse/jetty/server/Server.html
[jetty]: http://www.eclipse.org/jetty
[undertow]: http://undertow.io
[Server]: https://github.com/francoisperron/java-httpservers-test/blob/master/src/yose/http/Server.java
[Endpoint]: https://github.com/francoisperron/java-httpservers-test/blob/master/src/yose/http/Endpoint.java
[HttpRequest]: https://github.com/francoisperron/java-httpservers-test/blob/master/src/yose/http/HttpRequest.java
[HttpResponse]: https://github.com/francoisperron/java-httpservers-test/blob/master/src/yose/http/HttpResponse.java
[@ericminio]:https://github.com/ericminio
[port and adapter]:http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html
