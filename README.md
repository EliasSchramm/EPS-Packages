# EPS-Packages

A simple way to get a secure server client-server connection running.

* 1. What is this?
    * 1. Goal
    * 2. Base Principles
* 2. Examples
* 3. Detailed documentation
    
## 1. What is this?

### 1.1 Goal

This is a project I wrote to make it easier for me to do networking with Java. It takes everything that has to do with
sockets away from the developer giving him the utilities to create encrypted network systems without the need of even
touching network code.  

### 1.2 Base Principles

How does this package function? Everything is build around one main idea: Packages. These Packages are everything that 
client and server exchange. Whilst sending these packages are just encrypted JSON objects that then get transformed into
the desired package on the receiving end.  

The server is an object that can accept connections, send and/or relay information, handle things that have to do with 
encryption and send errors to the client.

The client object is only capable of connecting to one single server it can not directly interact with other clients. 

Upon connect the server will transmit his desired chunk-size, and his public RSA4096 key (or 'null' if encryption is 
disabled) to the client. The client will then, if the key wasn't 'null', generate an AES128 Key that will be used for
the rest of the session. The AES128 key gets encrypted with the servers Public and send back to the server. The server
decrypts the key with his Private.

After the initial handshake sequence all data will be transmitted and handled in the form of Packets. These Packets are 
abstract classes that contain all base datatypes. This data then gets put into a JSON string, gets encrypted and then
send to the server/client. The receiving end then decrypts and puts it back into packet-form. 

## Examples 

Let's start with a simple echo server that changes the entire string to uppercase. 

First we need two packages: One package that the client server sends to the server containing the message it wants to
have echoed and one package that the server sends back to the client. 

### Instalation

To use this libary add this to the dependency sections inside of your pom.xml:

```xml
<dependency>
    <groupId>de.eps-dev</groupId>
    <artifactId>eps-packages</artifactId>
    <version>1.3.2</version>
</dependency>
```

``PackageRequestEcho.java``
```java

import de.epsdev.packages.packages.Base_Package;
import de.epsdev.packages.packages.Package;

import java.io.IOException;
import java.net.Socket;

public class PackageRequestEcho extends Package {

    // You NEED to have this constructor or else it cant be initiated.
    public PackageRequestEcho(Base_Package base_package) {
        super(base_package);
    }

    public PackageRequestEcho(String message){
        super("PackageRequestEcho");

        // Add the message data to the package
        // String(field_name) Object(data)
        add("message", message);
    }

    // This will be executed when the package is received.
    @Override
    public void onPackageReceive(Socket sender, Object o) {

        // Get the string from the package
        String message = getString("message");
        // Manipulate the string
        message = message.toUpperCase();

        // Create a new PackageRespondEcho
        PackageRespondEcho packageRespondEcho = new PackageRespondEcho(message);

        // Send the package back to the sender
        try {
            packageRespondEcho.send(sender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

``PackageRespondEcho.java``
```java
import de.epsdev.packages.packages.Base_Package;
import de.epsdev.packages.packages.Package;

import java.net.Socket;

public class PackageRespondEcho extends Package {
    public PackageRespondEcho(String manipulated) {
        super("PackageRespondEcho");

        // Add the manipulated data to the package
        // String(field_name) Object(data)
        add("message", manipulated);
    }

    public PackageRespondEcho(Base_Package base_package) {
        super(base_package);
    }

    @Override
    public void onPackageReceive(Socket sender, Object o) {

        // Print the manipulated message
        System.out.println(getString("message"));

    }
}

```

Now we need to start a server and a connection (client). We also need to register both packages there.

``server.java``
```java
import de.epsdev.packages.Server;

public class server {

    public static void main(String [] args) {
        // Create the server
        // Int(port) Int(packageSize) Boolean(do_encrypt)
        Server server = new Server(1010, 512,true);
        
        // Register the packages
        // String(name) Class(package_class)
        server.registerPackage("PackageRequestEcho",PackageRequestEcho.class);
        server.registerPackage("PackageRespondEcho",PackageRespondEcho.class);
            
        // Start the server
        server.start();

        System.out.println("server ready");
    }
}
```

``client.java``
````java
import de.epsdev.packages.Connection;

public class client {

  public static void main(String [] args)  {
    // Initialize the connection
    // String(hostname) Int(port)
    Connection connection = new Connection("localhost",1010);

    // Register the packages
    // String(name) Class(package_class)
    connection.registerPackage("PackageRequestEcho",PackageRequestEcho.class);
    connection.registerPackage("PackageRespondEcho",PackageRespondEcho.class);

    // Start the connection
    connection.start();

    // Create the package
    PackageRequestEcho packageRequestEcho = new PackageRequestEcho("I am a message.");
    // Send the package
    connection.send(packageRequestEcho);
  }
}
````

Now start the server. Then run the client. If you did anything right you should see following output in the console:
````
I AM A MESSAGE.
````

Now something different: Error handling. Let's say that you have written a chat server that where you need to login.
A user has used the wrong password. How would you handle this problem? One way is using the ``PackageServerError``
package. It sends a defined error message to the client where it throws an exception.

````java
new PackageServerError("Wrong login credentials!").send(socket);
````

The client then prints:

````java
de.epsdev.packages.exeptions.PackageServerErrorException: Wrong login credentials!
	at de.epsdev.packages.packages.PackageServerError.onPackageReceive(PackageServerError.java:25)
	at de.epsdev.packages.Connection.run(Connection.java:42)
````

## Detailed documentation

Following soon.
