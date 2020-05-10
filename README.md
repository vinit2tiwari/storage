# storage
This repo acts as a sdk for fileStorage operations. LocalStorage is provided as default implementation.

Steps to add other storage engines and using them : 
  1.Make sure sdk is scanned by your application.
  2. Make sure your storage engine implements StorageEngine class.
  3. in application.properties of your application add storage.name = STORAGE_NAME.
  4. load storage.name in a local variable inside your class 
          @Value("${storage.name}")
          private String engineName;
  5. Use StorageEngineFactory to get instance of your storage engine.
  6. For more details refer test class.
