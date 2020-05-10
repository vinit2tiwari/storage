# storage
This repo acts as a sdk for fileStorage operations. LocalStorage is provided as default implementation.<br/>

Steps to add other storage engines and using them : <br/>
  <li>
    <ol>Make sure sdk is scanned by your application.</ol>
    <ol>Make sure your storage engine implements StorageEngine class.</ol>
    <ol>in application.properties of your application add storage.name = STORAGE_NAME.</ol>
    <ol>load storage.name in a local variable inside your class <br/>
          @Value("${storage.name}")<br/>
          private String engineName;</ol>
    <ol>Use StorageEngineFactory to get instance of your storage engine.</ol>
    <ol>For more details refer test class.</ol>
  <li>
