# forex-mtl

[Forex](forex-mtl) is a simple application that acts as a local proxy for getting exchange rates

   It can be executed by following instructions:
   
   - Sbt run(from root directory)
    
    sigurd@sigurd:~/projects/forex-mtl> sbt run 
    
  Launched server will listen localhost on 8080 port. You can use curl to make requests e.g.
  
    sigurd@sigurd:~/projects/forex-mtl> curl localhost:8080/rates/convert/from/USD/to/EUR
    
  Note: to handle Github API rate limit restriction set environment variable FOREX_TOKEN as following:
  
    sigurd@sigurd:~/projects/forex-mtl> FOREX_TOKEN=2c30c4fa74eba4f181245bbb5ab0acacac18eccd; export FOREX_TOKEN_TOKEN
    
  Functional side of the project is powered by ZIO and ZIO Layers. scalacache library is used to provide caching capabilities.
  Kaffeine cache implementation is used by default but it's possible to switch on Redis, memcache 
  or other appropriate caching solution.
  
  TODO:
   - make sophisticated error handling in http4s routes layer
   - make host and port in service url configurable
   - finish http routes functional test
   
  Possible further improvements:
  
   - write json decoding unit tests
   - write integration tests
   - use tapir for http4s endpoints
   - get rid of Future transformations dealing with memoizeF 
  
   