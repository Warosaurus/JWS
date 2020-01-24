# Java Web Server

A web server written from scratch in Java.

**Note:** This is more of an exploratory project to understand how web servers work.

## Features
At the moment JWS only supports retrieving html documents in the default `resources/html` directory on port 8080.

Building the project with gradle and then running will start the server at 127.0.0.1:8080.
Visiting http://127.0.0.1:8080/ in a browser you should see the following:

``Welcome!``

## Road-map
  * [ ] Add unit tests
  * [x] Add logging
  * [ ] Add support for file types other than html:
    * [ ] JavaScript
    * [ ] CSS
    * [ ] Images
  * [ ] Add support for POST requests
  * [ ] Add cookie support
  * [ ] Add configuration support:
    * [ ] Port number
    * [ ] Resource directory
    * [ ] Log directory
    * [ ] Routes (?)
  * [ ] Add a Threadpool to deal with requests as they arrive
  * [ ] Add caching for repeated requests
  * [ ] Add a templating engine (?)
