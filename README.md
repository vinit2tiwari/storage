<h1 class="code-line" data-line-start=0 data-line-end=1 ><a id="storage_0"></a>storage</h1>
<p class="has-line-data" data-line-start="1" data-line-end="2">This repo acts as a sdk for fileStorage operations. LocalStorage is provided as default implementation.</p>
<p class="has-line-data" data-line-start="3" data-line-end="4">Steps to add other storage engines and using them :</p>
<pre><code>1.Make sure sdk is scanned by your application.
2. Make sure your storage engine implements StorageEngine class.
3. in application.properties of your application add storage.name = STORAGE_NAME.
4. load storage.name in a local variable inside your class 
      @Value(&quot;${storage.name}&quot;)
      private String engineName;
5. Use StorageEngineFactory to get instance of your storage engine.
6. For more details refer test class.</code></pre>
